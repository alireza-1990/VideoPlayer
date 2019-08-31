package com.alirezaahmadi.videoplayer.di;

import com.alirezaahmadi.videoplayer.activity.MainActivity;
import com.alirezaahmadi.videoplayer.activity.PlayListDetailActivity;
import com.alirezaahmadi.videoplayer.activity.PlayerActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = MainFragmentBuilder.class)
    abstract MainActivity bindVideoListActivity();

    @ContributesAndroidInjector
    abstract PlayerActivity bindPlayerActivity();

    @ContributesAndroidInjector
    abstract PlayListDetailActivity bindPlaylistDetailActivity();
}