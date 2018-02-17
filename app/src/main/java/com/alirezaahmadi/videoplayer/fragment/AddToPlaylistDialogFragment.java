package com.alirezaahmadi.videoplayer.fragment;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alirezaahmadi.videoplayer.R;
import com.alirezaahmadi.videoplayer.adapter.PlaylistAdapter;
import com.alirezaahmadi.videoplayer.util.CommonUtil;
import com.alirezaahmadi.videoplayer.viewmodel.DaggerViewModelFactory;
import com.alirezaahmadi.videoplayer.viewmodel.PlaylistViewModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.AndroidSupportInjection;

public class AddToPlaylistDialogFragment extends DialogFragment implements PlaylistAdapter.PlaylistAdapterListener {
    private static final String ARG_VIDEO_IDS = "video_ids";

    @Inject DaggerViewModelFactory viewModelFactory;
    @Inject @Named("withoutDelete") PlaylistAdapter adapter;

    RecyclerView recyclerView;

    PlaylistViewModel viewModel;
    RecyclerView.LayoutManager layoutManager;

    public static AddToPlaylistDialogFragment newInstance(List<Integer> videoIds){
        AddToPlaylistDialogFragment fragment = new AddToPlaylistDialogFragment();
        Bundle args = new Bundle();
        args.putIntArray(ARG_VIDEO_IDS, CommonUtil.convertIntegerListToArray(videoIds));
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaylistViewModel.class);

        if(getArguments() == null ||
                !getArguments().containsKey(ARG_VIDEO_IDS))
            throw new IllegalStateException("fragment needs list of videos to work");

        List<Integer> videoIds = CommonUtil.convertIntArrayToList(getArguments().getIntArray(ARG_VIDEO_IDS));
        viewModel.setVideoIdsToAdd(videoIds);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_to_playlist_dialog, container, false);
        recyclerView = view.findViewById(R.id.add_to_play_list_recycler_view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        viewModel.getPlaylists().observe(this, playlists -> adapter.setPlaylists(playlists));

        adapter.setListener(this);

        return view;
    }

    @Override
    public void onNewPlaylistAdded(String playlistTitle) {
        viewModel.addPlayList(playlistTitle);
    }

    @Override
    public void onItemClicked(int playlistId) {
        viewModel.addVideosToPlaylist(viewModel.getVideoIdsToAdd(), playlistId);
        Toast.makeText(getActivity(), R.string.successfuly_added_to_playlist, Toast.LENGTH_LONG).show();

        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(getParentFragment() instanceof DialogDismissListener)
            ((DialogDismissListener) getParentFragment()).onDialogDismissed();

        super.onDismiss(dialog);
    }

    @Override
    public void onDeleteClicked(int playlistId) {

    }


}
