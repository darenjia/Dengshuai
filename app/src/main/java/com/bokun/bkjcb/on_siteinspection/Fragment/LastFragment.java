package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by BKJCB on 2017/3/28.
 */

public class LastFragment extends BaseFragment {

    private Button button;
    private CheckResult result;
    private OnClick clickListener;
    private ArrayList<CheckResult> results;
    private int fragmentId = 15;
    private int identifier;

    public interface OnClick {
        void onClick();
    }

    @Override
    public View initView() {
//        results = ((SerializableList) getArguments().get("results")).getList();
        identifier = getArguments().getInt("identifier");
        result = (CheckResult) getArguments().getSerializable("result");
//        if (results.size() > fragmentId) {
//            result = results.get(fragmentId);
//        } else {
//            result = new CheckResult();
//            result.setIdentifier(identifier);
//            results.add(fragmentId, result);
//        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.last_fragment_view, null);
        final EditText idea = (EditText) view.findViewById(R.id.lastFragment_idea);
        TextView user = (TextView) view.findViewById(R.id.lastFragment_user);
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
    }

    public void setClickListener(OnClick clickListener) {
        this.clickListener = clickListener;
    }

}
