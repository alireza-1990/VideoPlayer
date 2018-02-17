package com.alirezaahmadi.videoplayer.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;

import com.alirezaahmadi.videoplayer.adapter.PlaylistAdapter;
import com.alirezaahmadi.videoplayer.adapter.VideoAdapter;
import com.alirezaahmadi.videoplayer.db.PlaylistDao;
import com.alirezaahmadi.videoplayer.db.PlaylistItemDao;
import com.alirezaahmadi.videoplayer.db.VideoPlayerDb;
import com.alirezaahmadi.videoplayer.model.Video;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton @Provides
    Application providesApplication() {
        return application;
    }

    @Singleton @Provides
    AudioManager providesAudioManager(Application app){
        return (AudioManager) app.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    @Provides
    ContentResolver providesContentResolver(Application app){
        return app.getContentResolver();
    }

    @Singleton @Provides
    VideoPlayerDb provideDb(Application app) {
        return Room.databaseBuilder(app, VideoPlayerDb.class,"video_player.db").build();
    }

    @Singleton @Provides
    PlaylistDao providePlaylistDao(VideoPlayerDb db) {
        return db.playlistDao();
    }

    @Singleton @Provides
    PlaylistItemDao providePlaylistItemDao(VideoPlayerDb db) {
        return db.playlistItemDao();
    }

    @Named("withDelete") @Provides
    PlaylistAdapter providesPlaylistAdapterWithDelete(Application app){
        return new PlaylistAdapter(app, true);
    }

    @Named("withoutDelete") @Provides
    PlaylistAdapter providesPlaylistAdapterWithoutDelete(Application app){
        return new PlaylistAdapter(app, false);
    }


}
