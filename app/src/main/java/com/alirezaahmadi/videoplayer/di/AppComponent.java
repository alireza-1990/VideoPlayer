package com.alirezaahmadi.videoplayer.di;

import android.app.Activity;


import com.alirezaahmadi.videoplayer.VideoPlayerApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class,
        ViewModelModule.class,
})

public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(VideoPlayerApplication application);
        Builder appModule(AppModule appModule);
        AppComponent build();
    }

    void inject(VideoPlayerApplication application);

}
