package com.example.wy.newsstand.ioc.module;

import android.app.Activity;
import android.content.Context;

import com.example.wy.newsstand.ioc.scope.EveActivity;
import com.example.wy.newsstand.ioc.scope.LifeCycle;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wy on 2017/1/19.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @EveActivity
    @LifeCycle("Activity")
    public Context ProvideActivityContext() {
        return mActivity;
    }

    @Provides
    @EveActivity
    public Activity ProvideActivity() {
        return mActivity;
    }
}
