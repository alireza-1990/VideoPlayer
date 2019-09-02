package com.alirezaahmadi.videoplayer.repository;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import com.alirezaahmadi.videoplayer.db.PlaylistDao;
import com.alirezaahmadi.videoplayer.db.PlaylistItemDao;
import com.alirezaahmadi.videoplayer.db.VideoPlayerDb;
import com.alirezaahmadi.videoplayer.model.Playlist;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PlaylistRepositoryTest {

    private VideoPlayerDb db;
    private PlaylistDao playlistDao;

    private PlaylistRepository playlistRepository;

    @Before
    public void init(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, VideoPlayerDb.class).build();
        playlistDao = db.playlistDao();

        playlistRepository = new PlaylistRepository(playlistDao);
    }

    @Test
    public void onInit_getPlaylists_shouldBeEmptyList() {
        List<Playlist> playlists = playlistRepository.getPlaylists().blockingFirst();
        assertEquals(playlists.size(), 0);
    }

    @Test
    public void whenDbHasPlayList_getPlaylists_returnAllDbPlaylists(){
        playlistDao.insertPlaylist(new Playlist("Playlist one"));
        playlistDao.insertPlaylist(new Playlist("Playlist two"));
        playlistDao.insertPlaylist(new Playlist("Playlist three"));

        List<Playlist> playlists = playlistRepository.getPlaylists().blockingFirst();
        assertEquals(playlists.size(),3);
        assertEquals(playlists.get(0).getTitle(), "Playlist three");
        assertEquals(playlists.get(1).getTitle(), "Playlist two");
        assertEquals(playlists.get(2).getTitle(), "Playlist one");
    }

    @Test
    public void ifPlayListExists_getPlaylistById_returnsPlaylist() {
        Playlist playlistOne = new Playlist("Playlist one");
        playlistDao.insertPlaylist(playlistOne);

        Playlist playlist = playlistRepository.getPlaylistById(1).blockingFirst();
        assertEquals(playlist.getTitle(), "Playlist one");
    }

    @Test
    public void addPlaylist_addsThePlaylistToTheDb() {
        Playlist playlist = new Playlist("Playlist one");
        playlistRepository.addPlaylist(playlist).blockingGet();

        List<Playlist> playlists = playlistDao.loadAllPlayLists().blockingFirst();
        assertEquals(1, playlists.size());
        assertEquals(playlists.get(0).getTitle(), "Playlist one");
    }

    @Test
    public void deletePlayList_removesThePlaylistFromDb() {
        playlistDao.insertPlaylist(new Playlist("Playlist one"));
        playlistDao.insertPlaylist(new Playlist("Playlist two"));
        playlistDao.insertPlaylist(new Playlist("Playlist three"));

        playlistRepository.deletePlayList(2).blockingGet();

        List<Playlist> playlists = playlistDao.loadAllPlayLists().blockingFirst();
        assertEquals(2, playlists.size());
        assertEquals(playlists.get(0).getTitle(), "Playlist three");
        assertEquals(playlists.get(1).getTitle(), "Playlist one");
    }

}