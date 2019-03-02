package com.hb.myapplication.app;

import com.hb.lib.app.HBMvpApp;
import com.hb.myapplication.BuildConfig;
import com.hb.myapplication.di.component.AppComponent;
import com.hb.myapplication.di.component.DaggerAppComponent;
import com.hb.myapplication.di.module.AppModule;
import com.hb.myapplication.utils.image.GlideImageHelper;
import com.hb.myapplication.utils.image.ImageHelper;
import timber.log.Timber;

public class App extends HBMvpApp {

    public static ImageHelper imageHelper;

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    protected void init() {

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        initAllComponent();

        imageHelper = new GlideImageHelper(getBaseContext());


    }

    public void initAllComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        mAppComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
