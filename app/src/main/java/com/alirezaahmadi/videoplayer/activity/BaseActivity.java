package com.alirezaahmadi.videoplayer.activity;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected void startActivityInitProcess(){
        onCreateViewInstances();
        onBindViewModel();
        onViewInit();
        onActivityInitFinished();
    }

    protected void onCreateViewInstances(){
        //does nothing by default
    }

    protected void onBindViewModel(){
        //does nothing by default
    }

    protected void onViewInit(){
        //does nothing by default
    }

    protected void onActivityInitFinished() {
        //does nothing by default
    }
}
