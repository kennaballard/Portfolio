package com.example.a1649618.ui.util;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * A TimePickerDialogFragment customizable with default time and with user specified event handling
 *
 *  - based on class found in https://developer.android.com/guide/topics/ui/controls/pickers.html
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class TimePickerDialogFragment extends DialogFragment {

    // constants for bundling default hour/minute
    private static final String EXTRA_HOUR = "hour";
    private static final String EXTRA_MINUTE = "minute";

    // custom event listener
    private TimePickerDialog.OnTimeSetListener listener;

    /**
     * Create a TimePickerDialogFragment with a default hour and minute, and an event listener.
     * @param start default time
     * @param listener event listener
     * @return
     */
    public static TimePickerDialogFragment create(Date start, TimePickerDialog.OnTimeSetListener listener) {
        TimePickerDialogFragment f = new TimePickerDialogFragment();
        f.setTimeSetListener(listener);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);

        Bundle bundle = new Bundle(2);
        bundle.putInt(EXTRA_HOUR,   calendar.get(Calendar.HOUR));
        bundle.putInt(EXTRA_MINUTE, calendar.get(Calendar.MINUTE));
        f.setArguments(bundle);
        return f;
    }

    /**
     * Create a TimePickerDialogFragment with an event listener. The default hour/minute will be the current time.
     * @param listener event listener
     * @return
     */
    public static TimePickerDialogFragment create(TimePickerDialog.OnTimeSetListener listener) {
        return create(new Date(), listener);
    }


    public void setTimeSetListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int hour;
        int minute;

        Bundle args = getArguments();
        if(args.containsKey(EXTRA_HOUR)) {
            hour = args.getInt(EXTRA_HOUR);
            minute = args.getInt(EXTRA_MINUTE);
        }
        else {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), listener, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}
