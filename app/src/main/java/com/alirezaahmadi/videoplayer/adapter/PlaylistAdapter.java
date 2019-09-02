package com.alirezaahmadi.videoplayer.adapter;

import android.app.Application;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alirezaahmadi.videoplayer.R;
import com.alirezaahmadi.videoplayer.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final int TYPE_NEW = 1;
    private static final int TYPE_ITEM = 2;

    public interface PlaylistAdapterListener {
        void onNewPlaylistAdded(String playlistTitle);
        void onItemClicked(int playlistId);
        void onDeleteClicked(int playlistId);
    }

    private Application application;
    private List<Playlist> playlists;
    private PlaylistAdapterListener listener;
    private DeleteClickListener deleteClickListener;

    private boolean showDelete;

    public PlaylistAdapter(Application application, boolean showDelete) {
        this.application = application;
        this.showDelete = showDelete;
        playlists = new ArrayList<>();
        deleteClickListener = new DeleteClickListener();
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
        notifyDataSetChanged();
    }

    public void setListener(PlaylistAdapterListener listener) {
        this.listener = listener;
    }

    class NewPlaylistHolder extends RecyclerView.ViewHolder {
        private EditText inputET;

        NewPlaylistHolder(View itemView) {
            super(itemView);
            this.inputET = itemView.findViewById(R.id.item_new_playlist_input);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView titleTV;
        private ImageView deleteIcon;

        ItemHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.item_playlist_title);
            deleteIcon = itemView.findViewById(R.id.item_playlist_delete);
        }

        void bind(Playlist playlist){
            titleTV.setText(playlist.getTitle());
            itemView.setTag(playlist.getId());
            deleteIcon.setTag(playlist.getId());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_NEW: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_play_list, parent, false);
                NewPlaylistHolder newPlaylistHolder = new NewPlaylistHolder(view);
                newPlaylistHolder.inputET.setOnEditorActionListener(this);
                return newPlaylistHolder;
            }

            case TYPE_ITEM: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
                ItemHolder itemHolder = new ItemHolder(view);
                itemHolder.itemView.setOnClickListener(this);
                itemHolder.deleteIcon.setOnClickListener(deleteClickListener);

                if(!showDelete)
                    itemHolder.deleteIcon.setVisibility(View.GONE);

                return itemHolder;
            }

            default:
                throw new IllegalArgumentException("wrong view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            ItemHolder itemHolder = (ItemHolder) holder;
            Playlist playlist = getPlaylist(position);
            itemHolder.bind(playlist);
        }
    }

    private Playlist getPlaylist(int position){
        return playlists.get(position -1);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_NEW : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return playlists.size() + 1;
    }

    @Override
    public void onClick(View v) {
        int playlistId = (int) v.getTag();
        if(listener != null)
            listener.onItemClicked(playlistId);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String text = textView.getText().toString();
            if(text.trim().isEmpty())
                Toast.makeText(application, R.string.error_empty_playlist_name, Toast.LENGTH_SHORT).show();

            else if(listener != null)
                listener.onNewPlaylistAdded(text);

            textView.setText(null);
            return true;
        }
        return false;
    }

    private class DeleteClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int playlistId = (int) v.getTag();
            if(listener != null)
                listener.onDeleteClicked(playlistId);
        }
    }
}
