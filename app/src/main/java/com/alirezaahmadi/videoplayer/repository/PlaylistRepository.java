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
    private PlaylistItemDao playlistItemDao;

    @Inject
    public PlaylistRepository(PlaylistDao playlistDao, PlaylistItemDao playlistItemDao) {
        this.playlistDao = playlistDao;
        this.playlistItemDao = playlistItemDao;
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

    public Single detelePlayList(int playlistId){
        return Single.fromCallable(() -> {
            playlistDao.deletePlaylist(playlistId);
            return new Object();
        });
    }

    public Single addVideoToPlayList(List<Integer> videoIds, int playListId){
        return Single.fromCallable(() -> {
            List<PlaylistItem> playlistItems = new ArrayList<>();

            for(int id: videoIds){
                PlaylistItem item = new PlaylistItem();
                item.setVideoId(id);
                item.setPlaylistId(playListId);
                playlistItems.add(item);
            }

            playlistItemDao.insertPlaylistItems(playlistItems);
            return new Object();
        });
    }

    public Single deleteVideosFromPlayList(List<Integer> videoIds, int playListId){
        return Single.fromCallable(() -> {
            for(int id: videoIds){
                playlistItemDao.deleteItemsFromPlaylist(id, playListId);
            }

            return new Object();
        });
    }
}
