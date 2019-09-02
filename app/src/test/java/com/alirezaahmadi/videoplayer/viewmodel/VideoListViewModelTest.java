package com.alirezaahmadi.videoplayer.viewmodel;

import androidx.lifecycle.Observer;

import com.alirezaahmadi.videoplayer.BaseTest;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;
import com.alirezaahmadi.videoplayer.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//todo update the test cases

@RunWith(RobolectricTestRunner.class)
public class VideoListViewModelTest  {
    VideoRepository videoRepository;
    VideoListViewModel viewModel;

    @Before
    public void init(){
        videoRepository = mock(VideoRepository.class);
        viewModel = new VideoListViewModel(videoRepository);

        //todo return list with actual data
        when(videoRepository.getVideoList()).thenReturn(Observable.just(new ArrayList<>()));
    }

    @Test
    public void dontFetchWithoutObserver(){
        verify(videoRepository, never()).getVideoList();
    }

    @Test
    public void fetchWhenThereIsObserver() {
        viewModel.getVideoList().observeForever(mock(Observer.class));
        verify(videoRepository, times(1)).getVideoList();
    }

    @Test
    public void fetchOnceForMultipleObserver(){
        Observer observer1 = mock(Observer.class);
        Observer observer2 = mock(Observer.class);
        viewModel.getVideoList().observeForever(observer1);
        viewModel.getVideoList().observeForever(observer2);
        verify(videoRepository, times(1)).getVideoList();
    }

    @Test
    public void getDataWhenRepoIsNotEmpty() throws Exception {
        final List<Video> originalList = TestUtil.getVideoList();
        when(videoRepository.getVideoList()).thenReturn(Observable.just(originalList));
        assertEquals(viewModel.getVideoList().getValue().size(), 2);
        TestUtil.assertEqualsVideoLists(originalList, viewModel.getVideoList().getValue());
    }
}