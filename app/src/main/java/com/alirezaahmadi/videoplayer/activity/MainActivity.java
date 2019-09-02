package com.alirezaahmadi.videoplayer.activity;

import android.Manifest;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.alirezaahmadi.videoplayer.R;
import com.alirezaahmadi.videoplayer.adapter.HomePagerAdapter;
import com.alirezaahmadi.videoplayer.fragment.VideoListFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import dagger.android.AndroidInjection;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final int RC_READ_EXTERNAL_STORAGE = 101;

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;

    HomePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivityInitProcess();
    }

    @Override
    protected void onCreateViewInstances() {
        tabLayout = findViewById(R.id.main_tab_layout);
        viewPager = findViewById(R.id.main_view_pager);
        toolbar = findViewById(R.id.main_toolbar);
    }

    @Override
    protected void onViewInit() {
        adapter = new HomePagerAdapter(getApplication(), getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setVisibility(View.GONE);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //hide context menu when switching between tabs
                VideoListFragment fragment = adapter.getVideoListFragment();
                if(fragment != null)
                    fragment.closeActionMode();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initToolbar();
    }

    @Override
    protected void onActivityInitFinished() {
        requestPermissionsIfNecessary();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(null);
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Ask user for runtime permission. If fails offers user retry option but
     * stop him/her from going to app since accessing storage is absolutely
     * necessary for app to work.
     * Also it gives user an option to go to settings in case he/she denied
     * the permission and clicked on don't ask again.
     */
    private void requestPermissionsIfNecessary(){
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            viewPager.setVisibility(View.VISIBLE);

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.read_storage_rationale),
                    RC_READ_EXTERNAL_STORAGE, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            showPermissionRetryDialog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            requestPermissionsIfNecessary();

        }
    }

    private void showPermissionRetryDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.access_denied)
                .setMessage(R.string.permission_denied_description)
                .setPositiveButton(R.string.retry, (dialog, which) -> {
                    requestPermissionsIfNecessary();
                })
                .setNegativeButton(R.string.exit, (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
