package com.example.wy.newsstand.ioc.module;

import android.app.Service;
import android.content.Context;
import com.example.wy.newsstand.ioc.scope.EveService;
import com.example.wy.newsstand.ioc.scope.LifeCycle;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wy on 2017/1/19.
 */
@Module
public class ServiceModule {
    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @EveService
    @LifeCycle("Service")
    public Context ProvideServiceContext() {
        return mService;
    }
}
