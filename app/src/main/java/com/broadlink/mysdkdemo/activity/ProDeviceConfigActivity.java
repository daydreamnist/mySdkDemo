package com.broadlink.mysdkdemo.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.fragment.DeviceConfigStep1Fragment;
import com.broadlink.mysdkdemo.fragment.DeviceConfigStep2Fragment;

import java.util.ArrayList;
import java.util.List;

public class ProDeviceConfigActivity extends AppCompatActivity implements DeviceConfigStep1Fragment.OnFragmentInteractionListener, DeviceConfigStep2Fragment.OnFragmentInteractionListener, DeviceConfigStep1Fragment.OnButtonClickListener {

    private ViewPager mVp_deviceConfig;
    private List<Fragment> fragments;

    private DeviceConfigStep1Fragment fragmentStep1;

    private static int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_device_config);

        mVp_deviceConfig = findViewById(R.id.mVp_deviceConfig);
        fragments = new ArrayList<>();
        fragmentStep1 = DeviceConfigStep1Fragment.newInstance("","");
        fragments.add(fragmentStep1);
        fragmentStep1.setOnButtonClickListener( this);
        fragments.add(DeviceConfigStep2Fragment.newInstance("",""));
        FragmentManager fm = getSupportFragmentManager();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(fm,fragments);
        mVp_deviceConfig.setAdapter(myFragmentPagerAdapter);
        mVp_deviceConfig.setCurrentItem(currentIndex);
//        mVp_deviceConfig.setAdapter();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onButtonClick(View v) {
        mVp_deviceConfig.setCurrentItem(++currentIndex);

    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter{

        private FragmentManager fragmentManager;
        private List<Fragment> fragments;

        public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            this.fragmentManager = fm;
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}


