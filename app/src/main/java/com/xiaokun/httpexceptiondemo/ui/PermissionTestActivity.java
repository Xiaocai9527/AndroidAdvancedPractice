package com.xiaokun.httpexceptiondemo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiaokun.httpexceptiondemo.App;
import com.xiaokun.baselib.config.Constants;
import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.baselib.util.PermissionHelper;
import com.xiaokun.baselib.util.PermissionUtil;

import java.util.List;

/**
 * Created by 肖坤 on 2018/6/16.
 *
 * @author 肖坤
 * @date 2018/6/16
 */

public class PermissionTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRecordBtn;
    private Button mStoreBtn;
    private Button mLocationBtn;
    private Button mCameraBtn;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_permission_test);
        initView();
    }

    private void initView() {
        mRecordBtn = (Button) findViewById(R.id.record_btn);
        mStoreBtn = (Button) findViewById(R.id.store_btn);
        mLocationBtn = (Button) findViewById(R.id.location_btn);
        mCameraBtn = (Button) findViewById(R.id.camera_btn);

        initListener(mRecordBtn, mStoreBtn, mLocationBtn, mCameraBtn);
    }

    private void initListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_btn:
                record();
                break;
            default:
                break;
        }
    }

    public static final int RECORD_CODE = 1;
    String[] recordPerm = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private void record() {
        PermissionHelper.init(this)
                .permissions(recordPerm)
                .rationale("录音权限,存储权限")
                .requestCode(RECORD_CODE)
                .permissionListener(new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPermissionsGranted(int requestCode, List<String> perms) {
                        Toast.makeText(App.getAppContext(), "启动录音代码", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionsDenied(int requestCode, List<String> perms) {
                        Toast.makeText(App.getAppContext(), "缺少录音权限", Toast.LENGTH_SHORT).show();
                        //在拒绝的这个地方来进行终极处理, 这里防止有人点击了不再提醒的选项
                        App.getSp().edit().putInt(Constants.REQUEST_CODE_PERMISSION, RECORD_CODE).commit();
                        PermissionUtil.showMissingPermissionDialog((Activity) mContext, "录音");
                    }
                }).build().request();
    }
}
