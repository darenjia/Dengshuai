package com.bokun.bkjcb.on_siteinspection.Adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.R;

import java.util.List;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class ExpandableListViewAdapter implements ExpandableListAdapter {

    private Context context;
    private List<String> plan_list;
    private List<List<String>> plan_info;

    public ExpandableListViewAdapter(Context context, List<String> plan_list, List<List<String>> plan_info) {
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
        TextView txtview = (TextView) View.inflate(context, R.layout.expandable_group_item_view, null);
        txtview.setText(plan_list.get(groupPosition));
        return txtview;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView txtview = (TextView) View.inflate(context, R.layout.expandable_child_item_view, null);
        txtview.setText(plan_info.get(groupPosition).get(childPosition));
        return txtview;
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
}
