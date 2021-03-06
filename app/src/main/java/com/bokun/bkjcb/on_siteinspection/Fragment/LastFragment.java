package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by BKJCB on 2017/3/28.
 */

public class LastFragment extends BaseFragment {

    private Button button;
    private CheckResult result;
    private OnClick clickListener;
    private TextView user;

    public interface OnClick {
        void onClick();
    }

    @Override
    public View initView() {
        result = (CheckResult) getArguments().getSerializable("result");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.last_fragment_view, null);
        final EditText idea = (EditText) view.findViewById(R.id.lastFragment_idea);
        user = (TextView) view.findViewById(R.id.lastFragment_user);
        TextView date = (TextView) view.findViewById(R.id.lastFragment_date);
        String str_date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(System.currentTimeMillis());
        date.setText(str_date);
        String str = result.getComment();
        if (str != null) {
            idea.setText(str);
        }
        button = (Button) view.findViewById(R.id.btn_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setComment(idea.getText().toString());
                LogUtil.logI("正在提交");
                clickListener.onClick();
            }
        });
        return view;
    }

    @Override
    public void initData() {
        SharedPreferences preferences = getContext().getSharedPreferences("default", Context.MODE_PRIVATE);
        String username = preferences.getString("UserName", "");
        user.setText(username);

    }

    public void setClickListener(OnClick clickListener) {
        this.clickListener = clickListener;
    }

}
