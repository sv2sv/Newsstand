package com.example.wy.newsstand.ioc.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import com.example.wy.newsstand.ioc.scope.EveFragment;
import com.example.wy.newsstand.ioc.scope.LifeCycle;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wy on 2017/1/19.
 */
@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @EveFragment
    @LifeCycle("Activity")
    public Context provideActivityContext() {
        return mFragment.getActivity();
    }

    @Provides
    @EveFragment
    public Activity provideActivity() {
        return mFragment.getActivity();
    }

    @Provides
    @EveFragment
    public Fragment provideFragment() {
        return mFragment;
    }
}
