package com.alirezaahmadi.videoplayer.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.alirezaahmadi.videoplayer.model.Playlist;
import com.alirezaahmadi.videoplayer.model.PlaylistItem;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface PlaylistItemDao {
    @Query("SELECT * FROM playlistitem WHERE playlistId = :playlistId")
    Flowable<List<PlaylistItem>> loadPlayListItems(int playlistId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlaylistItems(List<PlaylistItem> playlistItems);


    @Query("DELETE FROM playlistitem WHERE videoId = :videoId AND playlistId = :playlistId")
    void deleteItemsFromPlaylist(int videoId, int playlistId);

}