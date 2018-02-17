package com.alirezaahmadi.videoplayer.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.alirezaahmadi.videoplayer.model.Playlist;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.PlaylistRepository;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VideoListViewModel extends ViewModel {
    private MutableLiveData<List<Video>> videoList = new MutableLiveData<>();
    private VideoRepository videoRepository;

    @Inject
    public VideoListViewModel(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public LiveData<List<Video>> getVideoList() {
        if(videoList.getValue() == null){
            videoRepository.getVideoList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(videos -> videoList.setValue(videos));
        }

        return videoList;
    }

}
