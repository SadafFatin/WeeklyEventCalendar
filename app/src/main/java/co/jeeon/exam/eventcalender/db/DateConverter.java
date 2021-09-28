package co.jeeon.exam.eventcalender.db;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateConverter {
    private static DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");

    @TypeConverter
    public static Date toDate(String timestamp) {
        try {
            return timestamp == null ? null : df.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    @TypeConverter
    public static String toTimestamp(Date date) {
        return date == null ? null : df.format(date.getTime());
    }
}
/*

 @TypeConverter
    public static Date toDate(String timestamp) {
        try {
            return timestamp == null ? null : df.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String toTimestamp(Date date) {
        return date == null ? null : df.format(date);
    }
 */