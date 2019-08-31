package com.alirezaahmadi.videoplayer.fragment;


import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alirezaahmadi.videoplayer.R;
import com.alirezaahmadi.videoplayer.adapter.VideoAdapter;
import com.alirezaahmadi.videoplayer.util.NavigationController;
import com.alirezaahmadi.videoplayer.viewmodel.DaggerViewModelFactory;
import com.alirezaahmadi.videoplayer.viewmodel.VideoListViewModel;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class VideoListFragment extends Fragment implements VideoAdapter.VideoClickListener, ActionMode.Callback, VideoAdapter.SelectionModeListener, DialogDismissListener {

    @Inject DaggerViewModelFactory viewModelFactory;
    @Inject VideoAdapter adapter;
    @Inject NavigationController navigationController;

    RecyclerView recyclerView;

    VideoListViewModel viewModel;
    LinearLayoutManager layoutManager;
    ActionMode actionMode;

    public static VideoListFragment newInstance(){
        return new VideoListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        recyclerView = view.findViewById(R.id.video_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        viewModel.getVideoList().observe(this, videos -> adapter.setVideoList(videos));

        adapter.setVideoClickListener(this);
        adapter.setSelectionModeListener(this);

        return view;
    }

    @Override
    public void onVideoClicked(int videoId) {
        navigationController.navigateToPlayer(videoId);
    }

    @Override
    public void onSelectionModeChanged(boolean selectionState) {
        if(selectionState)
            actionMode = getActivity().startActionMode(this);

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.video_context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if(item.getItemId() == R.id.add_to_play_list)
            addSelectedItemsToPlayList();

        return true;
    }

    private void addSelectedItemsToPlayList() {
        showAddToPlaylistDialog();
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        adapter.cancelCelectionMode();
    }

    private void showAddToPlaylistDialog() {
        AddToPlaylistDialogFragment fragment = AddToPlaylistDialogFragment.newInstance(adapter.getSelectedList());
        fragment.show(getChildFragmentManager(), "addPlaylistDialog");
    }

    public void closeActionMode(){
        if(actionMode != null)
            actionMode.finish();
    }

    @Override
    public void onDialogDismissed() {
        closeActionMode();
    }
}