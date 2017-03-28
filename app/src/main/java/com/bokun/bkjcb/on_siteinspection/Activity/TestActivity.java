package com.bokun.bkjcb.on_siteinspection.Activity;

import android.content.Intent;

import com.bokun.bkjcb.on_siteinspection.R;

/**
 * Created by BKJCB on 2017/3/24.
 */

public class TestActivity extends BaseActivity {

    @Override
    protected void initView() {
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void findView() {
        Intent intent = new Intent();
        intent.putExtra("name","dengshuai");
        setResult(1,intent);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {

    }
}
