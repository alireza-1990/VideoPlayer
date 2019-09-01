package com.alirezaahmadi.videoplayer;

import com.alirezaahmadi.videoplayer.model.Playlist;
import com.alirezaahmadi.videoplayer.model.PlaylistItem;
import com.alirezaahmadi.videoplayer.model.Video;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestUtil {

    public static List<Video> getVideoList() {
        List<Video> videoList = new ArrayList<>();

        Video video1 = new Video();
        video1.setTitle("Lovely Cat");
        video1.setId(1);
        video1.setVideoPath("/path/to/the/video/lovelyCat.mp4");
        video1.setThumbnailPath("/path/to/the/thumbnail/cat.jpg");
        videoList.add(video1);

        Video video2 = new Video();
        video2.setTitle("Baby cat");
        video2.setId(2);
        video2.setVideoPath("/path/to/the/other/video/whatever.whatever");
        video2.setThumbnailPath("/path/to/the/other/thumbnail/whatever.whatever");
        videoList.add(video2);

        return videoList;
    }

    public static List<Playlist> getMockPlaylists(){
        List<Playlist> playlists = new ArrayList<>();
        Playlist playlist1 = new Playlist("Playlist one");
        playlist1.setId(1);
        playlists.add(playlist1);
        return playlists;
    }

    public static List<PlaylistItem> getMockPlaylistItems(){
        List<PlaylistItem> playlistItems = new ArrayList<>();
        PlaylistItem playListItem1 = new PlaylistItem();
        playListItem1.setId(1);
        playListItem1.setPlaylistId(1);
        playListItem1.setVideoId(2);
        playlistItems.add(playListItem1);
        return playlistItems;
    }

    public static void assertEqualsVideoLists(List<Video> videoList1, List<Video> videoList2){
        for(int i = 0; i < videoList1.size(); i++) {
            assertEquals(videoList1.get(i).getId(), videoList2.get(i).getId());
            assertEquals(videoList1.get(i).getTitle(), videoList2.get(i).getTitle());
            assertEquals(videoList1.get(i).getVideoPath(), videoList2.get(i).getVideoPath());
            assertEquals(videoList1.get(i).getThumbnailPath(), videoList2.get(i).getThumbnailPath());
        }
    }

    public static void assertEqualsPlayLists(List<Playlist> playlists1, List<Playlist> playlists2){
        for(int i = 0; i < playlists1.size(); i++) {
            assertEquals(playlists1.get(i).getId(), playlists2.get(i).getId());
            assertEquals(playlists1.get(i).getTitle(), playlists2.get(i).getTitle());
        }
    }

}
