package com.alirezaahmadi.videoplayer.repository;

import com.alirezaahmadi.videoplayer.TestUtil;
import com.alirezaahmadi.videoplayer.db.PlaylistItemDao;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.util.StorageUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.reactivex.Flowable;

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
        when(storageUtil.getVideosForIds(new String[]{"2"})).thenReturn(mockVideoList.subList(0, 1));

        playlistItemDao = mock(PlaylistItemDao.class);
        when(playlistItemDao.loadPlayListItems(any())).thenReturn(Flowable.just(TestUtil.getMockPlaylistItems()));

        videoRepository = new VideoRepository(storageUtil, playlistItemDao);
    }

    @Test
    public void getVideoList_returnsExactListFromStorageUtil() {
        List<Video> videoList = videoRepository.getVideoList().blockingFirst();
        TestUtil.assertEqualsVideoLists(videoList, this.mockVideoList);
    }


    //todo test not working... decide to keep or remove
    @Test
    public void getPlaylistVideos() {
        List<Video> videoList = videoRepository.getPlaylistVideos(1).blockingFirst();

    }
}