package com.alirezaahmadi.videoplayer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VideoListViewModel extends BaseViewModel {
    private MutableLiveData<List<Video>> videoList = new MutableLiveData<>();
    private VideoRepository videoRepository;

    @Inject
    public VideoListViewModel(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public LiveData<List<Video>> getVideoList() {
        if(videoList.getValue() == null){
            Disposable disposable = videoRepository.getVideoList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(videos -> videoList.setValue(videos));

            addToUnsubsribed(disposable);
        }

        return videoList;
    }

}
