package com.martinez.steven.practica_2;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventosFragment extends Fragment {

    private PagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FragmentManager fm;
    private FragmentTransaction ft;


    public EventosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eventos, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((PrincipalActivity) getActivity()).setActionBarTitle(toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = view.findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        //Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //mToolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    TodosFragment tab0 = new TodosFragment();
                    return tab0;
                case 1:
                    FutbolkFragment tab1 = new FutbolkFragment();
                    return tab1;
                case 2:
                    BasquetbolFragment tab2 = new BasquetbolFragment();
                    return tab2;
                case 3:
                    FSalaFragment tab3 = new FSalaFragment();
                    return tab3;
                case 4:
                    VoleibolFragment tab4 = new VoleibolFragment();
                    return tab4;
                default:
                    return null;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Todos";
                case 1:
                    return "Fútbol";
                case 2:
                    return "Básquetbol";
                case 3:
                    return "F. Sala";
                case 4:
                    return "Voleibol";
            }
            return super.getPageTitle(position);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }
    }


}
