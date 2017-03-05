package com.example.wy.newsstand.ioc.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by wy on 17-3-5.
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface EveService {
}