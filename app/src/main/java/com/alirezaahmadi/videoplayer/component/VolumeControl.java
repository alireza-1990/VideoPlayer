package com.alirezaahmadi.videoplayer.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alirezaahmadi.videoplayer.R;

public class VolumeControl extends LinearLayout  {

    private View upBtn;
    private View downBtn;

    public VolumeControl(Context context) {
        super(context);
        initializeViews(context);
    }

    public VolumeControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public VolumeControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.componet_volume_control, this);

        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        upBtn = findViewById(R.id.volume_up);
        downBtn = findViewById(R.id.volume_down);
    }

    public void setUpClickListener(View.OnClickListener upClickListener){
        upBtn.setOnClickListener(upClickListener);
    }

    public void setDownClickListener(View.OnClickListener downClickListener){
        downBtn.setOnClickListener(downClickListener);
    }
}
