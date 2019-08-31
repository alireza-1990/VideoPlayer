package com.alirezaahmadi.videoplayer;

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

    public static void assertEqualsVideoLists(List<Video> videoList1, List<Video> videoList2){
        for(int i = 0; i < videoList1.size(); i++) {
            assertEquals(videoList1.get(i).getId(), videoList2.get(i).getId());
            assertEquals(videoList1.get(i).getTitle(), videoList2.get(i).getTitle());
            assertEquals(videoList1.get(i).getVideoPath(), videoList2.get(i).getVideoPath());
            assertEquals(videoList1.get(i).getThumbnailPath(), videoList2.get(i).getThumbnailPath());
        }
    }

}
