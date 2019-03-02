package com.hb.myapplication.di.component

import android.content.Context
import com.hb.lib.data.IDataManager
import com.hb.lib.utils.RxBus
import com.hb.myapplication.app.App
import com.hb.myapplication.di.module.AppModule
import com.hb.myapplication.di.module.BuildersModule
import com.hb.myapplication.di.module.NetModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import retrofit2.Retrofit
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        BuildersModule::class,
        AppModule::class,
        NetModule::class
    ]
)
interface AppComponent {
    fun inject(app: App)

    fun bus(): RxBus

    fun dataManager(): IDataManager

    fun context(): Context

    fun retrofit(): Retrofit

}