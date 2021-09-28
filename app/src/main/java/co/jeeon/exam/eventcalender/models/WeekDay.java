package co.jeeon.exam.eventcalender.models;

import java.util.Date;

import co.jeeon.exam.eventcalender.utils.UiUtils;

public class WeekDay {


    private String weekDay;
    private String weekDate;
    private Date date;

    public WeekDay(Date date) {
        this.date = date;
        this.weekDay = UiUtils.formatDateIntoShortDateArray(this.date)[0].toUpperCase();
        this.weekDate = UiUtils.formatDateIntoShortDateArray(this.date)[1];
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getWeekDate() {
        return weekDate;
    }

    public void setWeekDate(String weekDate) {
        this.weekDate = weekDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public boolean isEquals(WeekDay weekDate) {
        if( weekDate.getWeekDate().equals(this.weekDate)){
            return true;
        }
        else {
            return false;
        }
    }
}
