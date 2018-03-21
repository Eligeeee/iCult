package com.example.luisguilherme.icult;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class UploadActivity extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private SlidingTabStrip slidingTabStrip;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));
        slidingTabLayout.setViewPager(viewPager);




    }
}
