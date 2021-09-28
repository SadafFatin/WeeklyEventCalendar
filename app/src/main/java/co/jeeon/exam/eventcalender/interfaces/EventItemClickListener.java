package co.jeeon.exam.eventcalender.interfaces;

import android.view.View;

import co.jeeon.exam.eventcalender.models.Event;

public interface EventItemClickListener {
    void onEventItemOnClick(View view, int layoutPosition, Event event);
}
