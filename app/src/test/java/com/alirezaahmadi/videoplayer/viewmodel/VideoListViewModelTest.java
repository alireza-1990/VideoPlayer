package com.alirezaahmadi.videoplayer.viewmodel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.alirezaahmadi.videoplayer.BaseTest;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;
import com.alirezaahmadi.videoplayer.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class VideoListViewModelTest extends BaseTest {
    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    VideoRepository videoRepository;
    VideoListViewModel viewModel;

    @Before
    public void init(){
        videoRepository = mock(VideoRepository.class);
        viewModel = new VideoListViewModel(videoRepository);

        when(videoRepository.getVideoList()).thenReturn(Flowable.just(new ArrayList<>()));
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
        when(videoRepository.getVideoList()).thenReturn(Flowable.just(originalList));
        assertEquals(viewModel.getVideoList().getValue().size(), 2);
        TestUtil.assertEqualsLists(originalList, viewModel.getVideoList().getValue());
    }
}