package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.R;

/**
 * Created by BKJCB on 2017/3/28.
 */

public class LastFragment extends BaseFragment {

    private Button button;

    @Override
    public View initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.last_fragment_view, null);
        EditText idea = (EditText) view.findViewById(R.id.lastFragment_idea);
        TextView user = (TextView) view.findViewById(R.id.lastFragment_user);
        TextView date = (TextView) view.findViewById(R.id.lastFragment_date);
        button = (Button) view.findViewById(R.id.btn_submit);
        return view;
    }

    @Override
    public void initData() {

    }

    public void setListener(View.OnClickListener listener) {
        button.setOnClickListener(listener);
    }
}
