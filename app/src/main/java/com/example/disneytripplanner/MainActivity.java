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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegistrationFragment.RegistrationFragmentListener, HomeFragment.HomeFragmentListener,
        AccountFragment.AccountFragmentListener, NewTripFragment.NewTripFragmentListener, MyTripsFragment.MyTripsFragmentListener, MapFragment.MapFragmentListener, FilterWaitTimesByParkFragment.FilterWaitTimesByParkFragmentListener,
        WaitTimesFragment.WaitTimesFragmentListener, FavoritesFragment.FavoritesFragmentListener, ParkHoursFragment.ParkHoursFragmentListener {

    private FirebaseAuth mAuth;
    public static ViewPager2 viewPager;
    private static final int NUM_PAGES = 4;
    TabLayout tabLayout;
    private FragmentStateAdapter pagerAdapter;
    String[] tabTitles = new String[]{"Home", "Account", "My Trips", "Wait Times"};
    //#3498DB
    // https://api.themeparks.wiki/v1/destinations --> Gets all destinations
    // https://api.themeparks.wiki/v1/entity/e957da41-3552-4cf6-b636-5babc5cbc4e5 --> Example to get wdw data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rootView, new HomeFragment(), "home-fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        findViewById(R.id.buttonMyTrips).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rootView, new MyTripsFragment(), "my-trips-fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        findViewById(R.id.buttonMaps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rootView, new MapFragment(), "maps-fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment(), "login-fragment")
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new HomeFragment(), "home-fragment")
                    .commit();
        }
    }

    @Override
    public void goToHomePage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new HomeFragment(), "home-fragment")
                .commit();
    }

    @Override
    public void viewSelectedTrip(Trip trip) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ViewTripFragment.newInstance(trip), "home-fragment")
                .commit();
    }

    @Override
    public void backToLogin() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToRegistration() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new RegistrationFragment(), "registration-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showAccountSetting() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new AccountFragment(), "account-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void createNewTrip() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new NewTripFragment(), "new-trip-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewMyTrips() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new MyTripsFragment(), "my-trips-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewWaitTimes() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new FilterWaitTimesByParkFragment(), "wait-times-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToMyFavorites() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new FavoritesFragment(), "favorites-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewParkHours() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new ParkHoursFragment(), "park-hours-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelNewTrip() {
        getSupportFragmentManager()
                .popBackStack();
    }

    @Override
    public void sendSelectedPark(Park park) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, WaitTimesFragment.newInstance(park), "wait-times-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBackToResortOptions() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goBackToParkOptions() {
        getSupportFragmentManager().popBackStack();
    }
}