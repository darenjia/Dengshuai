package com.bokun.bkjcb.on_siteinspection.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bokun.bkjcb.on_siteinspection.Fragment.CheckItemFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.LastFragment;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class SecurityCheckActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private ImageButton btn_forward;
    private ImageButton btn_next;
    private TextView page_num;
    private List<Fragment> fragments;
    private List<String> contents;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        setContentView(R.layout.activity_securitycheck);
        toolbar = (Toolbar) findViewById(R.id.toolbar_secAct);
        toolbar.setTitle("安全检查");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        Utils.initSystemBar(this, toolbar);
    }

    @Override
    protected void findView() {
        viewPager = (ViewPager) findViewById(R.id.check_viewpager);
        btn_forward = (ImageButton) findViewById(R.id.btn_forward);
        btn_next = (ImageButton) findViewById(R.id.btn_next);
        page_num = (TextView) findViewById(R.id.txt_page);

        fragments = new ArrayList<>();
        contents = getCheckItems();
        for (int i = 0; i < contents.size(); i++) {
            Fragment fragment = null;
            fragment = new CheckItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("content", contents.get(i));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        Fragment fragment = new LastFragment();
        fragments.add(fragment);
        LogUtil.logI("size:" + fragments.size());
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
//        viewPager.setCurrentItem(13);
//        FragmentAdapter adapter = new FragmentAdapter(context, getCheckItems());
//        viewPager.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        viewPager.addOnPageChangeListener(this);
        btn_next.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        page_num.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        page_num.setText(1 + "/" + fragments.size());
    }

    public static void ComeToSecurityCheckActivity(Activity activity) {
        Intent intent = new Intent(activity, SecurityCheckActivity.class);
        activity.startActivity(intent);
    }

    public List<String> getCheckItems() {
        List checkItems = new ArrayList<>();
        JSONObject object = null;
        StringBuilder builder = new StringBuilder();
        try {
            InputStream inputStream = getAssets().open("ChecItemskDetail");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            object = new JSONObject(builder.toString());
            JSONArray array = object.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                checkItems.add(obj.getString("description"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return checkItems;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        /*LogUtil.logI("position:" + position);
        if (mMenu != null) {
            if (position != 14) {
                mMenu.findItem(R.id.btn_submit).setVisible(false);
            }
        }
        LogUtil.logI("position:" + mMenu.hasVisibleItems());*/
        page_num.setText((position + 1) + "/" + fragments.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.check_activity_menu, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_submit) {
            Toast.makeText(context, "正在提交", Toast.LENGTH_SHORT).show();
            CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
            daolmpl.addCheckResult(new CheckResult());
            daolmpl.colseDateBase();
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == btn_forward.getId()) {
            viewPager.arrowScroll(View.FOCUS_LEFT);
            LogUtil.logI("click forward");
        } else if (id == btn_next.getId()) {
            viewPager.arrowScroll(View.FOCUS_RIGHT);
            LogUtil.logI("click forward");
        } else if (id == page_num.getId()) {
            String[] title = new String[16];
            contents.toArray(title);
            title[15] = "处理意见";
            new AlertView(null, null, "取消", null,
                    title, this, AlertView.Style.ActionSheet,
                    new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            viewPager.setCurrentItem(position, false);
                        }
                    }).show();
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("检查还未完成，是否退出？")
                .setCancelable(true)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
