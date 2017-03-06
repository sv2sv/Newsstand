package com.example.wy.newsstand;

import android.app.Application;
import android.widget.Toast;


import com.example.wy.newsstand.ioc.component.ApplicationComponent;
import com.example.wy.newsstand.ioc.component.DaggerApplicationComponent;
import com.example.wy.newsstand.ioc.module.ApplicationModule;

/**
 * Created by wy on 17-1-5.
 */

public class WYNSApplication extends Application{

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        WYNSDepend.setup(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
