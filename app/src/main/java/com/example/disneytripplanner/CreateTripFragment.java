package com.example.disneytripplanner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.disneytripplanner.databinding.FragmentCreateTripBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateTripFragment extends Fragment {
    private static final String TAG = "create trip fragment";
    CreateTripFragment.CreateTripFragmentListener mListener;
    FragmentCreateTripBinding binding;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Resort> resorts = new ArrayList<>();
    ArrayList<String> resortNames = new ArrayList<>();

    DatePickerDialog datePicker;

    public CreateTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateTripBinding.inflate(inflater, container, false);

        getDisneyResorts();

        String tripName = binding.editTextTripName.getText().toString();

        resortNames.clear();
        String spinnerHint = "(Select a Resort)";
        resortNames.add(spinnerHint);
        for (int i = 0; i < resorts.size(); i++) {
            Resort resort = resorts.get(i);
            String name = resort.getResortName();
            resortNames.add(name);
        }
        Log.d(TAG, "Resort Options: " + resortNames);
        Spinner resortsSpinner = binding.resortSpinner;

        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, resortNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        resortsSpinner.setAdapter(arrayAdapter);

        binding.buttonSelectTripDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateString = String.format("%02d/%02d/%d", monthOfYear + 1, dayOfMonth, year);
                        binding.textViewTripDates.setText(dateString);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });


        return binding.getRoot();
    }

    public void getDisneyResorts() {
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
                    }
                });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateTripFragment.CreateTripFragmentListener) context;
    }

    interface CreateTripFragmentListener {

    }
}