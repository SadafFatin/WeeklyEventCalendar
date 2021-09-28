package co.jeeon.exam.eventcalender.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import co.jeeon.exam.eventcalender.R;
import co.jeeon.exam.eventcalender.databinding.FragmentEventCreateUpdateBinding;
import co.jeeon.exam.eventcalender.models.Event;
import co.jeeon.exam.eventcalender.models.EventInputData;
import co.jeeon.exam.eventcalender.ui.home.HomeViewModel;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     EventCreateUpdateFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class EventCreateUpdateFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_EVENT = "item_count";
    private static final String ARG_ACTION = "isUpdating";
    private FragmentEventCreateUpdateBinding binding;
    private EventInputData eventInputData;
    private Event event;
    private HomeViewModel homeViewModel;
    private boolean isUpdatingEvent;


    // TODO: Customize parameters
    public static EventCreateUpdateFragment newInstance(Event event,boolean isUpdatingEvent) {
        final EventCreateUpdateFragment fragment = new EventCreateUpdateFragment();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT, event);
        args.putBoolean(ARG_ACTION, isUpdatingEvent);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentEventCreateUpdateBinding.inflate(inflater, container, false);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding.addEvent.setOnClickListener(this);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable(ARG_EVENT);
            isUpdatingEvent = getArguments().getBoolean(ARG_ACTION);
            eventInputData = new EventInputData(event);
        }
        if(isUpdatingEvent) binding.addEvent.setText(R.string.prompt_update);

        binding.setEventData(eventInputData);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        homeViewModel.getOperationResult().observe(this.getViewLifecycleOwner(), aBoolean -> {
            this.dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.add_event) {
            if (eventInputData.isFormValid()) {
                event.setEventTitle(eventInputData.getEventTitle());
                event.setEventDesc(eventInputData.getEventDesc());
                v.setEnabled(false);
                if (isUpdatingEvent) {
                    homeViewModel.updateEvent(event);
                } else {
                    homeViewModel.insertEvent(event);
                }


            }
        }

    }
}