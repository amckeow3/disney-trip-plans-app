package com.example.disneytripplanner;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;

import com.example.disneytripplanner.databinding.FragmentNewTripBinding;
import com.example.disneytripplanner.databinding.FragmentSelectTripDatesBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectTripDatesFragment extends Fragment {
    FragmentSelectTripDatesBinding binding;
    SelectTripDatesFragmentListener mListener;
    String startDate;
    String endDate;
    //eFormatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ssZ");
    //SimpleDateFormat
    final SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
    SimpleDateFormat datformatTime = new SimpleDateFormat("h:mm a");

    public void setStartDate(String sDate) {
        startDate = sDate;
    }

    public void setEndDate(String eDate) {
        endDate = eDate;
    }

    private void getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year, month, dayOfMonth);
        String sDate = sdf.format(calendar.getTime());
        String dateString = String.format("%s", sDate);
        setStartDate(sDate);
    }

    private void setupUI() {
        getTodaysDate();

        binding.datePickerTripDates.setMinDate(new Date().getTime());
    }

    public SelectTripDatesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectTripDatesBinding.inflate(inflater, container, false);
        setupUI();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SelectTripDatesFragmentListener) context;
    }

    interface SelectTripDatesFragmentListener {

    }

}