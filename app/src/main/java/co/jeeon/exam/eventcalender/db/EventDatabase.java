package co.jeeon.exam.eventcalender.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import co.jeeon.exam.eventcalender.models.Event;

@Database(entities = { Event.class }, version = 1)
@TypeConverters(DateConverter.class)
public abstract class EventDatabase extends RoomDatabase {

    public abstract EventDao getEventDao();

    private static EventDatabase eventDB;

    public static EventDatabase getInstance(Context context) {
        if (null == eventDB) {
            eventDB = buildDatabaseInstance(context);
        }
        return eventDB;
    }

    private static EventDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                EventDatabase.class,
                DbConstants.DB_NAME).build();
    }

    public void cleanUp(){
        eventDB = null;
    }

}