package com.example.disneytripplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegistrationFragment.RegistrationFragmentListener, HomeFragment.HomeFragmentListener,
        AccountFragment.AccountFragmentListener, NewTripFragment.NewTripFragmentListener, MyTripsFragment.MyTripsFragmentListener, MapFragment.MapFragmentListener, FilterWaitTimesByParkFragment.FilterWaitTimesByParkFragmentListener, WaitTimesFragment.WaitTimesFragmentListener {

    private FirebaseAuth mAuth;
    private DrawerLayout mDrawer;
    private Menu topAppBar;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    //#3498DB
    // https://api.themeparks.wiki/v1/destinations --> Gets all destinations
    // https://api.themeparks.wiki/v1/entity/e957da41-3552-4cf6-b636-5babc5cbc4e5 --> Example to get wdw data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = findViewById(R.id.drawer_layout);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = WaitTimesFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = MyTripsFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }



    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
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
    public void selectWaitTimesByPark() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new FilterWaitTimesByParkFragment(), "wait-times-by-resort")
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