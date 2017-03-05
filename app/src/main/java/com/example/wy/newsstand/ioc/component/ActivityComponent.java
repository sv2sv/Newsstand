package com.example.wy.newsstand.ioc.component;

import android.app.Activity;
import android.content.Context;

import com.example.wy.newsstand.ioc.module.ActivityModule;
import com.example.wy.newsstand.ioc.scope.EveActivity;
import com.example.wy.newsstand.ioc.scope.LifeCycle;

import dagger.Component;

/**
 * Created by wy on 2017/1/19.
 */
@EveActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @LifeCycle("Activity")
    Context getActivityContext();


    Activity getActivity();

/*    void inject(NewsActivity newsActivity);

    void inject(NewsDetailActivity newsDetailActivity);

    void inject(NewsChannelActivity newsChannelActivity);

    void inject(NewsPhotoDetailActivity newsPhotoDetailActivity);*/

    //void inject(PhotoActivity photoActivity);

    //void inject(PhotoDetailActivity photoDetailActivity);
}
