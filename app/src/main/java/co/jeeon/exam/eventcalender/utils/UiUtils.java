package co.jeeon.exam.eventcalender.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import co.jeeon.exam.eventcalender.R;

/**
 * This utility class defines static methods shared by various Activities.
 */
public class UiUtils {
    /**
     * Debugging tag.
     */
    private static final String TAG =
            UiUtils.class.getCanonicalName();
    public static final  String[] weekDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};


    /**
     * Ensure this class is only used as a utility.
     */
    private UiUtils() {
        throw new AssertionError();
    }

    /**
     * Show a toast message.
     */
    public static void showToast(Context context,
                                 String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * shows toast via Snackbar
     */
    public static void showToast(View view, Context context, String msg) {
        Snackbar.make(
                view,
                msg,
                Snackbar.LENGTH_LONG).show();
    }

    /**
     * shows toast via alert dialog
     */
    public static void showAlertInfoDialog(Activity activity, String title, String msg) {
        DialogInterface.OnClickListener okListener = (dialog, which) ->
        {
            dialog.dismiss();
        };
        new AlertDialog.Builder(activity)
                .setMessage(msg)
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton(R.string.prompt_cancel, okListener)
                .create().show();
    }


    /**
     * @param title
     * @param message Show general info on various user events on webview
     */
    public static void showSuccessInfoDialog(Activity activity, String title, String message) {

        int dialogIcon = R.drawable.ic_check;
        DialogInterface.OnClickListener okListener = (dialog, which) ->
        {
            dialog.dismiss();
        };
        new android.app.AlertDialog.Builder(activity)
                .setTitle(title)
                .setIcon(dialogIcon)
                .setMessage(message)
                .setPositiveButton(R.string.prompt_proceed_button, okListener)
                .create()
                .show();
    }


    /**
     * shows toast via Snackbar
     */
    public static void showToastWithLoginAction(View view, Activity context,Intent signInIntent,
                                                ActivityResultLauncher<Intent> mStartForResult) {
        Snackbar snackbar =
                Snackbar.make(
                        view,
                        context.getString(R.string.prompt_sign_in),
                        6000)
                        .setActionTextColor(context.getResources().getColor(R.color.reply_orange_400, context.getTheme()));

        snackbar.setAction(
                R.string.prompt_sign_in,
                view1 -> {
                    mStartForResult.launch(signInIntent);
                });

        snackbar.show();
    }


    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the url.
     */
    public static void hideKeyboard(Activity activity,
                                    IBinder windowToken) {
        InputMethodManager mgr =
                (InputMethodManager) activity.getSystemService
                        (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    /**
     * Shows a progress dialog while doing background task
     */
    public static class ProcessingDialog {

        private WeakReference<Activity> activity;
        private Dialog dialog;

        //..we need the context else we can not create the dialog so get context in constructor
        public ProcessingDialog(Activity activity) {
            this.activity = new WeakReference<>(activity);
        }

        public void showDialog() {

            dialog = new Dialog(activity.get());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //...set cancelable false so that it's never get hidden
            dialog.setCancelable(false);
            //...that's the layout i told you will inflate later
            //dialog.setContentView(R.layout.custom_loading_layout);
            dialog.show();
        }

        //..also create a method which will hide the dialog when some work is done
        public void hideDialog() {
            dialog.dismiss();
        }

    }

    public static void showDialog(ProcessingDialog mProgressDialog) {
        if (mProgressDialog != null) {
            mProgressDialog.showDialog();
        }
    }

    public static void hideDialog(ProcessingDialog mProgressDialog) {
        if (mProgressDialog != null) {
            mProgressDialog.hideDialog();
        }
    }



    /**
     * Hide days from date picker layout
     *
     * @param mDatePicker
     */
    public static DatePicker hideDay(DatePicker mDatePicker) {
        try {
            /* Dealing with special situations above Android 5.0 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //Get DayView according to FindView method
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = mDatePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                //Get the DayView field according to the reflection method
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDatePicker;
    }


    /**
     * Hashmap weekDayColorPairMap is for Mapping different color to each weekday.
     */
    private static final HashMap<String, Integer> weekDayColorPairMap;
    private static SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEEE-d MMM,yyyy", Locale.getDefault());
    private static SimpleDateFormat shortDateFormat = new SimpleDateFormat("EE-dd/MM", Locale.getDefault());


    /**
     * initialization of static var weekDayColorPairMap.Mapping color to each weekday.
     */
    static {
        weekDayColorPairMap = new HashMap<>();
        weekDayColorPairMap.put("SAT", R.color.saturday);
        weekDayColorPairMap.put("SUN", R.color.sunday);
        weekDayColorPairMap.put("MON", R.color.monday);
        weekDayColorPairMap.put("TUE", R.color.tuesday);
        weekDayColorPairMap.put("WED", R.color.wednesday);
        weekDayColorPairMap.put("THU", R.color.thursday);
        weekDayColorPairMap.put("FRI", R.color.friday);
    }

    /**
     *
     * @param weekDay
     * @return color resource for weekday
     */
    public static int weekDayColor(String weekDay) {
        if (weekDayColorPairMap.containsKey(weekDay)) {
            return weekDayColorPairMap.get(weekDay);
        }
        return R.color.saturday;
    }

    /**
     *
     * @param date
     * @return formatted date consisting  WeekDay and Date then seperate them into array
     */
    public static String[] formatDateIntoWeekDayArray(Date date){
        return fullDateFormat.format(date).split("-");
    }


    /**
     *
     * @param date
     * @return formatted date into WeekDay text
     */
    public static String formatDateIntoWeekDay(Date date){
        return fullDateFormat.format(date) ;
    }

    /**
     *
     * @param date
     * @return formatted date into WeekDay text
     */
    public static String[] formatDateIntoShortDateArray(Date date){
        return shortDateFormat.format(date).split("-");
    }


    public enum FormErrors {
        INVALID_EVENT_TITLE,
        INVALID_EVENT_DESC,
        INVALID_EVENT_TIME,
    }

    /**
     * @param value
     * @return true if string
     */
    public static boolean validateRequiredField(String value) {
        if (value == null) {
            return false;
        } else return !value.equals("") && !value.equals(" ");
    }
}
