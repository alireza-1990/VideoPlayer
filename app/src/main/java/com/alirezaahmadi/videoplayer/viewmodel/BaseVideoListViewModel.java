package com.alirezaahmadi.videoplayer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

abstract class BaseVideoListViewModel extends BaseViewModel {
    private MutableLiveData<List<Integer>> selectedVideoIds = new MutableLiveData<>();
    private MutableLiveData<Boolean> selectionMode = new MutableLiveData<>();

    public BaseVideoListViewModel() {
        selectedVideoIds.setValue(new ArrayList<>());
        selectionMode.setValue(false);
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

    public void turnOffSelectionMode(){
        selectionMode.setValue(false);
        selectedVideoIds.getValue().clear();
        selectedVideoIds.setValue(selectedVideoIds.getValue());
    }
}
