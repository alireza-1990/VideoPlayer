package com.alirezaahmadi.videoplayer.repository;

import com.alirezaahmadi.videoplayer.TestUtil;
import com.alirezaahmadi.videoplayer.db.PlaylistItemDao;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.util.StorageUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VideoRepositoryTest {

    private StorageUtil storageUtil;
    private PlaylistItemDao playlistItemDao;
    private VideoRepository videoRepository;
    private List<Video> mockVideoList;

    @Before
    public void init(){
        storageUtil = mock(StorageUtil.class);
        mockVideoList = TestUtil.getVideoList();
        when(storageUtil.getAllVideos()).thenReturn(mockVideoList);
        when(storageUtil.getVideosForIds(any())).thenReturn(mockVideoList);

        playlistItemDao = mock(PlaylistItemDao.class);


        videoRepository = new VideoRepository(storageUtil, playlistItemDao);
    }

    @Test
    public void getVideoList_returnsExactListFromStorageUtil() {
        List<Video> videoList = videoRepository.getVideoList().blockingFirst();
        TestUtil.assertEqualsVideoLists(videoList, this.mockVideoList);
    }

    @Test
    public void getPlaylistVideos() {

    }
}