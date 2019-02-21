package com.xiaokun.advance_practive.im.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.im.PdIMClient;
import com.xiaokun.advance_practive.im.PdOptions;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/21
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ImLoginActivity extends AppCompatActivity {

    private static final String TAG = "ImLoginActivity";

    private TextInputEditText mEtUsername;
    private TextInputEditText mEtPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_im_login);
        PdOptions pdOptions = new PdOptions();
        pdOptions.setAppKey("12345678");
        PdIMClient.getInstance().init(this, pdOptions);
        initView();
    }

    private void initView() {
        mEtUsername = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);
    }

    public void loginIm(View view) {
        String name = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            PdIMClient.getInstance().login(name, password, new PdIMClient.LoginCallback() {
                @Override
                public void onSuccess() {
                    Log.e(TAG, "loginIm(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "成功回调");
                }

                @Override
                public void onError(int code, String errorMsg) {
                    Log.e(TAG, "loginIm(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                            "code:" + code + ";errorMsg:" + errorMsg);
                }
            });

        }
    }
}
