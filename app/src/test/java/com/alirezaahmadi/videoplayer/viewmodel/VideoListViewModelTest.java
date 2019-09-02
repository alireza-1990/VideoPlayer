package com.alirezaahmadi.videoplayer.viewmodel;

import com.alirezaahmadi.videoplayer.BaseTest;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;
import com.alirezaahmadi.videoplayer.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//todo update the test cases

@RunWith(RobolectricTestRunner.class)
public class VideoListViewModelTest extends BaseTest  {
    private List<Video> mockVideoList;
    private VideoRepository videoRepository;
    private VideoListViewModel viewModel;

    @Before
    public void init(){
        mockVideoList = TestUtil.getMockVideoList();
        videoRepository = mock(VideoRepository.class);
        when(videoRepository.getVideoList()).thenReturn(Observable.just(mockVideoList));

        viewModel = new VideoListViewModel(videoRepository);
    }

    @Test
    public void immediatelyFetchesVideos(){
        verify(videoRepository, times(1)).getVideoList();
    }

    @Test
    public void getDataWhenRepoIsNotEmpty() throws Exception {
        List<Video> videoList = viewModel.getVideoList().getValue();
        assertEquals(2, videoList.size());
        TestUtil.assertEqualsVideoLists(videoList, mockVideoList);
    }



}