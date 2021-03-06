package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bokun.bkjcb.on_siteinspection.Activity.SecurityCheckActivity;
import com.bokun.bkjcb.on_siteinspection.Adapter.ExpandableListViewAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DateUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.bokun.bkjcb.on_siteinspection.View.ConstructionDetailView;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/4/7.
 */

public class CheckPlanFragment extends MainFragment implements RequestListener {


    private ArrayList<CheckPlan> checkPlans;
    private ArrayList<ArrayList<CheckPlan>> constuctions;
    private ExpandableListView listview;
    private ExpandableListViewAdapter adapter;
    private AlertDialog dialog;
    public static int DATA_CHANGED = 1;
    public static int DATA_UNCHANGED = 0;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LogUtil.logI("加载fragment");
        View view = View.inflate(context, R.layout.content_plan, null);
        initPlanLayout(view);
        setExpandableListView(view);
        return view;
    }

    private void initPlanLayout(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRecycler));
        HttpRequestVo requestVo = new HttpRequestVo("", "");
        HttpManager manager = new HttpManager(context, this, requestVo);
        manager.postRequest();


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkPlans = DateUtil.queryCheckPlan(context);
                        Snackbar.make(refreshLayout, "刷新完成", Snackbar.LENGTH_LONG).show();
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    private void setExpandableListView(View view) {
        listview = (ExpandableListView) view.findViewById(R.id.plan_list);
        int width = Utils.getWindowWidthOrHeight(context, "Width");
        int left = width - (width / 10);
        int right = width - (width / 10) + (width / 20);
        LogUtil.logI("width:" + width + " left:" + left + " right:" + right);
        listview.setIndicatorBounds(left, right);
        checkPlans = DateUtil.queryCheckPlan(context);
        if (checkPlans.size() == 0) {
            for (int i = 0; i < 3; i++) {
                CheckPlan checkPlan = new CheckPlan();
                checkPlan.setIdentifier(21545150 + i);
                checkPlan.setName("检查计划" + i);
                checkPlan.setState(i);
                DateUtil.insertCheckPlan(context, checkPlan);
                checkPlans.add(checkPlan);
            }
        }
        constuctions = new ArrayList<>();
        constuctions.add(checkPlans);
        constuctions.add(checkPlans);
        constuctions.add(checkPlans);
        LogUtil.logI("current:" + System.currentTimeMillis());
        adapter = new ExpandableListViewAdapter(context, checkPlans, constuctions);
        listview.setAdapter(adapter);
        LogUtil.logI("currentTime:" + System.currentTimeMillis());
        listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                LogUtil.logI("click:" + groupPosition);
                return false;
            }
        });
        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                LogUtil.logI("click" + childPosition + ":" + groupPosition);
                createDailog(constuctions.get(groupPosition).get(childPosition), groupPosition, childPosition);
                return true;
            }
        });
    }

    @Override
    public void action(int i, Object object) {
        mHandler.sendEmptyMessage(i);
    }

    private void createDailog(final CheckPlan checkPlan, final int groupPosition, final int childPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        ConstructionDetailView constructionDetailView = new ConstructionDetailView(context, checkPlan);
        View view = constructionDetailView.getConstructionDetailView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_check) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("checkplan", checkPlan);
                    bundle.putInt("groupPosition", groupPosition);
                    bundle.putInt("childPosition", childPosition);
                    bundle.putInt("state", checkPlan.getState());
//                    SecurityCheckActivity.ComeToSecurityCheckActivity(context, bundle);
                    Intent intent = new Intent(context, SecurityCheckActivity.class);
                    intent.putExtras(bundle);
                    CheckPlanFragment.this.startActivityForResult(intent, 1);
                }
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onStart() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == DATA_CHANGED) {
                refreshData(data);
            }
        }
    }

    private void refreshData(Intent data) {
        int groupPosition = data.getExtras().getInt("groupPosition");
        int childPosition = data.getExtras().getInt("childPosition");
        int state = data.getExtras().getInt("state");
        LogUtil.logI("计划状态题改变，刷新列表数据" + state);
        constuctions.get(groupPosition).get(childPosition).setState(state);
        adapter.notifyDataSetChanged();
        listview.collapseGroup(groupPosition);
        listview.expandGroup(groupPosition);
    }
}
