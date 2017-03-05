package com.example.wy.newsstand.ioc.component;

import android.content.Context;

import com.example.wy.newsstand.ioc.module.ServiceModule;
import com.example.wy.newsstand.ioc.scope.EveService;
import com.example.wy.newsstand.ioc.scope.LifeCycle;

import dagger.Component;

/**
 * Created by wy on 2017/1/19.
 */
@EveService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    @LifeCycle("Service")
    Context getServiceContext();

}
