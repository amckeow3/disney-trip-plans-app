package com.example.disneytripplanner;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.disneytripplanner.models.Trip;

public class ViewTripFragment extends Fragment {
    ViewTripFragmentListener mListener;
    private static final String ARG_PARAM_TRIP = "paramTrip";
    Trip tripObject;
    String startDate;
    String endDate;
    String description;

    public ViewTripFragment() {
        // Required empty public constructor
    }

    public static ViewTripFragment newInstance(Trip trip) {
        ViewTripFragment fragment = new ViewTripFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TRIP, trip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tripObject = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);
            startDate = tripObject.getStartDate();
            endDate = tripObject.getEndDate();
            description = tripObject.getDescription();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ViewTripFragmentListener) context;
    }

    interface ViewTripFragmentListener {

    }
}