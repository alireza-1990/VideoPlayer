package com.alirezaahmadi.videoplayer.adapter;

import android.app.Application;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alirezaahmadi.videoplayer.R;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.repository.VideoRepository;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VideoAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

    public interface VideoClickListener {
        void onVideoClicked(int videoId);
    }

    public interface SelectionModeListener {
        void onSelectionModeChanged(boolean selectionState);
    }

    private Application application;
    private List<Video> videoList;
    private List<Integer> selectedList;
    private VideoClickListener videoClickListener;
    private SelectionModeListener selectionModeListener;
    private boolean selectionMode = false;

    @Inject
    public VideoAdapter(Application application) {
        this.application = application;
        this.videoList = new ArrayList<>();
        this.selectedList = new ArrayList<>();
    }

    public void setVideoClickListener(VideoClickListener videoClickListener) {
        this.videoClickListener = videoClickListener;
    }

    public void setSelectionModeListener(SelectionModeListener selectionModeListener) {
        this.selectionModeListener = selectionModeListener;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    public void cancelCelectionMode(){
        selectedList.clear();
        selectionMode = false;
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedList() {
        return selectedList;
    }

    private class VideoHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnailIV;
        private TextView titleTV;

        public VideoHolder(View itemView) {
            super(itemView);
            thumbnailIV = itemView.findViewById(R.id.item_video_thumbnail);
            titleTV = itemView.findViewById(R.id.item_video_title);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        VideoHolder videoHolder = new VideoHolder(view);
        videoHolder.itemView.setOnClickListener(this);
        return videoHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoHolder videoHolder = (VideoHolder) holder;
        Video video = videoList.get(position);
        videoHolder.titleTV.setText(video.getTitle());
        if(video.getThumbnailPath() != null)
            Picasso.with(application).load(new File(video.getThumbnailPath())).into(videoHolder.thumbnailIV);
        videoHolder.itemView.setTag(video.getId());
        videoHolder.itemView.setOnLongClickListener(this);

        if(selectedList.contains(video.getId())){
            videoHolder.itemView.setBackgroundColor(ContextCompat.getColor(application, R.color.video_item_selected_background));

        } else {
            videoHolder.itemView.setBackgroundColor(ContextCompat.getColor(application, R.color.white));

        }
    }

    @Override
    public boolean onLongClick(View v) {
        int videoId = (int) v.getTag();
        selectedList.add(videoId);

        if(!selectionMode){
            selectionMode = true;

            if(selectionModeListener != null)
                selectionModeListener.onSelectionModeChanged(true);

        }

        notifyDataSetChanged();

        return false;
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    @Override
    public void onClick(View v) {
        int videoId = (int) v.getTag();

        if(selectionMode){
            changeSelectionState(videoId);
            return;
        }

        if(videoClickListener != null)
            videoClickListener.onVideoClicked(videoId);
    }

    private void changeSelectionState(int videoId) {
        if(selectedList.contains(videoId))
            selectedList.remove(selectedList.indexOf(videoId));

        else
            selectedList.add(videoId);

        notifyDataSetChanged();
    }

}
