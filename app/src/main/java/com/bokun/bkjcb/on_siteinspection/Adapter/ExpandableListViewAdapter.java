package com.bokun.bkjcb.on_siteinspection.Adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CheckPlan> plan_list;
    private List<ArrayList<CheckPlan>> plan_info;

    public ExpandableListViewAdapter(Context context, ArrayList<CheckPlan> plan_list, ArrayList<ArrayList<CheckPlan>> plan_info) {
        this.context = context;
        this.plan_list = plan_list;
        this.plan_info = plan_info;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return plan_list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return plan_info.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return plan_list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return plan_info.get(groupPosition).get(childPosition);
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
        View view = View.inflate(context, R.layout.expandable_group_item_view, null);
        TextView txtview = (TextView) view.findViewById(R.id.group_title);
        txtview.setText(plan_list.get(groupPosition).getName());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.checkitemlist, null);
        TextView title = (TextView) view.findViewById(R.id.check_item_title);
        TextView state = (TextView) view.findViewById(R.id.check_item_state);
        CheckPlan plan = plan_info.get(groupPosition).get(childPosition);
        title.setText(plan.getName());
        state.setText(getState(plan.getState()));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
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

    private String getState(int state) {
        if (state == 0) {
            return "（未开始检查）";
        } else if (state == 1) {
            return "（检查未完成）";
        } else if (state == 2) {
            return "（检查已完成）";
        } else {
            return "（数据已上传）";
        }
    }
}
