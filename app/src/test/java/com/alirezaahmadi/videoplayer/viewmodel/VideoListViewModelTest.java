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
import java.util.List;
import io.reactivex.Observable;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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

    @Test
    public void selectedListIsEmptyByDefault(){
        List<Integer> selectedVideoIds = viewModel.getSelectedVideoIds().getValue();
        assertEquals(0, selectedVideoIds.size());
    }

    @Test
    public void selectionModeIsOffByDefault(){
        assertEquals(false, viewModel.getSelectionMode().getValue());
    }

    @Test
    public void whenAddingVideoToSelection_selectionModeSetsToTrue(){
        viewModel.changeVideoSelectionState(1);
        assertEquals(true, viewModel.getSelectionMode().getValue());
    }

    @Test
    public void whenAddingVideos_selectionListsGetsUpdated(){
        viewModel.changeVideoSelectionState(10);
        viewModel.changeVideoSelectionState(20);
        List<Integer> videoIds = viewModel.getSelectedVideoIds().getValue();
        assertEquals(2, videoIds.size());
        assertTrue(videoIds.contains(10));
        assertTrue(videoIds.contains(20));
    }

    @Test
    public void reCallingChangeVideoSelectionState_removesItFromSelectionList(){
        viewModel.changeVideoSelectionState(10);
        viewModel.changeVideoSelectionState(10);
        List<Integer> videoIds = viewModel.getSelectedVideoIds().getValue();
        assertEquals(0, videoIds.size());
    }

    @Test
    public void afterFirstAdd_whenSelectionListGetsEmpty_selectionModeIsStillTrue(){
        viewModel.changeVideoSelectionState(10);
        viewModel.changeVideoSelectionState(10);
        assertEquals(true, viewModel.getSelectionMode().getValue());
    }

    @Test
    public void whenSelectionModeIsOn_callingTurnOffSelectionMode_makeSelectionModeOff(){
        viewModel.changeVideoSelectionState(10);
        viewModel.turnOffSelectionMode();
        assertEquals(false, viewModel.getSelectionMode().getValue());
    }

    @Test
    public void callingTurnOffSelectionMode_emptiesTheSelectionList(){
        viewModel.changeVideoSelectionState(10);
        viewModel.changeVideoSelectionState(20);
        viewModel.turnOffSelectionMode();
    }

    @Test
    public void whenSelectionListGetsChanged_observersWillBeNotified(){
        Observer observer = mock(Observer.class);
        viewModel.getSelectedVideoIds().observeForever(observer);

        viewModel.changeVideoSelectionState(10);
        viewModel.changeVideoSelectionState(20);
        viewModel.changeVideoSelectionState(20);
        viewModel.turnOffSelectionMode();

        verify(observer, times(5)).onChanged(any());
    }

    @Test
    public void whenSelectionModeGetsChanged_observersWillBeNotified(){
        Observer observer = mock(Observer.class);
        viewModel.getSelectedVideoIds().observeForever(observer);

        viewModel.changeVideoSelectionState(10);
        viewModel.changeVideoSelectionState(20);
        viewModel.changeVideoSelectionState(30);

        verify(observer, times(4)).onChanged(any());
    }

}