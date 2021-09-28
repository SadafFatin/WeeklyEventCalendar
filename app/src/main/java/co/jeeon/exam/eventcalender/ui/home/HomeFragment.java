package co.jeeon.exam.eventcalender.ui.home;

import static co.jeeon.exam.eventcalender.utils.UiUtils.formatDateIntoWeekDayArray;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.jeeon.exam.eventcalender.R;
import co.jeeon.exam.eventcalender.databinding.FragmentHomeBinding;
import co.jeeon.exam.eventcalender.interfaces.EventItemClickListener;
import co.jeeon.exam.eventcalender.interfaces.WeekDaysItemClickListener;
import co.jeeon.exam.eventcalender.models.Event;
import co.jeeon.exam.eventcalender.models.WeekDay;
import co.jeeon.exam.eventcalender.ui.LifeCycleLoggingFragment;
import co.jeeon.exam.eventcalender.ui.event.EventCreateUpdateFragment;
import co.jeeon.exam.eventcalender.viewadapters.EventAdapter;
import co.jeeon.exam.eventcalender.viewadapters.WeekDaysAdapter;

public class HomeFragment extends LifeCycleLoggingFragment implements WeekDaysItemClickListener, View.OnClickListener, EventItemClickListener {


    public static final String TAG = "EVENT CALENDAR";
    private static final int INITIAL_POSITION = 100;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private Calendar currentWeekPointer, nextWeekPointer, previousWeekPointer;
    private WeekDay currentSelectedDate;
    private List<Event> currentSelectedDatesEvent, allDatesEvents;

    //Indexing according to calendars day value SUN-SAT 1-7
    private List<WeekDay> weekDaysList;
    private WeekDaysAdapter weekDaysAdapter;
    private EventAdapter eventsAdapter;


    /**
     * Called to do initial creation of a fragment.This is called after
     * and before
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
        // initialization calendar variables

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.dateDisplayToday.setOnClickListener(this);
        binding.calendarNextButton.setOnClickListener(this);
        binding.calendarPrevButton.setOnClickListener(this);
        binding.addNewEventFab.setOnClickListener(this);

        currentWeekPointer = Calendar.getInstance();
        nextWeekPointer = Calendar.getInstance();
        previousWeekPointer = Calendar.getInstance();
        nextWeekPointer.add(Calendar.DATE, 7);
        // by default currentSelectedDate is today's date
        currentSelectedDate = new WeekDay(Calendar.getInstance().getTime());
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
        //week days list setup
        weekDaysList = daysInCurrentWeek();
        weekDaysAdapter = new WeekDaysAdapter(requireContext(), this.weekDaysList, this);
        binding.weekDaysListView.setAdapter(weekDaysAdapter);
        //mark currentSelected day at display and list
        markCurrentSelectedDay(currentSelectedDate, INITIAL_POSITION);

        //events list setup
        currentSelectedDatesEvent = new ArrayList<>();
        allDatesEvents = new ArrayList<>();
        homeViewModel.getCurrentDateChanged().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                setUpCurrentSelectedDaysEvent();
            }
        });

    }


    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link #onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link #onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link #onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * @return the current weeks days according to currentWeekPointer value
     */
    private ArrayList<WeekDay> daysInCurrentWeek() {
        ArrayList<WeekDay> days = new ArrayList<>();
        currentWeekPointer.setFirstDayOfWeek(Calendar.SATURDAY);
        currentWeekPointer.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        for (int i = 0; i < 7; i++) {
            days.add(new WeekDay(currentWeekPointer.getTime()));
            currentWeekPointer.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }

    /**
     * @return the days in the next week from week nextWeekPointer is pointing to now
     */
    private ArrayList<WeekDay> daysInNextWeek() {
        ArrayList<WeekDay> days = new ArrayList<>();
        nextWeekPointer.setFirstDayOfWeek(Calendar.SATURDAY);
        nextWeekPointer.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        for (int i = 0; i < 7; i++) {
            days.add(new WeekDay(nextWeekPointer.getTime()));
            nextWeekPointer.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }

    /**
     * @return the days in the prev week from the week previousWeekPointer is pointing to now
     */
    private ArrayList<WeekDay> daysInPrevWeek() {
        ArrayList<WeekDay> days = new ArrayList<>();
        previousWeekPointer.add(Calendar.DATE, -7);
        previousWeekPointer.setFirstDayOfWeek(Calendar.SATURDAY);
        previousWeekPointer.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        for (int i = 0; i < 7; i++) {
            days.add(new WeekDay(previousWeekPointer.getTime()));
            previousWeekPointer.add(Calendar.DAY_OF_MONTH, 1);
        }
        previousWeekPointer.add(Calendar.DATE, -7);
        return days;
    }

    /**
     * Mark the current selected day in the list and scroll to that position
     */
    private void markCurrentSelectedDay(WeekDay weekDay, int position) {
        currentSelectedDate = weekDay;
        Log.d(TAG, currentSelectedDate.getWeekDate());
        //find out today's position and mark it
        if (position == HomeFragment.INITIAL_POSITION) {
            for (int i = 0; i < weekDaysList.size(); i++) {
                if (weekDaysList.get(i).isEquals(currentSelectedDate)) {
                    position = i;
                    weekDaysAdapter.setCurrentSelectedPosition(position);
                    break;
                }
            }
        }
        binding.weekDaysListView.scrollToPosition(position);
        binding.dateDisplayDate.setText(formatDateIntoWeekDayArray(currentSelectedDate.getDate())[1]);
        binding.dateDisplayDay.setText(formatDateIntoWeekDayArray(currentSelectedDate.getDate())[0]);
    }

    /**
     * Mark the current selected days detail view and loads the events
     * of the current selected day
     */
    private void setUpCurrentSelectedDaysEvent() {
        homeViewModel.getEventsByDate(currentSelectedDate.getDate()).observe(getViewLifecycleOwner(), events -> {
            currentSelectedDatesEvent.clear();
            currentSelectedDatesEvent.addAll(events);
            Log.d(TAG, currentSelectedDatesEvent.toString());
            if (currentSelectedDatesEvent.size() > 0) {
                binding.noEventLayout.setVisibility(View.GONE);
                eventsAdapter = new EventAdapter(requireContext(), currentSelectedDatesEvent, this);
                binding.eventsListView.setAdapter(eventsAdapter);
            } else {
                binding.noEventLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onWeekDayItemOnClick(int layoutPosition) {
        markCurrentSelectedDay(weekDaysList.get(layoutPosition), layoutPosition);
        setUpCurrentSelectedDaysEvent();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_new_event_fab) {
            EventCreateUpdateFragment.newInstance(new Event("", "", currentSelectedDate.getDate()), false)
                    .show(getActivity().getSupportFragmentManager(), TAG);
        }
        if (v.getId() == R.id.date_display_today) {
            currentSelectedDate = new WeekDay(Calendar.getInstance().getTime());
            markCurrentSelectedDay(currentSelectedDate, INITIAL_POSITION);
            homeViewModel.setCurrentDateChanged("Current Date changed");
        }
        if (v.getId() == R.id.calendar_next_button) {
            weekDaysList.addAll(daysInNextWeek());
            markCurrentSelectedDay(weekDaysList.get(weekDaysList.size() - 7), INITIAL_POSITION);
            homeViewModel.setCurrentDateChanged("Current Date changed");

        }
        if (v.getId() == R.id.calendar_prev_button) {
            weekDaysList.addAll(0, daysInPrevWeek());
            markCurrentSelectedDay(weekDaysList.get(0), INITIAL_POSITION);
            homeViewModel.setCurrentDateChanged("Current Date changed");
        }
    }

    @Override
    public void onEventItemOnClick(View view, int layoutPosition, Event event) {
        if (view.getId() == R.id.event_delete) {
            homeViewModel.deleteEvent(event);
            homeViewModel.setCurrentDateChanged("Current Date changed");

        } else if (view.getId() == R.id.event_edit) {
            EventCreateUpdateFragment.newInstance(event, true)
                    .show(getActivity().getSupportFragmentManager(), TAG);
        }

    }
}

