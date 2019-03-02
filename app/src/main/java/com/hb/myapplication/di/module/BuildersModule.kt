package com.hb.myapplication.di.module

import com.hb.myapplication.di.module.sub.SystemModule
import com.hb.myapplication.di.scope.CustomScope
import com.hb.myapplication.ui.detail.DetailActivity
import com.hb.myapplication.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class BuildersModule {

    @CustomScope
    @ContributesAndroidInjector(modules = [SystemModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @CustomScope
    @ContributesAndroidInjector(modules = [SystemModule::class])
    abstract fun contributeDetailActivity(): DetailActivity



}