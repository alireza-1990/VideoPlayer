package com.alirezaahmadi.videoplayer;

import android.app.Activity;
import android.app.Application;

import com.alirezaahmadi.videoplayer.di.AppComponent;
import com.alirezaahmadi.videoplayer.di.AppModule;
import com.alirezaahmadi.videoplayer.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class VideoPlayerApplication extends Application implements HasActivityInjector {

    @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .appModule(new AppModule(this))
                .build();

        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
