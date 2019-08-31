package com.alirezaahmadi.videoplayer.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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