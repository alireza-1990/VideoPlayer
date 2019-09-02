package com.alirezaahmadi.videoplayer.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.alirezaahmadi.videoplayer.BaseTest;
import com.alirezaahmadi.videoplayer.TestUtil;
import com.alirezaahmadi.videoplayer.model.Playlist;
import com.alirezaahmadi.videoplayer.repository.PlaylistItemRepository;
import com.alirezaahmadi.videoplayer.repository.PlaylistRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PlaylistViewModelTest extends BaseTest {

    private PlaylistRepository playlistRepository;
    private PlaylistItemRepository playlistItemRepository;
    private PlaylistViewModel playlistViewModel;
    private List<Playlist> mockPlaylists;


    @Before
    public void init(){
        mockPlaylists = TestUtil.getMockPlaylists();
        playlistRepository = mock(PlaylistRepository.class);
        playlistItemRepository = mock(PlaylistItemRepository.class);

        when(playlistRepository.getPlaylists()).thenReturn(Flowable.just(mockPlaylists));
        when(playlistRepository.addPlaylist(any())).thenReturn(Single.just(new Object()));
        when(playlistRepository.deletePlayList(anyInt())).thenReturn(Single.just(new Object()));
        when(playlistItemRepository.addVideoToPlayList(any(), anyInt())).thenReturn(Single.just(new Object()));

        playlistViewModel = new PlaylistViewModel(playlistRepository, playlistItemRepository);
    }

    @Test
    public void getPlaylists_shouldReturnExactListFromRepository() {
        List<Playlist> playlistLists = playlistViewModel.getPlaylists().getValue();
        TestUtil.assertEqualsPlayLists(mockPlaylists, playlistLists);
    }

    @Test
    public void addPlayList_callsTheRepository() {
        playlistViewModel.addPlayList("Playlist one");
        verify(playlistRepository, times(1)).addPlaylist(any());
    }

    @Test
    public void deletePlaylists_callsTheRepositoryOnce() {
        playlistViewModel.deletePlayList(2);
        verify(playlistRepository, times(1)).deletePlayList(2);
    }

    @Test
    public void addVideoToPlaylists_callsTheRepoWithExactParameters(){
        List<Integer> videoIds = new ArrayList<>();
        playlistViewModel.addVideosToPlaylist(videoIds, 1);
        verify(playlistItemRepository, times(1)).addVideoToPlayList(videoIds, 1);
    }
}