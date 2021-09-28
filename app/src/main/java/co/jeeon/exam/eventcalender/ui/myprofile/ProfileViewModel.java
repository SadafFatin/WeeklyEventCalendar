package co.jeeon.exam.eventcalender.ui.myprofile;

import android.app.Application;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import co.jeeon.exam.eventcalender.R;
import co.jeeon.exam.eventcalender.models.Event;
import co.jeeon.exam.eventcalender.models.User;
import co.jeeon.exam.eventcalender.repository.EventDataRepository;

public class ProfileViewModel extends AndroidViewModel {

    private final EventDataRepository eventDataRepository;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private final GoogleSignInClient mGoogleSignInClient;
    private final User user;
    private final Application application;


    public ProfileViewModel(Application application) {
        super(application);
        this.application = application;
        eventDataRepository = new EventDataRepository(application);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(application.getApplicationContext().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize GoogleSignIn Client and Firebase Auth
        this.mGoogleSignInClient = GoogleSignIn.getClient(application, gso);
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        this.user = new User(this.currentUser);
    }


    public User getCurrentUser() {
        return this.user;
    }

    public void isLoggedInSuccessfully(FirebaseUser firebaseUser) {
        this.user.loggedIn(firebaseUser);
    }

    public FirebaseAuth getmAuth() {
        return this.mAuth;
    }

    public void hasLoggedOut() {
        this.user.loggedOut();
    }


    public Task<Void> syncLocalDataWithServer(User user, List<Event> eventList) {
        return eventDataRepository.syncLocalEventsWithServer(user, eventList);
    }

    public Task<QuerySnapshot> fetchEventsFromServer(User user) {
        return eventDataRepository.fetchEventsFromServer(user);
    }

    public LiveData<List<Event>> getAllEvents() {
        return eventDataRepository.getAllEvents();
    }

    public void insertAllEvents(List<Event> eventList) {
        eventDataRepository.insert(eventList);
    }

    public void signIn(ActivityResultLauncher<Intent> mStartForResult) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mStartForResult.launch(signInIntent);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            this.hasLoggedOut();
        });
    }

    public void setMAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }
}