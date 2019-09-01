package com.alirezaahmadi.videoplayer.repository;

import com.alirezaahmadi.videoplayer.db.PlaylistDao;
import com.alirezaahmadi.videoplayer.db.PlaylistItemDao;
import com.alirezaahmadi.videoplayer.model.Playlist;
import com.alirezaahmadi.videoplayer.model.PlaylistItem;
import com.alirezaahmadi.videoplayer.util.StorageUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class PlaylistRepository {
    private PlaylistDao playlistDao;

    @Inject
    public PlaylistRepository(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    public Flowable<List<Playlist>> getPlaylists(){
        return playlistDao.loadAllPlayLists();
    }

    public Flowable<Playlist> getPlaylistById(int playlistId){
        return playlistDao.getPlaylistById(playlistId);
    }

    public Single addPlaylist(Playlist playlist){
        return Single.fromCallable(() -> {
            playlistDao.insertPlaylist(playlist);
            return new Object();
        });
    }

    public Single deletePlayList(int playlistId){
        return Single.fromCallable(() -> {
            playlistDao.deletePlaylist(playlistId);
            return new Object();
        });
    }
}
