package com.example.disneytripplanner;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.disneytripplanner.databinding.FragmentMapBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class MapFragment extends Fragment {
    private static final String TAG = "map fragment";
    MapFragment.MapFragmentListener mListener;
    FragmentMapBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private GoogleMap mMap;

    String locationId;
    LatLng locationLatLng;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), BuildConfig.MAPS_API_KEY, Locale.US);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding = FragmentMapBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;

                // Add a marker in Sydney and move the camera
                LatLng magicKingdom = new LatLng(28.417665, -81.581238);
                mMap.addMarker(new MarkerOptions()
                        .position(magicKingdom)
                        .title("Magic Kingdom"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(magicKingdom));

                // Initialize the Autocomplete Support Fragments.
                AutocompleteSupportFragment autocompleteFragmentStart = (AutocompleteSupportFragment)
                        getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

                // Specify the types of place data to return.
                autocompleteFragmentStart.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

                autocompleteFragmentStart.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onError(@NonNull Status status) {
                        Log.d(TAG, "An error occurred: " + status);
                    }

                    @Override
                    public void onPlaceSelected(@NonNull Place place) {
                        Log.d(TAG, "Start Location: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                        locationId = place.getId();
                        locationLatLng = place.getLatLng();

                        /*
                        mMap.addMarker(new MarkerOptions()
                                .position(locationLatLng)
                                .title("Selected Location"));

                         */
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (MapFragment.MapFragmentListener) context;
    }

    interface MapFragmentListener {

    }
}