package com.example.disneytripplanner;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.disneytripplanner.databinding.FragmentMyTripsBinding;
import com.example.disneytripplanner.databinding.TripLineItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyTripsFragment extends Fragment {
    private static final String TAG = "my trips fragment";
    MyTripsFragment.MyTripsFragmentListener mListener;
    FragmentMyTripsBinding binding;
    private FirebaseAuth mAuth;
    ArrayList<Trip> trips = new ArrayList<>();
    TripsListAdapter tripsListAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

    public MyTripsFragment() {
        // Required empty public constructor
    }

    private void getTrips() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String userUid = user.getUid();

        db.collection("users")
                .document(userUid)
                .collection("trips")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        trips.clear();
                        for (QueryDocumentSnapshot document: value) {
                            Log.d(TAG, "trip data ---->" + document.getData());
                            Trip trip = new Trip();
                            trip.setTripName(document.getString("tripName"));
                            trip.setResort(document.getString("resort"));
                            trip.setStartDate(document.getString("startDate"));
                            trip.setEndDate(document.getString("endDate"));
                            trips.add(trip);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = binding.recyclerViewTrips;
                                recyclerView.setHasFixedSize(true);
                                linearLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                tripsListAdapter = new TripsListAdapter(trips);
                                recyclerView.setAdapter(tripsListAdapter);
                            }
                        });
                    }
                });
    }

    class TripsListAdapter extends RecyclerView.Adapter<TripsListAdapter.TripsViewHolder> {
        ArrayList<Trip> mTrips;

        public TripsListAdapter(ArrayList<Trip> data) {
            this.mTrips = data;
        }

        @NonNull
        @Override
        public TripsListAdapter.TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TripLineItemBinding binding = TripLineItemBinding.inflate(getLayoutInflater(), parent, false);
            return new TripsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull TripsListAdapter.TripsViewHolder holder, int position) {
            Trip trip = mTrips.get(position);
            holder.setupUI(trip);
        }

        @Override
        public int getItemCount() {
            return this.mTrips.size();
        }

        public class TripsViewHolder extends RecyclerView.ViewHolder {
            TripLineItemBinding mBinding;
            Trip mTrip;
            int position;


            public TripsViewHolder(@NonNull TripLineItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Trip trip) {
                mTrip = trip;
                mBinding.textViewTripName.setText(mTrip.tripName);
                mBinding.textViewTripDates.setText(mTrip.startDate + " - " + mTrip.endDate);
                mBinding.textViewTripLocation.setText(mTrip.resort);
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String userId = user.getUid();
                Log.d(TAG, "user id: " + userId);

                String resortName = mTrip.getResort();

                if (resortName.matches("Shanghai Disney Resort")) {
                    mBinding.imageViewResortImage.setImageResource(R.drawable.shanghai_castle);
                } else if (resortName.matches("Disneyland Paris")){
                    mBinding.imageViewResortImage.setImageResource(R.drawable.paris_castle);
                } else if (resortName.matches("Disneyland California")) {
                    mBinding.imageViewResortImage.setImageResource(R.drawable.disneyland_icon);
                } else if (resortName.matches("Walt Disney World Resort")) {
                    mBinding.imageViewResortImage.setImageResource(R.drawable.wdw_icon);
                } else {
                    mBinding.imageViewResortImage.setImageResource(R.drawable.wdw_icon);
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        getTrips();
    }

    public void setupUI() {
        binding.imageViewAddTripIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.createNewTrip();
            }
        });

        binding.imageViewBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToHomePage();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyTripsBinding.inflate(inflater, container, false);

        setupUI();
        getTrips();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (MyTripsFragment.MyTripsFragmentListener) context;
    }

    interface MyTripsFragmentListener {
        void createNewTrip();
        void goToHomePage();
    }
}