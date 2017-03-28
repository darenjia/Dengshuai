package com.bokun.bkjcb.on_siteinspection.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;

/**
 * Created by BKJCB on 2017/3/17.
 */

public class LoginActivity extends BaseActivity implements RequestListener {
    private EditText mUserName;
    private EditText mPassword;
    private Button mLogin;
    private Button mChangeUser;
    private CoordinatorLayout mContainer;
    private LinearLayout mLoginView;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RequestListener.EVENT_NOT_NETWORD:
                    Snackbar.make(mContainer, "", Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();
                    break;
                case RequestListener.EVENT_CLOSE_SOCKET:
                    Snackbar.make(mContainer, "网络错误！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_NETWORD_EEEOR:
                    Snackbar.make(mContainer, "请确认网络是否可用！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_GET_DATA_EEEOR:
                    Snackbar.make(mContainer, "服务器错误，请稍后再试！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_GET_DATA_SUCCESS:
                    break;

            }
        }
    };
    private HttpManager httpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_mylogin);
    }

    @Override
    protected void findView() {
        mChangeUser = (Button) findViewById(R.id.shift_user_button);
        mLogin = (Button) findViewById(R.id.login_button);
        mPassword = (EditText) findViewById(R.id.input_password);
        mUserName = (EditText) findViewById(R.id.input_username);
        mContainer = (CoordinatorLayout) findViewById(R.id.snackbar_container);
        mLoginView = (LinearLayout) findViewById(R.id.logining_view);
    }

    @Override
    protected void setListener() {
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ComeToMainActivity(LoginActivity.this);
//                attemptLogin();
            }
        });
        mChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (httpManager != null && httpManager.isRunning()) {
                    httpManager.cancelHttpRequest();
                }
                mUserName.setText("");
                mPassword.setText("");
                mLoginView.setVisibility(View.GONE);
                mLogin.setVisibility(View.VISIBLE);
                mUserName.requestFocus();
            }
        });
    }

    /*
    * 判断用户名是否可用*/
    private boolean isUserNameValid(String name) {
        return name.length() > 4;
    }

    /*
    * 判断密码长度是否符合要求*/
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void attemptLogin() {
        // Reset errors.
        mUserName.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserName.setError(getString(R.string.error_field_required));
            focusView = mUserName;
            cancel = true;
        } else if (!isUserNameValid(userName)) {
            mUserName.setError(getString(R.string.error_invalid_username));
            focusView = mUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.g
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            MainActivity.ComeToMainActivity(this);

            HttpRequestVo request = new HttpRequestVo("", "", new JsonParser());
            httpManager = new HttpManager(this, this, request, 2);
            httpManager.postRequest();
            mLoginView.setVisibility(View.VISIBLE);
            mLogin.setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void action(int i, Object object) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("", " ");
        msg.what = i;
        msg.setData(bundle);
        mHandler.sendEmptyMessage(i);
    }

    @Override
    public void onBackPressed() {
        if (httpManager != null && httpManager.isRunning()) {
            httpManager.cancelHttpRequest();
            Snackbar.make(mContainer, "登录已取消！", Snackbar.LENGTH_LONG).show();
            mLoginView.setVisibility(View.GONE);
            mLogin.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }

    }

    public static void comeToLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
}
