package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bokun.bkjcb.on_siteinspection.R;

/**
 * Created by DengShuai on 2017/4/7.
 */

public class TestFragment extends MainFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expandable_group_item_view, null);
        return view;
    }
}
