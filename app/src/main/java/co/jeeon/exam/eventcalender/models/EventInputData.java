package co.jeeon.exam.eventcalender.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.ArrayList;

import co.jeeon.exam.eventcalender.BR;
import co.jeeon.exam.eventcalender.utils.UiUtils;

public class EventInputData extends BaseObservable {
    //validation var
    private ArrayList<UiUtils.FormErrors> errorList = new ArrayList<>();

    @Bindable
    public ArrayList<UiUtils.FormErrors> getErrorList() {
        return errorList;
    }

    public void setErrorList(ArrayList<UiUtils.FormErrors> errorList) {
        this.errorList = errorList;
        notifyPropertyChanged(BR.errorList);
    }

    @Bindable
    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
        notifyPropertyChanged(BR.eventTitle);
    }

    @Bindable
    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
        notifyPropertyChanged(BR.eventDesc);
    }

    // event info var
    private String eventTitle;
    private String eventDesc;


    public EventInputData(Event event) {
        if(event!=null){
            this.setEventTitle(event.getEventTitle());
            this.setEventDesc(event.getEventDesc());
        }
    }


    public EventInputData(){

    }

    public boolean isFormValid() {

        errorList.clear();
        if (!UiUtils.validateRequiredField(eventTitle)) {
            errorList.add(UiUtils.FormErrors.INVALID_EVENT_TITLE);
        }
        if (!UiUtils.validateRequiredField(eventDesc)) {
            errorList.add(UiUtils.FormErrors.INVALID_EVENT_DESC);
        }
        notifyPropertyChanged(BR.errorList);
        // all the other validation you require
        return errorList.isEmpty();
    }
}
