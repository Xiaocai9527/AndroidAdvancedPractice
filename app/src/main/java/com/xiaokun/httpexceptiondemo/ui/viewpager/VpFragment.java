package com.xiaokun.httpexceptiondemo.ui.viewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaokun.httpexceptiondemo.R;

/**
 * Created by 肖坤 on 2018/5/10.
 *
 * @author 肖坤
 * @date 2018/5/10
 */

public class VpFragment extends Fragment
{
    private static final String TAG = "VpFragment";
    private static final String KEY_TITLE = "TITLE";
    private TextView mTextView;

    public static VpFragment newInstance(String text)
    {
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, text);
        Log.d(TAG, "newInstance: " + text);
        VpFragment fragment = new VpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View contentView = inflater.inflate(R.layout.fragment_vp, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView)
    {
        mTextView = (TextView) contentView.findViewById(R.id.text_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        String title = bundle.getString(KEY_TITLE, "");
        mTextView.setText(title);
    }
}
