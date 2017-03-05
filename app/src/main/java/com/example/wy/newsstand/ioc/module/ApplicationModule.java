package com.example.wy.newsstand.ioc.module;

import android.content.Context;

import com.example.wy.newsstand.ioc.scope.EveApp;
import com.example.wy.newsstand.ioc.scope.LifeCycle;
import com.example.wy.newsstand.WYNSApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wy on 2017/1/19.
 */
@Module
public class ApplicationModule {
    private WYNSApplication mApplication;

    public ApplicationModule(WYNSApplication application) {
        mApplication = application;
    }

    @Provides
    @EveApp
    @LifeCycle("Application")
    public Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }
}
