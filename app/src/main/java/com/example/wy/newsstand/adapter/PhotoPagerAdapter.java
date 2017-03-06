package com.example.wy.newsstand.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.wy.newsstand.mvp.v.PhotoDetailFragment;

import java.util.List;


public class PhotoPagerAdapter extends FragmentStatePagerAdapter {
    private List<PhotoDetailFragment> mFragmentList;

    public PhotoPagerAdapter(FragmentManager fm, List<PhotoDetailFragment> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }



    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}
