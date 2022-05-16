package com.example.disneytripplanner;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.disneytripplanner.databinding.FragmentHomeBinding;
import com.example.disneytripplanner.models.Park;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment {
    private static final String TAG = "home fragment";
    HomeFragmentListener mListener;
    FragmentHomeBinding binding;
    private FirebaseAuth mAuth;

    public HomeFragment() {
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
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setupUI();

        return binding.getRoot();
    }

    public void setupUI() {

        /*
        binding.imageViewProfileCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity().getApplicationContext(), binding.imageViewProfileCharacter);

                popupMenu.getMenuInflater().inflate(R.menu.profile_pic_dropdown_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Log.d(TAG, "onMenuItemClick: Item Clicked " + menuItem.getTitle());
                        if (menuItem.getTitle().toString().matches("Account")) {
                            mListener.showAccountSetting();
                        } else if (menuItem.getTitle().toString().matches("Sign Out")) {
                            FirebaseAuth.getInstance().signOut();
                            mListener.goToLogin();
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
        */

        binding.cardViewMyTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.viewMyTrips();
            }
        });

        binding.cardViewParkHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.viewParkHours();
            }
        });

        binding.cardViewWaitTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //mListener.viewWaitTimes();
                getMkParkObject();
            }
        });

        binding.cardViewMyFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToMyFavorites();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    void getMkParkObject() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("resorts")
                .document("htf1iqKlYdTIHkEJVlkb")
                .collection("parks")
                .document("UryZXEUlWCi7ax4XIkC0")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Park park = new Park();
                        park.setId(value.getId());
                        park.setParkName(value.getString("park_name"));
                        park.setQueryId(value.getString("park_id"));
                        park.setQueueTimesApiId(value.getString("queue_times_api_id"));
                        mListener.viewWaitTimes(park);
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (HomeFragmentListener) context;
    }


    interface HomeFragmentListener {
        void showAccountSetting();
        void goToLogin();
        void viewMyTrips();
        void goToMyFavorites();
        void viewParkHours();
        void viewWaitTimes(Park park);
    }
}