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

import com.example.disneytripplanner.databinding.FragmentFilterWaitTimesByParkBinding;
import com.example.disneytripplanner.databinding.ParkLineItemBinding;
import com.example.disneytripplanner.models.Park;
import com.example.disneytripplanner.models.Resort;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FilterWaitTimesByParkFragment extends Fragment {
    private static final String TAG = "get wait by park frag";
    FilterWaitTimesByParkFragmentListener mListener;
    FragmentFilterWaitTimesByParkBinding binding;
    ParksListAdapter parksListAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    ArrayList<Park> parks = new ArrayList<>();

    public FilterWaitTimesByParkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getParkOptions();
    }

    void getParkOptions() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("resorts")
                .document("htf1iqKlYdTIHkEJVlkb")
                .collection("parks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        parks.clear();
                        for (QueryDocumentSnapshot document: value) {
                            Park park = new Park();
                            park.setId(document.getId());
                            park.setParkName(document.getString("park_name"));
                            park.setQueryId(document.getString("park_id"));
                            park.setQueueTimesApiId(document.getString("queue_times_api_id"));
                            parks.add(park);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = binding.recyclerViewParks;
                                recyclerView.setHasFixedSize(false);
                                linearLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                parksListAdapter = new ParksListAdapter(parks);
                                recyclerView.setAdapter(parksListAdapter);
                            }
                        });
                    }
                });
    }

    class ParksListAdapter extends RecyclerView.Adapter<ParksListAdapter.ParksViewHolder> {
        ArrayList<Park> mParks;

        public ParksListAdapter(ArrayList<Park> data) {
            this.mParks = data;
        }

        @NonNull
        @Override
        public ParksListAdapter.ParksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ParkLineItemBinding binding = ParkLineItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ParksListAdapter.ParksViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ParksListAdapter.ParksViewHolder holder, int position) {
            Park park = mParks.get(position);
            holder.setupUI(park);
        }

        @Override
        public int getItemCount() {
            return this.mParks.size();
        }

        public class ParksViewHolder extends RecyclerView.ViewHolder {
            ParkLineItemBinding mBinding;
            Park mPark;
            int position;


            public ParksViewHolder(@NonNull ParkLineItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Park park) {
                mPark = park;
                mBinding.textViewParkName.setText(mPark.getParkName());

                String parkName = mPark.getParkName();

                /*
                if (parkName.matches("Epcot")) {
                    mBinding.imageViewParkIcon.setImageResource(R.drawable.epcot_color);
                } else if (parkName.matches("Animal Kingdom")) {
                    mBinding.imageViewParkIcon.setImageResource(R.drawable.animal_kingdom_tree_color);
                } else if (parkName.matches("Disney Hollywood Studios")) {
                    mBinding.imageViewParkIcon.setImageResource(R.drawable.tot_color);
                } else {
                    mBinding.imageViewParkIcon.setImageResource(R.drawable.mk_castle_color);
                }
                 */

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Park selectedPark = mPark;
                        Log.d(TAG, "onClick: selected park --- " + selectedPark.getParkName());
                        mListener.sendSelectedPark(selectedPark);
                    }
                });
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterWaitTimesByParkBinding.inflate(inflater, container, false);

        getParkOptions();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (FilterWaitTimesByParkFragmentListener) context;
    }

    interface FilterWaitTimesByParkFragmentListener {
        void sendSelectedPark(Park park);
        void goBackToResortOptions();
    }
}