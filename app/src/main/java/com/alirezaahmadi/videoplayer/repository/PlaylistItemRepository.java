package com.alirezaahmadi.videoplayer.repository;

import com.alirezaahmadi.videoplayer.db.PlaylistDao;
import com.alirezaahmadi.videoplayer.db.PlaylistItemDao;
import com.alirezaahmadi.videoplayer.model.Playlist;
import com.alirezaahmadi.videoplayer.model.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class PlaylistItemRepository {
    private PlaylistItemDao playlistItemDao;

    @Inject
    public PlaylistItemRepository(PlaylistItemDao playlistItemDao) {
        this.playlistItemDao = playlistItemDao;
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
