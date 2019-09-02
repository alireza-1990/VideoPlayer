package com.alirezaahmadi.videoplayer.repository;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.alirezaahmadi.videoplayer.db.PlaylistDao;
import com.alirezaahmadi.videoplayer.db.PlaylistItemDao;
import com.alirezaahmadi.videoplayer.db.VideoPlayerDb;
import com.alirezaahmadi.videoplayer.model.Playlist;
import com.alirezaahmadi.videoplayer.model.PlaylistItem;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PlaylistItemRepositoryTest {

    private VideoPlayerDb db;
    private PlaylistItemDao playlistItemDao;
    private PlaylistItemRepository playlistItemRepository;

    @Before
    public void init(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, VideoPlayerDb.class).build();
        playlistItemDao = db.playlistItemDao();
        PlaylistDao playlistDao = db.playlistDao();
        playlistDao.insertPlaylist(new Playlist("Playlist one"));
        playlistItemRepository = new PlaylistItemRepository(playlistItemDao);
    }

    @Test
    public void whenCreated_playlistHasNoItems(){
        List<PlaylistItem> playlistItems = playlistItemDao.loadPlayListItems(1).blockingFirst();
        assertEquals(0, playlistItems.size());
    }

    @Test
    public void addingItem_addsTheItemToDb(){
        List<Integer> videoIds = new ArrayList<>();
        videoIds.add(1);
        videoIds.add(2);
        videoIds.add(3);

        playlistItemRepository.addVideoToPlayList(videoIds, 1).blockingGet();

        List<PlaylistItem> playlistItems = playlistItemDao.loadPlayListItems(1).blockingFirst();
        assertEquals(3, playlistItems.size());
        assertEquals(1, playlistItems.get(0).getVideoId());
        assertEquals(2, playlistItems.get(1).getVideoId());
        assertEquals(3, playlistItems.get(2).getVideoId());
    }

    @Test
    public void removingItems_removeThemFromDb(){
        List<Integer> videoIds = new ArrayList<>();
        videoIds.add(1);
        videoIds.add(2);
        videoIds.add(3);
        playlistItemRepository.addVideoToPlayList(videoIds, 1).blockingGet();

        List<Integer> videoIdsToDelete = new ArrayList<>();
        videoIdsToDelete.add(2);
        playlistItemRepository.deleteVideosFromPlayList(videoIdsToDelete, 1).blockingGet();

        List<PlaylistItem> playlistItems = playlistItemDao.loadPlayListItems(1).blockingFirst();
        assertEquals(2, playlistItems.size());
        assertEquals(1, playlistItems.get(0).getVideoId());
        assertEquals(3, playlistItems.get(1).getVideoId());
    }

}