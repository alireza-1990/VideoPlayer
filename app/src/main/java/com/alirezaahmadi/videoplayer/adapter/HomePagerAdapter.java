package com.alirezaahmadi.videoplayer.adapter;

import android.app.Application;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alirezaahmadi.videoplayer.R;
import com.alirezaahmadi.videoplayer.fragment.PlayListsFragment;
import com.alirezaahmadi.videoplayer.fragment.VideoListFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private Application application;

    /**
     * Stores fragment reference so activity can latter on call fragment methods
     * when necessary.
     */
    private VideoListFragment videoListFragment;

    public HomePagerAdapter(Application application, FragmentManager fm) {
        super(fm);
        this.application = application;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                videoListFragment = VideoListFragment.newInstance();
                return videoListFragment;

            case 1:
                return PlayListsFragment.newInstance();

            default:
                throw new IllegalArgumentException("wrong position");
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return application.getString(R.string.all_videos);

            case 1:
                return application.getString(R.string.palylists);

            default:
                throw new IllegalArgumentException("wrong position");
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public VideoListFragment getVideoListFragment(){
        return videoListFragment;
    }
}
