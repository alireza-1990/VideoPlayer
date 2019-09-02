package com.alirezaahmadi.videoplayer.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alirezaahmadi.videoplayer.model.Playlist;
import com.alirezaahmadi.videoplayer.repository.PlaylistItemRepository;
import com.alirezaahmadi.videoplayer.repository.PlaylistRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlaylistViewModel extends BaseViewModel {
    private MutableLiveData<List<Playlist>> playlists = new MutableLiveData<>();
    private PlaylistRepository playlistRepository;
    private PlaylistItemRepository playlistItemRepository;
    private List<Integer> videoIdsToAdd = new ArrayList<>();

    @Inject
    public PlaylistViewModel(PlaylistRepository playlistRepository, PlaylistItemRepository playlistItemRepository) {
        this.playlistRepository = playlistRepository;
        this.playlistItemRepository = playlistItemRepository;

        playlists.setValue(new ArrayList<>());
    }

    public List<Integer> getVideoIdsToAdd() {
        return videoIdsToAdd;
    }

    public void setVideoIdsToAdd(List<Integer> videoIdsToAdd) {
        this.videoIdsToAdd = videoIdsToAdd;
    }

    public LiveData<List<Playlist>> getPlaylists() {
        Disposable disposable = playlistRepository.getPlaylists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> playlists.setValue(videos));

        addToUnsubsribed(disposable);

        return playlists;
    }

    public void addPlayList(String title){
        Playlist playList = new Playlist(title);
        playlistRepository.addPlaylist(playList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void addVideosToPlaylist(List<Integer> videoIds, int playlistId){
        playlistItemRepository.addVideoToPlayList(videoIds, playlistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deletePlayList(int playlistId){
        playlistRepository.deletePlayList(playlistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
