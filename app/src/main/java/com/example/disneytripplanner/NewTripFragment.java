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
    NewTripFragment.NewTripFragmentListener mListener;
    FragmentNewTripBinding binding;
    private FirebaseAuth mAuth;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Resort> resorts = new ArrayList<>();
    ArrayList<String> resortNames = new ArrayList<>();
    String startDateString;
    String endDateString;

    public NewTripFragment() {
        // Required empty public constructor
    }

    public void getDisneyResortOptions() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("resorts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        resorts.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Resort resort = new Resort();
                            resort.setId(document.getId());
                            resort.setResortName(document.getString("resort_name"));
                            //GeoPoint geoPoint = document.getGeoPoint("geo_location")
                            resorts.add(resort);
                        }

                        Log.d(TAG, "Resorts: " + resorts);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resortNames.clear();
                                String spinnerHint = "Select a Resort";
                                resortNames.add(spinnerHint);
                                for (int i = 0; i < resorts.size(); i++) {
                                    Resort resort = resorts.get(i);
                                    String name = resort.getResortName();
                                    resortNames.add(name);
                                }
                                Log.d(TAG, "Resort Options: " + resortNames);
                                Spinner resortsSpinner = binding.spinnerResortOptions;

                                arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, resortNames);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                resortsSpinner.setAdapter(arrayAdapter);
                            }
                        });
                    }
                });

    }

    public void setStartDate(String date) {
        startDateString = date;
    }
    public void setEndDate(String date) {
        endDateString = date;
    }

    private void createTrip() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String userUid = user.getUid();

        HashMap<String, Object> trip = new HashMap<>();

        String tripName = binding.editTextNewTripName.getText().toString();
        String startDate = startDateString;
        String endDate = endDateString;
        String resort = binding.spinnerResortOptions.getSelectedItem().toString();
        trip.put("tripName", tripName);
        trip.put("resort", resort);
        trip.put("startDate", startDate);
        trip.put("endDate", endDate);

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewTripBinding.inflate(inflater, container, false);

        getDisneyResortOptions();

        MaterialDatePicker.Builder startDateBuilder = MaterialDatePicker.Builder.datePicker();
        startDateBuilder.setTitleText("START DATE");
        final MaterialDatePicker startDatePicker = startDateBuilder.build();
        binding.editTextStartDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startDatePicker.show(getChildFragmentManager(), "START_DATE_PICKER");
                    }
                });

        startDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        String start = startDatePicker.getHeaderText();
                        binding.editTextStartDate.setText(startDatePicker.getHeaderText());
                        setStartDate(start);
                    }
                });

        MaterialDatePicker.Builder endDateBuilder = MaterialDatePicker.Builder.datePicker();
        endDateBuilder.setTitleText("END DATE");
        final MaterialDatePicker endDatePicker = endDateBuilder.build();
        binding.editTextEndDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endDatePicker.show(getChildFragmentManager(), "END_DATE_PICKER");
                    }
                });

        endDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        String end = endDatePicker.getHeaderText();
                        binding.editTextEndDate.setText(endDatePicker.getHeaderText());
                        setEndDate(end);
                    }
                });

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

        binding.textViewCancelNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelNewTrip();
            }
        });

        binding.textViewSaveNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String tripStartDate = startDateString;
               String tripEndDate = endDateString;
               String tripName = binding.editTextNewTripName.getText().toString();
               String resortName = binding.spinnerResortOptions.getSelectedItem().toString();

               if (tripName.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a trip name", Toast.LENGTH_SHORT).show();
                } else if (tripStartDate == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please select a start date", Toast.LENGTH_SHORT).show();
                } else if (tripEndDate == null) {
                   Toast.makeText(getActivity().getApplicationContext(), "Please select an end date", Toast.LENGTH_SHORT).show();
               } else if (resortName == "Select a Resort" || resortName.isEmpty()) {
                   Toast.makeText(getActivity().getApplicationContext(), "Please select a resort", Toast.LENGTH_SHORT).show();
               }
               else {
                    createTrip();
                    mListener.goToHomePage();
               }

            }
        });

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
        mListener = (NewTripFragment.NewTripFragmentListener) context;
    }

    interface NewTripFragmentListener {
        void cancelNewTrip();
        void goToHomePage();
    }
}