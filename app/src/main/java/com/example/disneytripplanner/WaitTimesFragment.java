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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.disneytripplanner.databinding.AttractionLineItemBinding;
import com.example.disneytripplanner.databinding.FragmentWaitTimesBinding;
import com.example.disneytripplanner.models.Attraction;
import com.example.disneytripplanner.models.Park;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WaitTimesFragment extends Fragment {
    private static final String TAG = "wait times fragment";
    WaitTimesFragment.WaitTimesFragmentListener mListener;
    FragmentWaitTimesBinding binding;

    private final OkHttpClient client = new OkHttpClient();

    private static final String ARG_PARAM_PARK = "paramPark";

    Park parkObject;
    String parkName;
    String parkQueryId;
    String queueTimesApiId;
    Park selectedPark;

   String[] parkOptions = {"Magic Kingdom", "Epcot", "Animal Kingdom", "Disney Hollywood Studios"};

    ArrayList<Attraction> attractions = new ArrayList<>();
    ArrayList<Attraction> allAttractions = new ArrayList<>();

    AttractionsListAdapter attractionsListAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

    public WaitTimesFragment() {
        // Required empty public constructor
    }

    public static WaitTimesFragment newInstance(Park park) {
        WaitTimesFragment fragment = new WaitTimesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_PARK, park);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parkObject = (Park) getArguments().getSerializable(ARG_PARAM_PARK);
            parkName = parkObject.getParkName();
            parkQueryId = parkObject.getQueryId();
            queueTimesApiId = parkObject.getQueueTimesApiId();
        }
    }

    void getParkInfo(String parkName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("resorts")
                .document("htf1iqKlYdTIHkEJVlkb")
                .collection("parks")
                .whereEqualTo("park_name", parkName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Log.d(TAG, "Park Info(): " + value.getMetadata());
                        for (QueryDocumentSnapshot document: value) {
                            Log.d(TAG, "Document: " + document.toString());
                            Park park = new Park();
                            park.setId(document.getId());
                            park.setParkName(document.getString("park_name"));
                            park.setQueryId(document.getString("park_id"));
                            park.setQueueTimesApiId(document.getString("queue_times_api_id"));
                            Log.d(TAG, "apiId: " + park.getQueueTimesApiId());
                            setSelectedPark(park);
                        }
                    }
                });
    }

    void setSelectedPark(Park mPark) {
        selectedPark = mPark;
        getAttractionsList(selectedPark.getQueueTimesApiId());
    }

    void getAttractionsList(String apiId) {
        Request request = new Request.Builder()
                .url("https://queue-times.com/en-US/parks/" + apiId + "/queue_times.json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d(TAG, "onResponse: " + body);
                    attractions.clear();

                    try {
                        JSONObject json = new JSONObject(body);
                        String landsStr = json.getString("lands");
                        JSONArray landsJsonArray = new JSONArray(landsStr);

                        for (int i = 0; i < landsJsonArray.length(); i++) {
                            JSONObject landJsonObject = landsJsonArray.getJSONObject(i);
                            String attractionsStr = landJsonObject.getString("rides");
                            JSONArray attractionsJsonArray = new JSONArray(attractionsStr);

                            for (int j = 0; j < attractionsJsonArray.length(); j++) {
                                Attraction attraction = new Attraction();
                                JSONObject attractionJSONObject = attractionsJsonArray.getJSONObject(j);

                                attraction.setLand(landJsonObject.getString("name"));
                                attraction.setName(attractionJSONObject.getString("name"));
                                attraction.setOpen(attractionJSONObject.getBoolean("is_open"));
                                attraction.setWaitTime(attractionJSONObject.getInt("wait_time"));

                                Log.d(TAG, "Attraction info ==================>>>>>> " + attraction.toString());

                                attractions.add(attraction);
                            }
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = binding.recyclerViewWaitTimes;
                                recyclerView.setHasFixedSize(false);
                                linearLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                attractionsListAdapter = new AttractionsListAdapter(attractions);
                                recyclerView.setAdapter(attractionsListAdapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "Error getting attractions data --- " + response.body());
                }
            }
        });
    }

    class AttractionsListAdapter extends RecyclerView.Adapter<AttractionsListAdapter.AttractionsViewHolder> {
        ArrayList<Attraction> mAttractions;

        public AttractionsListAdapter(ArrayList<Attraction> data) {
            this.mAttractions = data;
        }

        @NonNull
        @Override
        public AttractionsListAdapter.AttractionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            AttractionLineItemBinding binding = AttractionLineItemBinding.inflate(getLayoutInflater(), parent, false);
            return new AttractionsListAdapter.AttractionsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AttractionsListAdapter.AttractionsViewHolder holder, int position) {
            Attraction attraction = mAttractions.get(position);
            holder.setupUI(attraction);
        }

        @Override
        public int getItemCount() {
            return this.mAttractions.size();
        }

        public class AttractionsViewHolder extends RecyclerView.ViewHolder {
            AttractionLineItemBinding mBinding;
            Attraction mAttraction;
            int position;


            public AttractionsViewHolder(@NonNull AttractionLineItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Attraction attraction) {
                mAttraction = attraction;
                mBinding.textViewAttractionName.setText(mAttraction.getName());
                mBinding.textViewAttractionLand.setText(mAttraction.getLand());
                mBinding.textViewWaitTime.setText(String.valueOf(mAttraction.getWaitTime()));
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWaitTimesBinding.inflate(inflater, container, false);
        setupUI();
        return binding.getRoot();
    }

    void setupUI() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, parkOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerParkOptions.setAdapter(adapter);
        binding.spinnerParkOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                String parkName = parent.getItemAtPosition(position).toString();
                getParkInfo(parkName);
                //mListener.sendSelectedPark(selectedPark);
                //mListener.sendSelectedPark(parkName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (WaitTimesFragment.WaitTimesFragmentListener) context;
    }

    interface WaitTimesFragmentListener {
        void goBackToParkOptions();
        //void sendSelectedPark(Park parkName);
    }
}