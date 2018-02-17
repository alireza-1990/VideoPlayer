package com.alirezaahmadi.videoplayer.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.alirezaahmadi.videoplayer.viewmodel.DaggerViewModelFactory;
import com.alirezaahmadi.videoplayer.viewmodel.PlayerViewModel;
import com.alirezaahmadi.videoplayer.viewmodel.PlaylistDetailViewModel;
import com.alirezaahmadi.videoplayer.viewmodel.PlaylistViewModel;
import com.alirezaahmadi.videoplayer.viewmodel.VideoListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(DaggerViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(VideoListViewModel.class)
    abstract ViewModel provideVideoListViewModel(VideoListViewModel videoListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel.class)
    abstract ViewModel providePlayerViewModel(PlayerViewModel playerViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistViewModel.class)
    abstract ViewModel providePlaylistViewModel(PlaylistViewModel playlistViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistDetailViewModel.class)
    abstract ViewModel providePlaylistDetailViewModel(PlaylistDetailViewModel playlistDetailViewModel);
}
