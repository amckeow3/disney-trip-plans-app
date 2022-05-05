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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.disneytripplanner.databinding.FragmentFilterWaitTimesByResortBinding;
import com.example.disneytripplanner.databinding.ResortLineItemBinding;
import com.example.disneytripplanner.databinding.TripLineItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FilterWaitTimesByResortFragment extends Fragment {
    private static final String TAG = "filter wait by resort";
    FilterWaitTimesByResortFragment.FilterWaitTimesByResortFragmentListener mListener;
    FragmentFilterWaitTimesByResortBinding binding;
    ArrayList<Resort> resorts = new ArrayList<>();
    ArrayList<Park> parks = new ArrayList<>();
    private FirebaseAuth mAuth;
    ResortsListAdapter resortsListAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

    public FilterWaitTimesByResortFragment() {
        // Required empty public constructor
    }

    public void getResorts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("resorts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        resorts.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Resort resort = new Resort();
                            String resortId = document.getId();
                            resort.setId(resortId);
                            resort.setResortName(document.getString("resort_name"));
                            resort.setQueryId(document.getString("resort_id"));
                            resort.setSlug(document.getString("slug"));
                            resorts.add(resort);
                        }

                        Log.d(TAG, "Resorts: " + resorts);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = binding.recyclerViewResorts;
                                recyclerView.setHasFixedSize(true);
                                linearLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                resortsListAdapter = new ResortsListAdapter(resorts);
                                recyclerView.setAdapter(resortsListAdapter);
                            }
                        });
                    }
                });

    }




    class ResortsListAdapter extends RecyclerView.Adapter<ResortsListAdapter.ResortsViewHolder> {
        ArrayList<Resort> mResorts;

        public ResortsListAdapter(ArrayList<Resort> data) {
            this.mResorts = data;
        }

        @NonNull
        @Override
        public ResortsListAdapter.ResortsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ResortLineItemBinding binding = ResortLineItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ResortsListAdapter.ResortsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ResortsListAdapter.ResortsViewHolder holder, int position) {
            Resort resort = mResorts.get(position);
            holder.setupUI(resort);
        }

        @Override
        public int getItemCount() {
            return this.mResorts.size();
        }

        public class ResortsViewHolder extends RecyclerView.ViewHolder {
            ResortLineItemBinding mBinding;
            Resort mResort;
            int position;


            public ResortsViewHolder(@NonNull ResortLineItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Resort resort) {
                mResort = resort;
                mBinding.textViewResort.setText(mResort.resortName);

                String resortName = mResort.getResortName();

                if (resortName.matches("Shanghai Disney Resort")) {
                    mBinding.imageViewResortVisual.setImageResource(R.drawable.shanghai_castle);
                } else if (resortName.matches("Disneyland Paris")){
                    mBinding.imageViewResortVisual.setImageResource(R.drawable.paris_castle);
                } else if (resortName.matches("Disneyland California")) {
                    mBinding.imageViewResortVisual.setImageResource(R.drawable.disneyland_icon);
                } else if (resortName.matches("Walt Disney World Resort")) {
                    mBinding.imageViewResortVisual.setImageResource(R.drawable.wdw_icon);
                } else if (resortName.matches("Hong Kong Disneyland Resort")){
                    mBinding.imageViewResortVisual.setImageResource(R.drawable.hk_disneyland);
                } else if (resortName.matches("Tokyo Disney Resort")) {
                    mBinding.imageViewResortVisual.setImageResource(R.drawable.tokyo_disney);
                } else {
                    mBinding.imageViewResortVisual.setImageResource(R.drawable.wdw_icon);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Resort selectedResort = mResort;
                        Log.d(TAG, "onClick: Selected Forum Id " + selectedResort.resortName);
                        mListener.sendSelectedResort(selectedResort);
                    }
                });
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getResorts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterWaitTimesByResortBinding.inflate(inflater, container, false);

        getResorts();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (FilterWaitTimesByResortFragment.FilterWaitTimesByResortFragmentListener) context;
    }

    interface FilterWaitTimesByResortFragmentListener {
        void sendSelectedResort(Resort resort);
    }
}