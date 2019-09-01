package com.alirezaahmadi.videoplayer.repository;

import com.alirezaahmadi.videoplayer.BaseTest;
import com.alirezaahmadi.videoplayer.TestUtil;
import com.alirezaahmadi.videoplayer.db.PlaylistDao;
import com.alirezaahmadi.videoplayer.db.PlaylistItemDao;
import com.alirezaahmadi.videoplayer.model.Playlist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class PlaylistRepositoryTest extends BaseTest {
    private List<Playlist> mockPlaylists;

    private PlaylistDao playlistDao;
    private PlaylistItemDao playlistItemDao;
    private PlaylistRepository playlistRepository;


    @Before
    public void init(){

        mockPlaylists = TestUtil.getMockPlaylists();
        playlistDao = mock(PlaylistDao.class);
        when(playlistDao.loadAllPlayLists()).thenReturn(Flowable.just(mockPlaylists));
        when(playlistDao.getPlaylistById(1)).thenReturn(Flowable.just(mockPlaylists.get(0)));

        playlistItemDao = mock(PlaylistItemDao.class);

        playlistRepository = new PlaylistRepository(playlistDao, playlistItemDao);
    }

    @Test
    public void getPlaylists() {
        List<Playlist> playlists = playlistRepository.getPlaylists().blockingFirst();
        TestUtil.assertEqualsPlayLists(playlists, mockPlaylists);
    }

    @Test
    public void getPlaylistById() {
        Playlist playlist = playlistRepository.getPlaylistById(1).blockingFirst();
        assertEquals(playlist.getId(), mockPlaylists.get(0).getId());
        assertEquals(playlist.getTitle(), mockPlaylists.get(0).getTitle());
    }

    @Test
    public void addPlaylist_callsTheDeoWithTheSameObject() {
        Playlist playlist =  new Playlist("title 1");
        playlistRepository.addPlaylist(playlist).blockingGet();
        Mockito.verify(playlistDao, times(1)).insertPlaylist(playlist);
    }

    @Test
    public void deletePlayList_callsTheDeoWithTheSameId() {
        playlistRepository.deletePlayList(1).blockingGet();
        Mockito.verify(playlistDao, times(1)).deletePlaylist(1);
    }

    @Test
    public void addVideoToPlayList() {

    }

    @Test
    public void deleteVideosFromPlayList() {
        List<Integer> videoIdList = new ArrayList<>();
        videoIdList.add(1);
        videoIdList.add(2);

        playlistRepository.deleteVideosFromPlayList(videoIdList, 1).blockingGet();

        Mockito.verify(playlistItemDao, times(1)).deleteItemsFromPlaylist(1,1);
        Mockito.verify(playlistItemDao, times(1)).deleteItemsFromPlaylist(2,1);
    }
}