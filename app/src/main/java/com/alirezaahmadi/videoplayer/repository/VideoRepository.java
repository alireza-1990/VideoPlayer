package com.alirezaahmadi.videoplayer.repository;

import com.alirezaahmadi.videoplayer.db.PlaylistItemDao;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.util.StorageUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;

@Singleton
public class VideoRepository {
    private StorageUtil storageUtil;
    private PlaylistItemDao playlistItemDao;

    @Inject
    public VideoRepository(StorageUtil storageUtil, PlaylistItemDao playlistItemDao) {
        this.storageUtil = storageUtil;
        this.playlistItemDao = playlistItemDao;
    }

    public Observable<List<Video>> getVideoList(){
        return Observable.fromCallable(() -> storageUtil.getAllVideos());
    }

    public Observable<List<Video>> getPlaylistVideos(int playListId) {
        return playlistItemDao.loadPlayListItems(playListId)
                .toObservable()
                .flatMap(playlistItems -> {
                    String[] videoIds = new String[playlistItems.size()];
                    for (int i = 0; i < playlistItems.size(); i++)
                        videoIds[i] = String.valueOf(playlistItems.get(i).getVideoId());

                    return Observable.fromCallable(() -> storageUtil.getVideosForIds(videoIds));
                });
    }
}
