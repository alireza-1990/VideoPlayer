package com.alirezaahmadi.videoplayer.util;

import android.app.Application;
import android.content.Intent;

import com.alirezaahmadi.videoplayer.activity.PlayListDetailActivity;
import com.alirezaahmadi.videoplayer.activity.PlayerActivity;

import javax.inject.Inject;

public class NavigationController {
    private Application application;

    @Inject
    public NavigationController(Application application) {
        this.application = application;
    }

    public void navigateToPlayer(int videoId){
        Intent intent = PlayerActivity.createIntent(application, videoId);
        application.startActivity(intent);
    }

    public void navigateToPlayer(int videoId, int playlistId){
        Intent intent = PlayerActivity.createIntent(application, videoId, playlistId);
        application.startActivity(intent);
    }

    public void navigateToPlaylistDetail(int playlistId){
        Intent intent = PlayListDetailActivity.createIntent(application,  playlistId);
        application.startActivity(intent);
    }


}
