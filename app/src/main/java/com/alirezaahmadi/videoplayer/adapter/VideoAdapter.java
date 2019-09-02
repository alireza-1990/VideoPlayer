package com.alirezaahmadi.videoplayer.adapter;

import android.app.Application;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alirezaahmadi.videoplayer.R;
import com.alirezaahmadi.videoplayer.model.Video;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

//todo add DiffUtil for better performance
public class VideoAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

    public interface VideoClickListener {
        void onVideoClicked(int videoId);
    }

    public interface VideoLongClickListener {
        void onVideoLongClick(int videoId);
    }

    private Application application;
    private List<Video> videoList;
    private List<Integer> selectedList;
    private VideoClickListener videoClickListener;
    private VideoLongClickListener videoLongClickListener;

    @Inject
    public VideoAdapter(Application application) {
        this.application = application;
        this.videoList = new ArrayList<>();
        this.selectedList = new ArrayList<>();
    }

    public void setVideoClickListener(VideoClickListener videoClickListener) {
        this.videoClickListener = videoClickListener;
    }

    public void setVideoLongClickListener(VideoLongClickListener videoLongClickListener) {
        this.videoLongClickListener = videoLongClickListener;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    public void setSelectedList(List<Integer> selectedList) {
        this.selectedList = selectedList;
        notifyDataSetChanged();
    }

    private class VideoHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnailIV;
        private TextView titleTV;

        VideoHolder(View itemView) {
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
            Picasso.get().load(new File(video.getThumbnailPath())).into(videoHolder.thumbnailIV);
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

        if(videoLongClickListener != null)
            videoLongClickListener.onVideoLongClick(videoId);

        return true;
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    @Override
    public void onClick(View v) {
        int videoId = (int) v.getTag();

        if(videoClickListener != null)
            videoClickListener.onVideoClicked(videoId);
    }

}
