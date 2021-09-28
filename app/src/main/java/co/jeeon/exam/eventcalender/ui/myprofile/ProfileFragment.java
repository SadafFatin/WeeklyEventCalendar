package co.jeeon.exam.eventcalender.ui.myprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import co.jeeon.exam.eventcalender.R;
import co.jeeon.exam.eventcalender.databinding.FragmentProfileBinding;
import co.jeeon.exam.eventcalender.models.Event;
import co.jeeon.exam.eventcalender.ui.LifeCycleLoggingFragment;
import co.jeeon.exam.eventcalender.utils.UiUtils;

public class ProfileFragment extends LifeCycleLoggingFragment implements View.OnClickListener {

    private ProfileViewModel profileViewModel;
    private @NonNull
    FragmentProfileBinding binding;

    public static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;


    /**
     * Called to do initial creation of a fragment.This is called after
     * {@link #onAttach(Context)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     *
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     *
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //prepare view model and binding objects for later use
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setCurrentUser(profileViewModel.getCurrentUser());
        binding.fetchMyData.setOnClickListener(this);
        binding.syncMyData.setOnClickListener(this);
        binding.signOut.setOnClickListener(this);
        binding.signIn.setOnClickListener(this);
        return binding.getRoot();
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI();
    }

    public void updateUI() {
        if (profileViewModel.getCurrentUser() ==null) {
            UiUtils.showToast(requireView(), requireActivity(), getString(R.string.signin_prompt));
        } else {
            Glide.with(requireContext())
                    .load(profileViewModel.getCurrentUser().getImage()) // Uri of the picture
                    .transform(new CircleCrop())
                    .into(binding.avater);
        }
    }


    private void signIn() {
       profileViewModel.signIn(mStartForResult);
    }
    public void signOut() {
        profileViewModel.signOut();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    // [START auth_with_google]
    public void firebaseAuthWithGoogle(Activity activity , String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        profileViewModel.getmAuth().signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        profileViewModel.isLoggedInSuccessfully(profileViewModel.getmAuth().getCurrentUser());
                        updateUI();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }


                });
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        Log.d(TAG, result.getResultCode() + result.getData().getDataString());
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            try {
                                // Google Sign In was successful, authenticate with Firebase
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                firebaseAuthWithGoogle(requireActivity(),account.getIdToken());
                            } catch (ApiException e) {
                                // Google Sign In failed, update UI appropriately
                                Log.w(TAG, "Google sign in failed", e);
                            }
                        }
                    });

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sync_my_data) {
            profileViewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
                profileViewModel.syncLocalDataWithServer(profileViewModel.getCurrentUser(), events).addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            UiUtils.showToast(requireView(), requireContext(), "Successfully Synchronized your events in cloud");
                        } else {
                            UiUtils.showToast(requireView(), requireContext(), "Failed Synchronizing your events in cloud");
                        }
                    }
                });
            });
        }

        if (v.getId() == R.id.fetch_my_data) {
            profileViewModel.fetchEventsFromServer(profileViewModel.getCurrentUser()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Event> eventList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        eventList.add(doc.toObject(Event.class));
                    }
                    profileViewModel.insertAllEvents(eventList);
                    UiUtils.showToast(requireView(), requireContext(), "Successfully Fetched events from cloud");
                } else {
                    UiUtils.showToast(requireView(), requireContext(), "Failed Fetching your events from cloud");
                }
            });
        }
        if (v.getId() == R.id.sign_out) {
            signOut();
        }
        if (v.getId() == R.id.sign_in) {
            signIn();
        }

    }
}