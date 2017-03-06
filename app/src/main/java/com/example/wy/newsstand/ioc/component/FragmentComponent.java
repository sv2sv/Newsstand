package com.example.wy.newsstand.ioc.component;

import android.app.Activity;
import android.content.Context;

import com.example.wy.newsstand.ioc.module.FragmentModule;
import com.example.wy.newsstand.ioc.scope.EveFragment;
import com.example.wy.newsstand.ioc.scope.LifeCycle;
import com.example.wy.newsstand.mvp.v.NewsListFragment;

import dagger.Component;

/**
 * Created by wy on 2017/1/19.
 */
@EveFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @LifeCycle("Activity")
    Context getActivityContext();

    Activity getActivity();

    void inject(NewsListFragment newsListFragment);
}
