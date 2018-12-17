package com.xiaokun.wanandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.TextView;;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/17
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    public TextView mLogin;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mLogin = view.findViewById(R.id.login);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login) {
            ((WanLoginActivity) getActivity()).openFg(1);
        } else {

        }
    }
}
