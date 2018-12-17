package com.xiaokun.advance_practive.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xiaokun.advance_practive.R;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/09/19
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class StateListAnimatorActivity extends AppCompatActivity {

    private boolean isLogin = false;

    public static void start(Context context) {
        Intent starter = new Intent(context, StateListAnimatorActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLogin) {
            setContentView(R.layout.activity_state_list_animator);
        } else {
            showLoginFragment();
        }
    }

    private void showLoginFragment() {
        FullScreenDialogFragment dialogFragment = FullScreenDialogFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), "fulldialog");
        dialogFragment.setCancelable(false);
//        dialogFragment.getDialog().getWindow().setLayout(MATCH_PARENT, MATCH_PARENT);
    }
}
