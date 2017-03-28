package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.R;

/**
 * Created by BKJCB on 2017/3/28.
 */

public class LastFragment extends BaseFragment{

    @Override
    public View initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.last_fragment_view,null);
        Button button = (Button) view.findViewById(R.id.btn_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"正在提交",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void initData() {

    }
}
