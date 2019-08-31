package com.alirezaahmadi.videoplayer.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.alirezaahmadi.videoplayer.model.Playlist;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlaylist(Playlist playlist);

    @Query("DELETE FROM playlist WHERE id = :playlistId")
    void deletePlaylist(int playlistId);

    @Query("SELECT * FROM playlist ORDER BY id DESC")
    Flowable<List<Playlist>> loadAllPlayLists();

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    Flowable<Playlist> getPlaylistById(int playlistId);

}