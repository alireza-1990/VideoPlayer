package com.alirezaahmadi.videoplayer.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Playlist.class,
        parentColumns = "id",
        childColumns = "playlistId",
        onDelete = CASCADE))
public class PlaylistItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int videoId;

    @ColumnInfo(name = "playlistId")
    private int playlistId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }
}
