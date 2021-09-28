package co.jeeon.exam.eventcalender.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import co.jeeon.exam.eventcalender.models.Event;

@Dao
@TypeConverters(DateConverter.class)
public interface EventDao {

    @Query("SELECT * FROM "+ DbConstants.TABLE_NAME_EVENT+" ORDER BY event_date DESC")
    LiveData<List<Event>> getAllEvents();


    @Query("SELECT * FROM "+DbConstants.TABLE_NAME_EVENT+" WHERE event_date = :eventDate"+" ORDER BY event_date DESC")
    LiveData<List<Event>> getEventsByDate(Date eventDate);


    /*
     * Insert the object in database
     * @param event, object to be inserted
     */
    @Insert
    void insert(Event event);

    /*
     * Insert the object in database
     * @param event, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Event> event);


    /*
     * update the object in database
     * @param event, object to be updated
     */
    @Update
    void update(Event repos);

    /*
     * delete the object from database
     * @param event, object to be deleted
     */
    @Delete
    void delete(Event event);

    /*
     * delete list of objects from database
     * @param event, array of objects to be deleted
     */
    @Delete
    void delete(Event... event);      // Event... is var args, here event is an array

}
