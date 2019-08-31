package com.alirezaahmadi.videoplayer.util;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.alirezaahmadi.videoplayer.model.Video;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * This class is responsible for getting all videos avaliable on user device.
 * It reads the data from MediaStore and returns a list of Video objects.
 */
public class StorageUtil {
    private ContentResolver contentResolver;

    @Inject
    public StorageUtil(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public List<Video> getAllVideos(){
        return readFromMediaStore(true, null);
    }

    public List<Video> getVideosForIds(String[] videoIds){
        return readFromMediaStore(false, videoIds);
    }

    private List<Video> readFromMediaStore(boolean readAll, String[] videoIds) {
        List<Video> videoList = new ArrayList<>();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.TITLE,
                MediaStore.Video.Media.DATA
        };

        String selection;
        String[] selectionArgs;

        if(readAll){
            selection = null;
            selectionArgs = null;

        } else if (videoIds.length == 0){
            return videoList;

        } else {
            selection = MediaStore.Video.VideoColumns._ID + " IN (" + makePlaceholders(videoIds.length) + ")";
            selectionArgs = videoIds;
        }

        Cursor c = contentResolver.query(uri, projection, selection, selectionArgs, null);
        if (c != null) {
            while (c.moveToNext()) {
                Video video = new Video();
                video.setId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.VideoColumns._ID)));
                video.setTitle(c.getString(c.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.TITLE)));
                video.setVideoPath(c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                video.setThumbnailPath(getThumbnailPath(video.getId()));
                videoList.add(video);
            }

            c.close();
        }

        return videoList;
    }

    private String getThumbnailPath(int videoId){
        String[] projection = { MediaStore.Video.Thumbnails.DATA };

        Uri videoUri = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;

        try (Cursor cursor = contentResolver.query(videoUri, projection, MediaStore.Video.Thumbnails.VIDEO_ID + "=?",
                new String[]{Integer.toString(videoId)}, null)) {

            if ((cursor != null) && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
            }
        }

        return null;
    }

    //todo how to test this function?!
    private String makePlaceholders(int len) {
        if (len < 1) {
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }
}
