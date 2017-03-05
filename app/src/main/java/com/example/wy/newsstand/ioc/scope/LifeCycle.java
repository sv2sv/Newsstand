package com.example.wy.newsstand.ioc.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by wy on 17-3-5.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface LifeCycle {
    String value() default "Application";
}
