package com.alirezaahmadi.videoplayer.activity;

import dagger.android.support.DaggerAppCompatActivity;

abstract public class BaseActivity extends DaggerAppCompatActivity {

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
