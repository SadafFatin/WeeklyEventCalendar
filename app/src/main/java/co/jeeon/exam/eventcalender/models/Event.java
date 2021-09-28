package co.jeeon.exam.eventcalender.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import co.jeeon.exam.eventcalender.db.DateConverter;

@Entity(tableName = "table_event")
public class Event implements Serializable {


    public Event(String eventTitle, String eventDesc, Date eventDate) {
        this.eventTitle = eventTitle;
        this.eventDesc = eventDesc;
        this.eventDate = eventDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (this.eventId != event.eventId) return false;
        return eventDate != null ? eventDate.equals(event.eventDate) : event.eventDate == null;
    }



    @Override
    public int hashCode() {
        int result = eventId;
        result = 31 * result + (eventDate != null ? eventDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "event_id=" + eventId +
                ", event_date='" + eventDate + '\'' +
                ", event_desc='" + eventDesc + '\'' +
                '}';
    }


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    private int eventId;

    @ColumnInfo(name = "event_title")
    private String eventTitle;

    @ColumnInfo(name = "event_desc")
    private String eventDesc;

    @ColumnInfo(name = "event_date")
    @TypeConverters(DateConverter.class)
    private Date eventDate;


    public Event() {
    }
}
