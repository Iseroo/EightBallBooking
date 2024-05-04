package com.example.eightballbooking;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DateTimePickerFragment extends DialogFragment {

    private DateTimePickerListener listener;

    public interface DateTimePickerListener {
        void onDateTimeSelected(Calendar dateTime);
    }

    public DateTimePickerFragment(DateTimePickerListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Inflate the layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_date_time_picker, null);

        // Initialize date and time pickers
        DatePicker datePicker = view.findViewById(R.id.date_picker);
        TimePicker timePicker = view.findViewById(R.id.time_picker);
        datePicker.init(year, month, day, null);
        timePicker.setIs24HourView(true);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);

        // Create a new instance of DatePickerDialog and return it
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Set", (dialog, id) -> {
                    Calendar selectedTime = Calendar.getInstance();
                    selectedTime.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                            timePicker.getHour(), timePicker.getMinute());
                    listener.onDateTimeSelected(selectedTime);
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                .create();
    }
}
