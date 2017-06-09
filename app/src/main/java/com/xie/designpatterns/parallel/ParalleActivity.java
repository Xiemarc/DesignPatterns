package com.xie.designpatterns.parallel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xie.designpatterns.R;

public class ParalleActivity extends AppCompatActivity {

    private ViewPager vp;
    private int[] layoutIds = {
            R.layout.fragment_paralle_welcome1,
            R.layout.fragment_paralle_welcome2,
            R.layout.fragment_paralle_welcome3
    };

    private WelcompagerTransformer transformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paralle);
        vp = (ViewPager) findViewById(R.id.vp);
        WelcomeAdapgter adapgter = new WelcomeAdapgter(getSupportFragmentManager());
        vp.setOffscreenPageLimit(layoutIds.length);
        vp.setAdapter(adapgter);
        transformer = new WelcompagerTransformer();
        vp.setPageTransformer(true, transformer);
        vp.setOnPageChangeListener(transformer);
    }

    class WelcomeAdapgter extends FragmentPagerAdapter {


        public WelcomeAdapgter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ParalleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("layoutId", layoutIds[position]);
            bundle.putInt("pageIndex", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return layoutIds.length;
        }
    }
}
