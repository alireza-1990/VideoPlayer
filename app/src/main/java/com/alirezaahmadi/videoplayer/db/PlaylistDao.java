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