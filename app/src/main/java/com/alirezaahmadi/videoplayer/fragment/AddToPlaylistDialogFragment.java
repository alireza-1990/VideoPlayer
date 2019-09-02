package com.alirezaahmadi.videoplayer.fragment;

import android.app.Dialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AddToPlaylistDialogFragment extends DialogFragment implements PlaylistAdapter.PlaylistClickListener, PlaylistAdapter.PlaylistNewItemListener {
    private static final String ARG_VIDEO_IDS = "video_ids";

    @Inject DaggerViewModelFactory viewModelFactory;
    @Inject @Named("withoutDelete") PlaylistAdapter adapter;

    private RecyclerView recyclerView;
    private PlaylistViewModel viewModel;
    private RecyclerView.LayoutManager layoutManager;

    static AddToPlaylistDialogFragment newInstance(List<Integer> videoIds){
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

        adapter.setPlaylistClickListener(this);
        adapter.setPlaylistNewItemListener(this);

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

}
