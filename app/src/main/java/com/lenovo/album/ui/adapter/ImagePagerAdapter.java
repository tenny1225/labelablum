package com.lenovo.album.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by noahkong on 17-6-9.
 */

public class ImagePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public ImagePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }



    @Override
    public int getCount() {
        if(fragments!=null){
           return fragments.size();
        }
        return 0;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position % fragments.size());

        return fragment;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
