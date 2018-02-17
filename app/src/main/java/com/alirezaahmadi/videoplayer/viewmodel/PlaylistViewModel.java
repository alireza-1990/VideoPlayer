package com.alirezaahmadi.videoplayer.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.alirezaahmadi.videoplayer.fragment.PlayListsFragment;
import com.alirezaahmadi.videoplayer.model.Playlist;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.PlaylistRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlaylistViewModel extends ViewModel {
    private MutableLiveData<List<Playlist>> playlists = new MutableLiveData<>();
    private PlaylistRepository repository;
    private List<Integer> videoIdsToAdd = new ArrayList<>();

    @Inject
    public PlaylistViewModel(PlaylistRepository repository) {
        this.repository = repository;
    }

    public List<Integer> getVideoIdsToAdd() {
        return videoIdsToAdd;
    }

    public void setVideoIdsToAdd(List<Integer> videoIdsToAdd) {
        this.videoIdsToAdd = videoIdsToAdd;
    }

    public LiveData<List<Playlist>> getPlaylists() {
        if(playlists.getValue() == null){
            repository.getPlaylists()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(videos -> playlists.setValue(videos));
        }

        return playlists;
    }

    public void addPlayList(String title){
        Playlist playList = new Playlist(title);
        repository.addPlaylist(playList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void addVideosToPlaylist(List<Integer> videoIds, int playlistId){
        repository.addVideoToPlayList(videoIds, playlistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deletePlayList(int playlistId){
        repository.detelePlayList(playlistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
