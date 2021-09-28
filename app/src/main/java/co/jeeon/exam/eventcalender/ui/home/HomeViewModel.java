package co.jeeon.exam.eventcalender.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.List;

import co.jeeon.exam.eventcalender.models.Event;
import co.jeeon.exam.eventcalender.repository.EventDataRepository;

public class HomeViewModel extends AndroidViewModel {

    private  LiveData<List<Event>> eventListByDate;
    private  LiveData<String> operationResult;



    private MutableLiveData<String> currentDateChanged;

    private EventDataRepository eventDataRepository;

    public HomeViewModel(Application application) {
        super(application);
        eventDataRepository = new EventDataRepository(application);
        operationResult = eventDataRepository.getOperationResult();
        currentDateChanged  = new MutableLiveData<>();
    }

    public LiveData<List<Event>> getEventsByDate(Date eventDate) {
        eventListByDate = eventDataRepository.getEventsByDate(eventDate);
        return eventListByDate;
    }

    public LiveData<List<Event>> getAllEvents() {
        return eventDataRepository.getAllEvents();
    }

    public void insertEvent(Event event) {
        eventDataRepository.insert(event);
    }

    public void deleteEvent(Event event) {
        eventDataRepository.deleteEvent(event);
    }


    public void updateEvent(Event event) { eventDataRepository.updateEvent(event); }

    public LiveData<String> getOperationResult() {
        return operationResult;
    }

    public MutableLiveData<String> getCurrentDateChanged() {
        return currentDateChanged;
    }

    public void setCurrentDateChanged(String val) {
        this.currentDateChanged.setValue(val);
    }

}