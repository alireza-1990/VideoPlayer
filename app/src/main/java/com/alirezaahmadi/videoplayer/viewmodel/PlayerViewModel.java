package com.alirezaahmadi.videoplayer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.media.AudioManager;

import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlayerViewModel extends BaseViewModel {

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
        setCurrentVolumeVolume();
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
        Observable<List<Video>> observable = playlistId != -1 ? videoRepository.getPlaylistVideos(playlistId) : videoRepository.getVideoList();

        Disposable disposable = observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(videos -> {
                videoList.setValue(videos);
                updateCurrentVideo();
            });

        addToUnsubsribed(disposable);
    }

    public LiveData<Integer> getVolumeValue() {
        setCurrentVolumeVolume();
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

    private void setCurrentVolumeVolume(){
        int value = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeValue.setValue(value);
    }

    public void volumeUp(){
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        setCurrentVolumeVolume();
    }

    public void volumeDown(){
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        setCurrentVolumeVolume();
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
