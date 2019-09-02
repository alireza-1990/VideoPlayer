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

public class VideoListViewModel extends BaseViewModel {
    private VideoRepository videoRepository;
    private MutableLiveData<List<Video>> videoList = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> selectedVideoIds = new MutableLiveData<>();
    private MutableLiveData<Boolean> selectionMode = new MutableLiveData<>();

    @Inject
    public VideoListViewModel(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;

        videoList.setValue(new ArrayList<>());
        selectedVideoIds.setValue(new ArrayList<>());
        selectionMode.setValue(false);

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

    public LiveData<List<Integer>> getSelectedVideoIds() {
        return selectedVideoIds;
    }

    public LiveData<Boolean> getSelectionMode() {
        return selectionMode;
    }

    public void changeVideoSelectionState(int videoId){
        selectionMode.setValue(true);

        if(selectedVideoIds.getValue().contains(videoId)){
            selectedVideoIds.getValue().remove(Integer.valueOf(videoId));

        } else {
            selectedVideoIds.getValue().add(videoId);
        }

        selectedVideoIds.setValue(selectedVideoIds.getValue());
    }
}
