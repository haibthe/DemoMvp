package com.hb.myapplication.di.scope;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by haibui on 2017-02-02.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomScope {

}
