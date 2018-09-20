package com.xiaokun.httpexceptiondemo.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaokun.httpexceptiondemo.R;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/09/19
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class FullScreenDialogFragment extends DialogFragment {


    public static FullScreenDialogFragment newInstance() {
        Bundle args = new Bundle();
        FullScreenDialogFragment fragment = new FullScreenDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这个才是真正的全屏对话框
        setStyle(STYLE_NORMAL, R.style.DialogFragmentTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_dialog, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //这个左右有padding
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            dialog.getWindow().setLayout(width, height);
//        }
    }

}
