package com.example.disneytripplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.disneytripplanner.auth.LoginFragment;
import com.example.disneytripplanner.auth.RegistrationFragment;
import com.example.disneytripplanner.databinding.ActivityMainBinding;
import com.example.disneytripplanner.models.Park;
import com.example.disneytripplanner.models.Trip;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegistrationFragment.RegistrationFragmentListener, HomeFragment.HomeFragmentListener,
        AccountFragment.AccountFragmentListener, NewTripFragment.NewTripFragmentListener, MyTripsFragment.MyTripsFragmentListener, MapFragment.MapFragmentListener,
        WaitTimesFragment.WaitTimesFragmentListener, FavoritesFragment.FavoritesFragmentListener, ParkHoursFragment.ParkHoursFragmentListener, ViewTripFragment.ViewTripFragmentListener,
        SelectTripDatesFragment.SelectTripDatesFragmentListener {

    private static final String TAG = "Main Activity";
    private FirebaseAuth mAuth;
    ActivityMainBinding binding;

    //#3498DB
    // https://api.themeparks.wiki/v1/destinations --> Gets all destinations
    // https://api.themeparks.wiki/v1/entity/e957da41-3552-4cf6-b636-5babc5cbc4e5 --> Example to get wdw data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.topAppBar);

        binding.bottomNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.homeButton:
                        goToHomePage();
                        break;
                    case R.id.mapButton:
                        openMapView();
                        break;
                    case R.id.optionsButton:
                        createNewTrip();
                        break;
                    case R.id.searchButton:
                        Log.d(TAG, "Implement Search Fragment");
                        break;
                }

                return true;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            hideTopAppBar();
            goToLogin();
        } else {
            goToHomePage();
        }
    }

    @Override
    public void goToHomePage() {
        hideTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new HomeFragment(), "home-fragment")
                .commit();
    }

    @Override
    public void selectTripDates() {
        hideTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SelectTripDatesFragment(), "select-trip-dates-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewSelectedTrip(Trip trip) {
        showTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ViewTripFragment.newInstance(trip), "home-fragment")
                .commit();
    }

    @Override
    public void backToLogin() {
        hideTopAppBar();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToRegistration() {
        hideTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new RegistrationFragment(), "registration-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showAccountSetting() {
        showTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new AccountFragment(), "account-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToLogin() {
        hideTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void createNewTrip() {
        hideTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new NewTripFragment(), "new-trip-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewMyTrips() {
        showTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new MyTripsFragment(), "my-trips-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewParkHours() {
        showTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new ParkHoursFragment(), "park-hours-fragment")
                .addToBackStack(null)
                .commit();
    }

    public void openMapView() {
        showTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new MapFragment(), "map-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelNewTrip() {
        getSupportFragmentManager()
                .popBackStack();
    }

    @Override
    public void viewWaitTimes(Park park) {
        showTopAppBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, WaitTimesFragment.newInstance(park), "wait-times-fragment")
                .addToBackStack(null)
                .commit();
    }

    public void hideTopAppBar() {
        binding.topAppBar.setVisibility(View.GONE);
    }

    public void showTopAppBar() {
        binding.topAppBar.setVisibility(View.VISIBLE);
    }
}