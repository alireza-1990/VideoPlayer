package com.alirezaahmadi.videoplayer.adapter;

import android.app.Application;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alirezaahmadi.videoplayer.R;
import com.alirezaahmadi.videoplayer.fragment.PlayListsFragment;
import com.alirezaahmadi.videoplayer.fragment.VideoListFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private Application application;

    /**
     * Stores fragment reference so activity can latter on call fragment methods
     * when necessary.
     */
    private VideoListFragment videoListFragment; //todo is this a memory leak? go throw videomodel itself?

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
