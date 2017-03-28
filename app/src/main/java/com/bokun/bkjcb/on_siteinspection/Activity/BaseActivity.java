package com.bokun.bkjcb.on_siteinspection.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Utils.AppManager;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseActivity extends AppCompatActivity {

    public Context context;
    private long firstTime = 0;
    private long lastTime = 0;
    private static final int PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
        AppManager.getAppManager().addActivity(this);
        context = this;
        initView();
        findView();
        setListener();
        checkPermissions();

    }

    //设置页面layout
    protected abstract void initView();

    //初始化控件
    protected abstract void findView();

    //设置控件监听
    protected abstract void setListener();

    //获取数据
    protected abstract void loadData();

    @Override
    protected void onDestroy() {

        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (firstTime == 0) {
            firstTime = new Date().getTime();
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            lastTime = new Date().getTime();
            if (lastTime - firstTime < 3 * 1000) {
                AppManager.getAppManager().finishActivity();
            }
        }
        clearFirstTime();
    }

    private void clearFirstTime() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                firstTime = 0;
            }
        }, 3 * 1000);
    }

    public void checkPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                },
                PERMISSIONS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
