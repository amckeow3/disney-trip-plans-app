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

import com.example.disneytripplanner.databinding.AttractionLineItemBinding;
import com.example.disneytripplanner.databinding.FragmentWaitTimesBinding;
import com.example.disneytripplanner.databinding.ParkLineItemBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        getPostsList(queueTimesApiId);

    }

    void getPostsList(String apiId) {
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

                        if (landsJsonArray.length() == 0) {
                            String ridesStr = json.getString("rides");
                            JSONArray ridesJsonArray = new JSONArray(ridesStr);

                            for (int i = 0; i < ridesJsonArray.length(); i++) {
                                Attraction attraction = new Attraction();

                                JSONObject ridesJSONObject = ridesJsonArray.getJSONObject(i);

                                attraction.setLand(null);
                                attraction.setName(ridesJSONObject.getString("name"));
                                attraction.setOpen(ridesJSONObject.getBoolean("is_open"));
                                attraction.setWaitTime(ridesJSONObject.getInt("wait_time"));

                                Log.d(TAG, "Attraction info ==================>>>>>> " + attraction.toString());

                                attractions.add(attraction);
                            }
                        } else {
                            for (int i = 0; i < landsJsonArray.length(); i++) {
                                JSONObject landJsonObject = landsJsonArray.getJSONObject(i);
                                //Attraction attraction = new Attraction();

                                //attraction.setLand(landJsonObject.getString("name"));
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
                mBinding.textViewAttractionName.setText(mAttraction.name);
                mBinding.textViewAttractionLand.setText(mAttraction.land);
                mBinding.textViewWaitTime.setText(String.valueOf(mAttraction.waitTime));
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWaitTimesBinding.inflate(inflater, container, false);

        getPostsList(queueTimesApiId);

        setupUI();

        return binding.getRoot();
    }

    void setupUI() {
        binding.buttonBackToParkOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goBackToParkOptions();
            }
        });

        binding.textViewBackToParkOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goBackToParkOptions();
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
        mListener = (WaitTimesFragment.WaitTimesFragmentListener) context;
    }

    interface WaitTimesFragmentListener {
        void goBackToParkOptions();
    }
}