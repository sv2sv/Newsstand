package com.example.wy.newsstand.ioc.component;

import android.content.Context;

import com.example.wy.newsstand.ioc.module.ApplicationModule;
import com.example.wy.newsstand.ioc.scope.EveApp;
import com.example.wy.newsstand.ioc.scope.LifeCycle;

import dagger.Component;

/**
 * Created by wy on 2017/1/19.
 */
@EveApp
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @LifeCycle("Application")
    Context getApplication();
}