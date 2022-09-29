package com.example.disneytripplanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.example.disneytripplanner.auth.LoginFragment;
import com.example.disneytripplanner.auth.RegistrationFragment;
import com.example.disneytripplanner.databinding.ActivityMainBinding;
import com.example.disneytripplanner.models.Park;
import com.example.disneytripplanner.models.Trip;
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
        WaitTimesFragment.WaitTimesFragmentListener, FavoritesFragment.FavoritesFragmentListener, ParkHoursFragment.ParkHoursFragmentListener, ViewTripFragment.ViewTripFragmentListener {

    private FirebaseAuth mAuth;
    public static ViewPager2 viewPager;
    private static final int NUM_PAGES = 4;
    TabLayout tabLayout;
    private FragmentStateAdapter pagerAdapter;
    NavigationView bottomNav;
    ActivityMainBinding binding;

    String[] tabTitles = new String[]{"Home", "Account", "My Trips", "Wait Times"};
    //#3498DB
    // https://api.themeparks.wiki/v1/destinations --> Gets all destinations
    // https://api.themeparks.wiki/v1/entity/e957da41-3552-4cf6-b636-5babc5cbc4e5 --> Example to get wdw data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.navigationView);

        findViewById(R.id.buttonHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomNavigation();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rootView, new HomeFragment(), "home-fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        /*
        findViewById(R.id.buttonMyTrips).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rootView, new MyTripsFragment(), "my-trips-fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
         */

        findViewById(R.id.buttonMaps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomNavigation();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rootView, new MapFragment(), "maps-fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            hideBottomNavigation();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment(), "login-fragment")
                    .commit();
        } else {
            showBottomNavigation();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new HomeFragment(), "home-fragment")
                    .commit();
        }
    }

    @Override
    public void goToHomePage() {
        showBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new HomeFragment(), "home-fragment")
                .commit();
    }

    @Override
    public void viewSelectedTrip(Trip trip) {
        showBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ViewTripFragment.newInstance(trip), "home-fragment")
                .commit();
    }

    @Override
    public void backToLogin() {
        hideBottomNavigation();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToRegistration() {
        hideBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new RegistrationFragment(), "registration-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showAccountSetting() {
        showBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new AccountFragment(), "account-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToLogin() {
        hideBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void createNewTrip() {
        showBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new NewTripFragment(), "new-trip-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewMyTrips() {
        showBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new MyTripsFragment(), "my-trips-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToMyFavorites() {
        showBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new FavoritesFragment(), "favorites-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewParkHours() {
        showBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new ParkHoursFragment(), "park-hours-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelNewTrip() {
        showBottomNavigation();
        getSupportFragmentManager()
                .popBackStack();
    }

    @Override
    public void viewWaitTimes(Park park) {
        showBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, WaitTimesFragment.newInstance(park), "wait-times-fragment")
                .addToBackStack(null)
                .commit();
    }

    public void hideBottomNavigation() {
        if (bottomNav.getVisibility() == View.VISIBLE) {
            bottomNav.setVisibility(View.GONE);
        }
    }

    public void showBottomNavigation() {
        if (bottomNav.getVisibility() == View.GONE) {
            bottomNav.setVisibility(View.VISIBLE);
        }
    }
}