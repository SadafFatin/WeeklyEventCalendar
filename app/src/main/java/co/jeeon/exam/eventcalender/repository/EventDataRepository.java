package co.jeeon.exam.eventcalender.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import co.jeeon.exam.eventcalender.db.EventDatabase;
import co.jeeon.exam.eventcalender.models.Event;
import co.jeeon.exam.eventcalender.models.User;

public class EventDataRepository {


    private LiveData<List<Event>> eventListByDate;
    private MutableLiveData<String> operationResult = new MutableLiveData<>();

    //Remote db conf
    FirebaseFirestore eventDatabaseRemote;
    //Local db conf
    private final EventDatabase eventDatabase;

    public EventDataRepository(Application application) {
        this.eventDatabase = EventDatabase.getInstance(application);
        this.eventDatabaseRemote = FirebaseFirestore.getInstance();
    }

    public LiveData<List<Event>> getAllEvents() {
        return eventDatabase.getEventDao().getAllEvents();
    }

    public LiveData<List<Event>> getEventsByDate(Date eventDate) {
        eventListByDate = eventDatabase.getEventDao().getEventsByDate(eventDate);
        return eventListByDate;
    }

    public void insert(Event event) {
        insertAsync(event);
    }

    public void insert(List<Event> eventList) {
        insertAsync(eventList);
        Log.d(" fetchEventsFromLocal ", this.getAllEvents().toString());
    }

    public void deleteEvent(Event event) {
        deleteAsync(event);
    }

    public void updateEvent(Event event) {
        updateAsync(event);
    }

    public MutableLiveData<String> getOperationResult() {
        return operationResult;
    }

    private void insertAsync(Event event) {
        new Thread(() -> {
            try {
                eventDatabase.getEventDao().insert(event);
                operationResult.postValue("Successfully Inserted");
            } catch (Exception e) {
                operationResult.postValue("Failed Inserting");
            }
        }).start();
    }

    private void insertAsync(List<Event> eventList) {
        new Thread(() -> {
            try {
                eventDatabase.getEventDao().insertAll(eventList);
                operationResult.postValue("Successfully Inserted");
            } catch (Exception e) {
                operationResult.postValue("Failed Inserting");
            }
        }).start();
    }

    private void deleteAsync(Event event) {
        new Thread(() -> {
            try {
                eventDatabase.getEventDao().delete(event);
                operationResult.postValue("Successfully Deleted");
            } catch (Exception e) {
                operationResult.postValue("Failed Deleting");
            }
        }).start();
    }

    private void updateAsync(Event event) {
        new Thread(() -> {
            try {
                eventDatabase.getEventDao().delete(event);
                operationResult.postValue("Successfully Updated");
            } catch (Exception e) {
                operationResult.postValue("Failed Updating");
            }
        }).start();
    }


    public Task<Void> syncLocalEventsWithServer(@NotNull User user, @NotNull List<Event> allEvents) {
        WriteBatch writeBatch = eventDatabaseRemote.batch();
        CollectionReference collectionReference = eventDatabaseRemote.collection(user.getEmail());
        for (Event event : allEvents) {
            DocumentReference documentReference = collectionReference.document(String.valueOf(event.getEventId()));
            writeBatch.set(documentReference, event);
        }
        return writeBatch.commit();
    }

    public Task<QuerySnapshot> fetchEventsFromServer(@NotNull User user) {
        return this.eventDatabaseRemote.collection(user.getEmail()).get();
    }

}

