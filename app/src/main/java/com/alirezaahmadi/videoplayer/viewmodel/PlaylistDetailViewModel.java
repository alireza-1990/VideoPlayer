package com.alirezaahmadi.videoplayer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.view.View;

import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.PlaylistRepository;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlaylistDetailViewModel extends ViewModel {
    private MutableLiveData<List<Video>> videoList = new MutableLiveData<>();
    private MutableLiveData<Integer> emptyErrorVisibility = new MutableLiveData<>();
    private MutableLiveData<String> title = new MutableLiveData<>();

    private VideoRepository videoRepository;
    private PlaylistRepository playlistRepository;
    private int playlistId = -1;

    @Inject
    public PlaylistDetailViewModel(VideoRepository videoRepository, PlaylistRepository playlistRepository) {
        this.videoRepository = videoRepository;
        this.playlistRepository = playlistRepository;
    }

    public LiveData<List<Video>> getVideoList() {
        return videoList;
    }

    public LiveData<Integer> getEmptyErrorVisibility() {
        return emptyErrorVisibility;
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
        getVideos();
        loadPlaylist();
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void deleteVideos(List<Integer> videoIds){
        playlistRepository.deleteVideosFromPlayList(videoIds, playlistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void getVideos(){
        videoRepository.getPlaylistVideos(playlistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> {
                    videoList.setValue(videos);
                    emptyErrorVisibility.setValue(videos.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }

    private void loadPlaylist() {
        playlistRepository.getPlaylistById(playlistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playlist -> title.setValue(playlist.getTitle()));
    }
}
