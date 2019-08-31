package com.alirezaahmadi.videoplayer.util;

import android.content.ContentResolver;
import android.database.MatrixCursor;
import android.provider.MediaStore;

import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.mockito.Mockito.*;


@RunWith(RobolectricTestRunner.class)
public class StorageUtilTest {

    private StorageUtil storageUtil;
    private ContentResolver contentResolver;

    private List<Video> originalList;

    @Before
    public void init(){
        contentResolver = Mockito.mock(ContentResolver.class);
        originalList = TestUtil.getVideoList();

        MatrixCursor videoCursor = new MatrixCursor(new String[] { MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.TITLE,
                MediaStore.Video.Media.DATA
        });

        for(Video video: originalList) {
            videoCursor.addRow(new Object[]{video.getId(), video.getTitle(), video.getVideoPath()});
        }

        when(contentResolver.query(eq(MediaStore.Video.Media.EXTERNAL_CONTENT_URI),
                any(String[].class), isNull(), isNull(), isNull()))
                .thenReturn(videoCursor);


        for(Video video: originalList) {
            MatrixCursor thumbnailCursor = new MatrixCursor(new String[] {
                    MediaStore.Video.Thumbnails.VIDEO_ID, MediaStore.Video.Thumbnails.DATA});

            thumbnailCursor.addRow(new Object[]{video.getId(), video.getThumbnailPath()});

            when(contentResolver.query(eq(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI),
                    any(String[].class), anyString(), eq(new String[]{String.valueOf(video.getId())}), isNull()))
                    .thenReturn(thumbnailCursor);
        }

        storageUtil = new StorageUtil(contentResolver);
    }

    @Test
    public void getAllVideos_returnsAllTheItems() throws Exception {
        List<Video> videoList = storageUtil.getAllVideos();
        TestUtil.assertEqualsLists(originalList, videoList);
    }

}