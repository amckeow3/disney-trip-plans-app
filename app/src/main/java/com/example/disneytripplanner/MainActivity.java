package com.example.disneytripplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegistrationFragment.RegistrationFragmentListener, HomeFragment.HomeFragmentListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new HomeFragment(), "home-fragment")
                .commit();
    }

    @Override
    public void goToHomePage() {

    }

    @Override
    public void sendSelectedNavItem(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, fragment)
                .commit();
    }
}