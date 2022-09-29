package com.example.disneytripplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.example.disneytripplanner.auth.LoginFragment;
import com.example.disneytripplanner.auth.RegistrationFragment;
import com.example.disneytripplanner.models.Park;
import com.example.disneytripplanner.models.Trip;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegistrationFragment.RegistrationFragmentListener, HomeFragment.HomeFragmentListener,
        AccountFragment.AccountFragmentListener, NewTripFragment.NewTripFragmentListener, MyTripsFragment.MyTripsFragmentListener, MapFragment.MapFragmentListener,
        WaitTimesFragment.WaitTimesFragmentListener, FavoritesFragment.FavoritesFragmentListener, ParkHoursFragment.ParkHoursFragmentListener, ViewTripFragment.ViewTripFragmentListener {

    private FirebaseAuth mAuth;
    public static ViewPager2 viewPager;
    private static final int NUM_PAGES = 4;
    TabLayout tabLayout;
    private FragmentStateAdapter pagerAdapter;
    NavigationView topNav;
    NavigationView bottomNav;

    String[] tabTitles = new String[]{"Home", "Account", "My Trips", "Wait Times"};
    //#3498DB
    // https://api.themeparks.wiki/v1/destinations --> Gets all destinations
    // https://api.themeparks.wiki/v1/entity/e957da41-3552-4cf6-b636-5babc5cbc4e5 --> Example to get wdw data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topNav = findViewById(R.id.topNavBar);
        bottomNav = findViewById(R.id.navigationView);

        findViewById(R.id.buttonHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomNavigation();
                showTopNavigation();
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
                hideTopNavigation();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rootView, new MapFragment(), "maps-fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            hideTopNavigation();
            hideBottomNavigation();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment(), "login-fragment")
                    .commit();
        } else {
            showTopNavigation();
            showBottomNavigation();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new HomeFragment(), "home-fragment")
                    .commit();
        }
    }

    @Override
    public void goToHomePage() {
        showBottomNavigation();
        showTopNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new HomeFragment(), "home-fragment")
                .commit();
    }

    @Override
    public void viewSelectedTrip(Trip trip) {
        showBottomNavigation();
        showTopNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ViewTripFragment.newInstance(trip), "home-fragment")
                .commit();
    }

    @Override
    public void backToLogin() {
        hideBottomNavigation();
        hideTopNavigation();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToRegistration() {
        hideBottomNavigation();
        hideTopNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new RegistrationFragment(), "registration-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showAccountSetting() {
        showBottomNavigation();
        showTopNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new AccountFragment(), "account-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToLogin() {
        hideBottomNavigation();
        hideTopNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void createNewTrip() {
        showBottomNavigation();
        hideTopNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new NewTripFragment(), "new-trip-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewMyTrips() {
        showBottomNavigation();
        hideTopNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new MyTripsFragment(), "my-trips-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToMyFavorites() {
        showBottomNavigation();
        showTopNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new FavoritesFragment(), "favorites-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewParkHours() {
        showBottomNavigation();
        hideTopNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new ParkHoursFragment(), "park-hours-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelNewTrip() {
        showBottomNavigation();
        hideTopNavigation();
        getSupportFragmentManager()
                .popBackStack();
    }

    @Override
    public void viewWaitTimes(Park park) {
        showBottomNavigation();
        hideTopNavigation();
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
    public void hideTopNavigation() {
        if (topNav.getVisibility() == View.VISIBLE) {
            topNav.setVisibility(View.GONE);
        }
    }

    public void showTopNavigation() {
        if (topNav.getVisibility() == View.GONE) {
            topNav.setVisibility(View.VISIBLE);
        }
    }

}