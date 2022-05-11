package com.example.disneytripplanner;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.disneytripplanner.databinding.FragmentFavoritesBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FavoritesFragment extends Fragment {
    FavoritesFragment.FavoritesFragmentListener mListener;
    FragmentFavoritesBinding binding;

    private ViewPager2 viewPager;
    private static final int NUM_PAGES = 2;
    private TabLayout tabLayout;
    private FragmentStateAdapter pagerAdapter;
    String[] tabTitles = new String[]{"Attractions", "Entertainment"};

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 1: {
                    return new MyTripsFragment();
                }
                default: {
                    return new AccountFragment();
                }
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);

        viewPager = binding.viewPagerFavorites;
        tabLayout = binding.tabLayoutFavorites;
        pagerAdapter = new MyPagerAdapter(getActivity());
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(tabTitles[position])).attach();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (FavoritesFragment.FavoritesFragmentListener) context;
    }

    interface FavoritesFragmentListener {

    }
}