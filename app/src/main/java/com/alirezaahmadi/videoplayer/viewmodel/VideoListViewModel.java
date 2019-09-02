package com.alirezaahmadi.videoplayer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VideoListViewModel extends BaseVideoListViewModel {
    private VideoRepository videoRepository;
    private MutableLiveData<List<Video>> videoList = new MutableLiveData<>();

    @Inject
    public VideoListViewModel(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;

        videoList.setValue(new ArrayList<>());
        updateData();
    }

    private void updateData(){
        Disposable disposable = videoRepository.getVideoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> videoList.setValue(videos));

        addToUnsubsribed(disposable);

    }

    public LiveData<List<Video>> getVideoList() {
        return videoList;
    }

}
