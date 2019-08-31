package com.alirezaahmadi.videoplayer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.media.AudioManager;

import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlayerViewModel extends ViewModel {

    private MutableLiveData<List<Video>> videoList = new MutableLiveData<>();
    private MutableLiveData<Video> currentVideo = new MutableLiveData<>();
    private MutableLiveData<Integer> volumeValue = new MutableLiveData<>();

    private VideoRepository videoRepository;
    private AudioManager audioManager;

    private int videoId = -1;
    private int playlistId = -1;
    private int currentIndex;

    @Inject
    public PlayerViewModel(VideoRepository videoRepository, AudioManager audioManager) {
        this.videoRepository = videoRepository;
        this.audioManager = audioManager;
        setCurrentVolumeVlue();
    }

    public void init(int videoId){
        init(videoId, -1);
    }

    public void init(int videoId, int playlistId){
        this.videoId = videoId;
        this.playlistId = playlistId;

        if(videoList.getValue() != null) //already having the list
            updateCurrentVideo();
    }


    public LiveData<Video> getCurrentVideo() {
        if(videoId != -1 && videoList.getValue() == null){
            loadVideos();
        }
        return currentVideo;
    }

    private void loadVideos() {
        Flowable<List<Video>> flowable = playlistId != -1 ? videoRepository.getPlaylistVideos(playlistId) : videoRepository.getVideoList();

        flowable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(videos -> {
                videoList.setValue(videos);
                updateCurrentVideo();
            });
    }

    public LiveData<Integer> getVolumeValue() {
        setCurrentVolumeVlue();
        return volumeValue;
    }

    private void updateCurrentVideo(){
        Video temp = new Video();
        temp.setId(videoId);
        int index = videoList.getValue().indexOf(temp);

        if(index != -1){
            currentIndex = index;
            currentVideo.setValue(videoList.getValue().get(index));
        }
    }

    private void setCurrentVolumeVlue(){
        int value = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeValue.setValue(value);
    }

    public void volumeUp(){
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        setCurrentVolumeVlue();
    }

    public void volumeDown(){
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        setCurrentVolumeVlue();
    }

    public void next(){
        if(currentIndex < videoList.getValue().size() -1)
            currentIndex++;
        else
            currentIndex = 0;

        currentVideo.setValue(videoList.getValue().get(currentIndex));
    }

    public void prev(){
        if(currentIndex > 0)
            currentIndex--;
        else
            currentIndex = videoList.getValue().size() -1;

        currentVideo.setValue(videoList.getValue().get(currentIndex));
    }
}
