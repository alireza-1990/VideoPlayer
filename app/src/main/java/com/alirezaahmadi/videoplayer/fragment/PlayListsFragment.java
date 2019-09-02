package com.alirezaahmadi.videoplayer.fragment;


import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alirezaahmadi.videoplayer.R;
import com.alirezaahmadi.videoplayer.adapter.PlaylistAdapter;
import com.alirezaahmadi.videoplayer.util.NavigationController;
import com.alirezaahmadi.videoplayer.viewmodel.DaggerViewModelFactory;
import com.alirezaahmadi.videoplayer.viewmodel.PlaylistViewModel;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.AndroidSupportInjection;

public class PlayListsFragment extends Fragment implements PlaylistAdapter.PlaylistClickListener, PlaylistAdapter.PlaylistNewItemListener, PlaylistAdapter.PlaylistDeleteListener {

    @Inject DaggerViewModelFactory viewModelFactory;
    @Inject @Named("withDelete") PlaylistAdapter adapter;
    @Inject NavigationController navigationController;

    private RecyclerView recyclerView;
    private PlaylistViewModel viewModel;
    private RecyclerView.LayoutManager layoutManager;

    public static PlayListsFragment newInstance() {
        return new PlayListsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaylistViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_lists, container, false);
        recyclerView = view.findViewById(R.id.playlist_recycler_view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setNestedScrollingEnabled(false); //todo know why?

        viewModel.getPlaylists().observe(this, playlists -> adapter.setPlaylists(playlists));
        adapter.setPlaylistClickListener(this);
        adapter.setPlaylistNewItemListener(this);
        adapter.setPlaylistDeleteListener(this);

        return view;
    }

    @Override
    public void onNewPlaylistAdded(String playlistTitle) {
        viewModel.addPlayList(playlistTitle);
    }

    @Override
    public void onItemClicked(int playlistId) {
        navigationController.navigateToPlaylistDetail(playlistId);
    }

    @Override
    public void onDeleteClicked(int playlistId) {
        viewModel.deletePlayList(playlistId);
    }
}
