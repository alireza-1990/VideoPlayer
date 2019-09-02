package com.alirezaahmadi.videoplayer.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.alirezaahmadi.videoplayer.R;
import com.alirezaahmadi.videoplayer.model.Video;
import com.alirezaahmadi.videoplayer.component.VolumeControl;
import com.alirezaahmadi.videoplayer.viewmodel.DaggerViewModelFactory;
import com.alirezaahmadi.videoplayer.viewmodel.PlayerViewModel;

import javax.inject.Inject;

import at.grabner.circleprogress.CircleProgressView;
import dagger.android.AndroidInjection;

public class PlayerActivity extends BaseActivity {
    private static final String ARG_VIDEO_ID = "video_id";
    private static final String ARG_PLAYLIST_ID = "playlist_id";

    @Inject DaggerViewModelFactory viewModelFactory;

    VolumeControl volumeControl;
    VideoView videoView;
    CircleProgressView volumeProgress;

    PlayerViewModel viewModel;
    MediaController mediaController;

    public static Intent createIntent(Context context, int videoId){
        Intent intent = createIntent(context, videoId, -1);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent createIntent(Context context, int videoId, int playlistId){
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(ARG_VIDEO_ID, videoId);
        intent.putExtra(ARG_PLAYLIST_ID, playlistId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayerViewModel.class);
        if(!getIntent().hasExtra(ARG_VIDEO_ID)){
            throw new IllegalStateException("Video id is required");
        }

        int videoId = getIntent().getIntExtra(ARG_VIDEO_ID, -1);
        int playlistId = getIntent().getIntExtra(ARG_PLAYLIST_ID, -1);

        viewModel.init(videoId, playlistId);

        volumeControl = findViewById(R.id.player_volume_control);
        videoView  = findViewById(R.id.video_view);
        volumeProgress  = findViewById(R.id.volume_progress);

        viewModel.getCurrentVideo().observe(this, this::playVideo);
        viewModel.getVolumeValue().observe(this, value -> updateVolumeProgress(value));

        volumeControl.setUpClickListener(v -> viewModel.volumeUp());
        volumeControl.setDownClickListener(v -> viewModel.volumeDown());
        volumeProgress.setAlpha(0);

        initVideoView();
    }

    private void initVideoView(){
        mediaController = new MediaController(this, false);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        mediaController.setPrevNextListeners(v -> viewModel.next(),
                v -> viewModel.prev());

        mediaController.setDrawingCacheBackgroundColor(ContextCompat.getColor(this, R.color.white));
        mediaController.setBackgroundColor(ContextCompat.getColor(this, R.color.player_control_background));

        videoView.setOnCompletionListener(mp -> viewModel.next());
        videoView.setMediaController(mediaController);
    }

    /**
     * every time volume gets updated a progress will pop on on screen
     * and fades out after few seconds.
     **/
    AnimatorSet animatorSet = new AnimatorSet();
    private void updateVolumeProgress(int volumeValue){

        //do not show the volume progress on first time.
        if(volumeProgress.getCurrentValue() == -1){
            volumeProgress.setValue(volumeValue);
            return;
        }

        animatorSet.cancel();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(volumeProgress.getAlpha(), 1f);
        valueAnimator.setDuration(400);
        valueAnimator.addUpdateListener(animation ->
                volumeProgress.setAlpha((float) animation.getAnimatedValue()));

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                volumeProgress.setValueAnimated(volumeValue, 150);
            }
        });

        ValueAnimator fadeAnimator = ValueAnimator.ofFloat(1f, 0);
        fadeAnimator.setDuration(400);
        fadeAnimator.addUpdateListener(animation ->
                volumeProgress.setAlpha((float) animation.getAnimatedValue()));

        animatorSet.play(valueAnimator);
        animatorSet.play(fadeAnimator).after(4000);
        animatorSet.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
            viewModel.volumeDown();

        else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP)
            viewModel.volumeUp();

        return super.onKeyDown(keyCode, event);
    }

    private void playVideo(Video video){
        Uri videoUri = Uri.parse(video.getVideoPath());
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();
    }
}
