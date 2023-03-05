package com.example.disneytripplanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disneytripplanner.databinding.FragmentHomeBinding;
import com.example.disneytripplanner.databinding.FragmentNewTripBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class NewTripFragment extends Fragment {
    private static final String TAG = "new trip fragment";
    NewTripFragmentListener mListener;
    FragmentNewTripBinding binding;
    private FirebaseAuth mAuth;
    String startDate;
    String endDate;

    final SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ssZ");
    SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");

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
        String todaysDate = sdf.format(calendar.getTime());
        String dateString = String.format("%s", todaysDate);
        setStartDate(todaysDate);
        setEndDate(todaysDate);
        binding.editTextStartDate.setText(dateString);
        binding.editTextEndDate.setText(dateString);
    }

    private void createTrip() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String userUid = user.getUid();

        HashMap<String, Object> trip = new HashMap<>();

        String tripName = binding.editTextNewTripName.getText().toString();
        String description = binding.editTextTripDescription.getText().toString();
        trip.put("tripName", tripName);
        trip.put("description", description);

        db.collection("users")
                .document(userUid)
                .collection("trips")
                .add(trip)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "New trip was successfully created! --> " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error creating new trip" + e);
                    }
                });

    }

    private void setupUI() {
        getTodaysDate();
    }

    public NewTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewTripBinding.inflate(inflater, container, false);

        setupUI();

        /*
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair selectedDates = (Pair) materialDatePicker.getSelection();
//              then obtain the startDate & endDate from the range
                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
//              assigned variables
                Date startDate = rangeDate.first;
                Date endDate = rangeDate.second;
//              Format the dates in ur desired display mode
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd MMM yyyy");
//              Display it by setText
                binding.textViewStartDate.setText(simpleFormat.format(startDate));
                binding.textViewEndDate.setText(simpleFormat.format(endDate));
            }
        });
         */
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("New Trip");

        /*
        MaterialDatePicker.Builder startDateBuilder = MaterialDatePicker.Builder.datePicker();
        startDateBuilder.setTitleText("SELECT A START DATE");

        final MaterialDatePicker startDatePicker = startDateBuilder.build();

        binding.imageViewCalendarStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        startDatePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        startDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        binding.textViewStartDate.setText(startDatePicker.getHeaderText());
                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });
               */
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (NewTripFragmentListener) context;
    }

    interface NewTripFragmentListener {
        void cancelNewTrip();
        void goToHomePage();
        void selectTripDates();
    }
}