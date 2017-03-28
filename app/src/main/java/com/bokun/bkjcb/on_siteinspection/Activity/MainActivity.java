package com.bokun.bkjcb.on_siteinspection.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.AppManager;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.View.ConstructionDetailView;

import java.util.HashMap;
import java.util.Map;

/*
* MianActivity
* */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, RequestListener {

    public String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private ConstraintLayout contentView;
    private SwipeRefreshLayout refreshLayout;
    private Map<String, View> viewMap;
    final String[] name = {"检查计划1", "检查计划2", "检查计划3"};
    final String[][] ids = {{"工程1", "工程2", "工程3"}, {"工程1", "工程2", "工程3"}, {"工程1", "工程2", "工程3"}};
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RequestListener.EVENT_NOT_NETWORD:
                    Snackbar.make(contentView, "", Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();
                    break;
                case RequestListener.EVENT_CLOSE_SOCKET:
                    Snackbar.make(contentView, "网络错误！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_NETWORD_EEEOR:
                    Snackbar.make(contentView, "请确认网络是否可用！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_GET_DATA_EEEOR:
                    Snackbar.make(contentView, "服务器错误，请稍后再试！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_GET_DATA_SUCCESS:
                    break;

            }
            refreshLayout.setRefreshing(false);
        }
    };
    private Toolbar toolbar;
    public ExpandableListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    protected void initView() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

//        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewMap = new HashMap<>();
    }

    @Override
    protected void findView() {
        contentView = (ConstraintLayout) findViewById(R.id.content_main);
//        contentView = (ConstraintLayout) View.inflate(this,R.layout.content_main,null);

        click_first_menu(name, ids);
    }

    @Override
    protected void setListener() {
        listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                LogUtil.logD(TAG, "click:" + groupPosition);
                return false;
            }
        });
        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                LogUtil.logD(TAG, "click" + childPosition + ":" + groupPosition);
                createDailog().show();
                return false;
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_check_plan) {
            toolbar.setTitle("检查计划");
            click_first_menu(name, ids);
        } else if (id == R.id.nav_info_check) {
            toolbar.setTitle("工程信息查询");
        } else if (id == R.id.nav_map) {
            toolbar.setTitle("地图");

        } else if (id == R.id.nav_details) {
            toolbar.setTitle("工程信息详情");

        } else if (id == R.id.nav_update_result) {
            toolbar.setTitle("上传进度");
        } else if (id == R.id.nav_update_result) {
            LoginActivity.comeToLoginActivity(this);
        } else if (id == R.id.nav_exit) {
            AppManager.getAppManager().finishAllActivity();
        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void click_first_menu(String[] name, String[][] ids) {
        toolbar.setTitle("检查计划");
        contentView.removeAllViews();
        View view = viewMap.get("first");
        if (view == null) {
            view = View.inflate(this, R.layout.content_plan, null);
        }
        initPlanLayout(view);
        setExpandableListView(name, ids, view);
        viewMap.put("first", view);
        contentView.addView(view);
    }

    private void initPlanLayout(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRecycler));
        HttpRequestVo requestVo = new HttpRequestVo("", "");
        HttpManager manager = new HttpManager(this, this, requestVo);
        manager.postRequest();


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(refreshLayout, "刷新完成", Snackbar.LENGTH_LONG).show();
                        refreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    /*
    * 测试检查计划显示
    * */
    private void setExpandableListView(final String[] name, final String[][] ids, View view) {
        listview = (ExpandableListView) view.findViewById(R.id.plan_list);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        listview.setIndicatorBounds(width - 70, width - 30);
        listview.setAdapter(new ExpandableListAdapter() {

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getGroupCount() {
                return name.length;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return ids[groupPosition].length;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return name[groupPosition];
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return ids[groupPosition][childPosition];
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

                TextView txtview = (TextView) View.inflate(context, R.layout.expandable_group_item_view, null);
                txtview.setText(name[groupPosition]);
                return txtview;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView txtview = new TextView(MainActivity.this);

                txtview.setText(ids[groupPosition][childPosition]);
                return txtview;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void onGroupExpanded(int groupPosition) {
            }

            @Override
            public void onGroupCollapsed(int groupPosition) {
            }

            @Override
            public long getCombinedChildId(long groupId, long childId) {
                return childId;
            }

            @Override
            public long getCombinedGroupId(long groupId) {
                return groupId;
            }
        });
    }

    public static void ComeToMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void action(int i, Object object) {
        mHandler.sendEmptyMessage(i);
    }

    private Dialog createDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ConstructionDetailView view = new ConstructionDetailView(this);
        builder.setView(view.getConstructionDetailView());
        builder.setTitle("地下空间日常安全检查");
        builder.setPositiveButton("检查", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SecurityCheckActivity.ComeToSecurityCheckActivity(MainActivity.this);
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}
