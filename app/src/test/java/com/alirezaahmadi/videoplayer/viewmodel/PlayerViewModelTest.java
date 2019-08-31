package com.alirezaahmadi.videoplayer.viewmodel;

import androidx.lifecycle.Observer;
import android.media.AudioManager;

import com.alirezaahmadi.videoplayer.BaseTest;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;
import com.alirezaahmadi.videoplayer.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PlayerViewModelTest extends BaseTest {

//    @Rule
//    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    VideoRepository videoRepository;
    AudioManager audioManager;

    PlayerViewModel viewModel;

    int currentVolume = 5;
    int maxVolume = 15;

    @Before
    public void init(){
        videoRepository = mock(VideoRepository.class);
        audioManager = mock(AudioManager.class);

        viewModel = new PlayerViewModel(videoRepository, audioManager);

        List<Video> originalList = TestUtil.getVideoList();
        when(videoRepository.getVideoList()).thenReturn(Observable.just(originalList));

        when(audioManager.getStreamMaxVolume(anyInt())).thenReturn(15);

        doAnswer(invocation -> currentVolume).when(audioManager).getStreamVolume(anyInt());

        doAnswer(invocation -> {
            if(currentVolume < maxVolume)
                currentVolume++;
            return null;
        }).when(audioManager).adjustVolume(eq(AudioManager.ADJUST_RAISE), anyInt());

        doAnswer(invocation -> {
            if(currentVolume > 0)
                currentVolume--;
            return null;
        }).when(audioManager).adjustVolume(eq(AudioManager.ADJUST_LOWER), anyInt());
    }

    @Test
    public void dontFetchWithoutObserver(){
        verify(videoRepository, never()).getVideoList();
    }

    @Test
    public void dontFetchWithoutVideoId(){
        viewModel.getCurrentVideo().observeForever(mock(Observer.class));
        verify(videoRepository, never()).getVideoList();
    }

    @Test
    public void fetchOnceForMultipleObserver(){
        viewModel.init(1);
        viewModel.getCurrentVideo().observeForever(mock(Observer.class));
        viewModel.getCurrentVideo().observeForever(mock(Observer.class));
        verify(videoRepository, times(1)).getVideoList();
    }

    @Test
    public void changingIdChangesCurrentVideo() throws Exception {
        viewModel.init(1);
        assertEquals(viewModel.getCurrentVideo().getValue().getId(), 1);
        viewModel.init(2);
        assertEquals(viewModel.getCurrentVideo().getValue().getId(), 2);
    }

    @Test
    public void volumeUpWhenNotMax() throws Exception {
        Observer observer = mock(Observer.class);
        viewModel.getVolumeValue().observeForever(observer);
        currentVolume = 5;
        viewModel.volumeUp();

        //make sure observer getting called twice, once when subscribing and second when value is changed.
        verify(observer, times(2)).onChanged(any());
        assertTrue(viewModel.getVolumeValue().getValue() == 6);
    }

    @Test
    public void volumeUpWhenMax(){
        Observer observer = mock(Observer.class);
        viewModel.getVolumeValue().observeForever(observer);
        currentVolume = 15;
        viewModel.volumeUp();
        verify(observer, times(2)).onChanged(any());
        assertTrue(viewModel.getVolumeValue().getValue() == maxVolume);
    }

    @Test
    public void volumeDown() throws Exception {
        Observer observer = mock(Observer.class);
        viewModel.getVolumeValue().observeForever(observer);
        currentVolume = 5;
        viewModel.volumeDown();
        verify(observer, times(2)).onChanged(any());
        assertTrue(viewModel.getVolumeValue().getValue() == 4);
    }

    @Test
    public void volumeDownWhenMin(){
        Observer observer = mock(Observer.class);
        viewModel.getVolumeValue().observeForever(observer);
        currentVolume = 0;
        viewModel.volumeDown();
        verify(observer, times(2)).onChanged(any());
        assertTrue(viewModel.getVolumeValue().getValue() == 0);
    }

    @Test
    public void next() throws Exception {
        final List<Video> videoList = videoRepository.getVideoList().blockingFirst();
        viewModel.init(videoList.get(0).getId());
        viewModel.getCurrentVideo().observeForever(mock(Observer.class));
        viewModel.next();
        assertEquals(viewModel.getCurrentVideo().getValue().getId(), videoList.get(1).getId());
    }

    @Test
    public void nextOnLastGoToFirst(){
        final List<Video> videoList = videoRepository.getVideoList().blockingFirst();
        viewModel.init(videoList.get(videoList.size() -1).getId());
        viewModel.getCurrentVideo().observeForever(mock(Observer.class));
        viewModel.next();
        assertEquals(viewModel.getCurrentVideo().getValue().getId(), videoList.get(0).getId());
    }

    @Test
    public void prev() throws Exception {
        final List<Video> videoList = videoRepository.getVideoList().blockingFirst();
        viewModel.init(videoList.get(1).getId());
        viewModel.getCurrentVideo().observeForever(mock(Observer.class));
        viewModel.prev();
        assertEquals(viewModel.getCurrentVideo().getValue().getId(), videoList.get(0).getId());
    }

    @Test
    public void prevOnFirstGoToLast(){
        final List<Video> videoList = videoRepository.getVideoList().blockingFirst();
        viewModel.init(videoList.get(0).getId());
        viewModel.getCurrentVideo().observeForever(mock(Observer.class));
        viewModel.prev();
        assertEquals(viewModel.getCurrentVideo().getValue().getId(), videoList.get(videoList.size() -1).getId());
    }

}