package com.example.disneytripplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegistrationFragment.RegistrationFragmentListener, HomeFragment.HomeFragmentListener,
        AccountFragment.AccountFragmentListener, NewTripFragment.NewTripFragmentListener, MyTripsFragment.MyTripsFragmentListener, MapFragment.MapFragmentListener,
        FilterWaitTimesByResortFragment.FilterWaitTimesByResortFragmentListener, FilterWaitTimesByParkFragment.FilterWaitTimesByParkFragmentListener, WaitTimesFragment.WaitTimesFragmentListener {

    private FirebaseAuth mAuth;

    //#3498DB
    // https://api.themeparks.wiki/v1/destinations --> Gets all destinations
    // https://api.themeparks.wiki/v1/entity/e957da41-3552-4cf6-b636-5babc5cbc4e5 --> Example to get wdw data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment(), "login-fragment")
                    .commit();
        } else {
            String name = user.getDisplayName();
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
    public void showMaps() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new MapFragment(), "maps-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void selectWaitTimeByResort() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new FilterWaitTimesByResortFragment(), "wait-times-by-resort")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelNewTrip() {
        getSupportFragmentManager()
                .popBackStack();
    }

    @Override
    public void sendSelectedResort(Resort resort) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, FilterWaitTimesByParkFragment.newInstance(resort), "wait-times-by-park")
                .addToBackStack(null)
                .commit();
    }
}