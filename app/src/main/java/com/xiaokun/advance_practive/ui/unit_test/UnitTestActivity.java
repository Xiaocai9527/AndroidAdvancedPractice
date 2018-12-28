package com.xiaokun.advance_practive.ui.unit_test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiaokun.advance_practive.R;
import com.xiaokun.wanandroid.WanLoginActivity;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class UnitTestActivity extends AppCompatActivity implements View.OnClickListener {
    public Button mbutton36;
    public Button mbutton38;
    public Button mbutton37;
    public Button mbutton43;
    public Button mbutton42;
    public Button mbutton45;
    public Button mbutton44;
    public Button mbutton41;
    public Button mbutton40;
    public Button mbutton39;

    public static void start(Context context) {
        Intent starter = new Intent(context, UnitTestActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_test);
        initView();
    }

    private void initView() {
        mbutton36 = findViewById(R.id.button36);
        mbutton38 = findViewById(R.id.button38);
        mbutton37 = findViewById(R.id.button37);
        mbutton43 = findViewById(R.id.button43);
        mbutton42 = findViewById(R.id.button42);
        mbutton45 = findViewById(R.id.button45);
        mbutton44 = findViewById(R.id.button44);
        mbutton41 = findViewById(R.id.button41);
        mbutton40 = findViewById(R.id.button40);
        mbutton39 = findViewById(R.id.button39);
        mbutton36.setOnClickListener(this);
        mbutton38.setOnClickListener(this);
        mbutton37.setOnClickListener(this);
        mbutton43.setOnClickListener(this);
        mbutton42.setOnClickListener(this);
        mbutton45.setOnClickListener(this);
        mbutton44.setOnClickListener(this);
        mbutton41.setOnClickListener(this);
        mbutton40.setOnClickListener(this);
        mbutton39.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button36:
                WanLoginActivity.start(this);
                break;
            case R.id.button38:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.title).setMessage(R.string.message).setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
            case R.id.button37:
                Toast.makeText(this, "哈皮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button43:

                break;
            case R.id.button42:

                break;
            case R.id.button45:

                break;
            case R.id.button44:

                break;
            case R.id.button41:

                break;
            case R.id.button40:

                break;
            case R.id.button39:
                mbutton39.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                mbutton39.setEnabled(false);
                break;
            default:
                break;
        }
    }
}
