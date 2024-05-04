package com.example.eightballbooking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Calendar selectedDate = Calendar.getInstance();
    private TimePickerDialog.OnTimeSetListener timeListener;

    public DatePickerFragment(TimePickerDialog.OnTimeSetListener timeListener) {
        this.timeListener = timeListener;
    }

    public Calendar getSelectedDate() {
        return selectedDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        selectedDate.set(year, month, day);
        new TimePickerDialog(getContext(), timeListener, selectedDate.get(Calendar.HOUR_OF_DAY), selectedDate.get(Calendar.MINUTE), true).show();
    }
}

