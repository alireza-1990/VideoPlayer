package com.alirezaahmadi.videoplayer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

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
