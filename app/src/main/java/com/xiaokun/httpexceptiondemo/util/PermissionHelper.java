package com.xiaokun.httpexceptiondemo.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/15
 *      描述  ：权限控制类
 *      版本  ：1.0
 * </pre>
 */
public class PermissionHelper
{

    public static final String TAG = "PermissionHelper";

    private Context mContext;
    private String[] mPerms;
    private PermissionListener mPermissionListener;
    private String mRationale;
    private int mRequestCode;
    private PermissionFragment mPermissionFragment;
    private List<String> permsList;

    public static Builder init(FragmentActivity context)
    {
        Builder mBuilder = new Builder(context);
        return mBuilder;
    }

    private PermissionHelper(Builder builder)
    {
        mContext = builder.mContext;
        mPerms = builder.mPerms;
        mPermissionListener = builder.mPermissionListener;
        mRationale = builder.mRationale;
        mRequestCode = builder.mRequestCode;
        mPermissionFragment = builder.mPermissionFragment;
        permsList = builder.permsList;
    }

    public void request()
    {
        if (EasyPermissions.hasPermissions(mContext, mPerms))
        {
            mPermissionListener.onPermissionsGranted(mRequestCode, permsList);
        } else
        {
            Observable.timer(50, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>()
            {
                @Override
                public void accept(Long aLong) throws Exception
                {
                    EasyPermissions.requestPermissions(mPermissionFragment, mRationale, mRequestCode, mPerms);
                }
            });
        }
    }

    public static class Builder
    {
        private FragmentActivity mContext;
        private String[] mPerms;
        private PermissionListener mPermissionListener;
        private String mRationale;
        private int mRequestCode;
        private PermissionFragment mPermissionFragment;
        private List<String> permsList = new ArrayList<>();

        public Builder(FragmentActivity context)
        {
            this.mContext = context;
        }

        public Builder permissionListener(PermissionListener perssionListener)
        {
            this.mPermissionListener = perssionListener;
            return this;
        }

        public Builder permissions(String[] pers)
        {
            this.mPerms = pers;
            for (String perm : mPerms)
            {
                permsList.add(perm);
            }
            return this;
        }

        public Builder rationale(String rationale)
        {
            this.mRationale = rationale;
            return this;
        }

        public Builder requestCode(int requestCode)
        {
            this.mRequestCode = requestCode;
            return this;
        }

        public PermissionHelper build()
        {
            FragmentManager fragmentManager = mContext.getSupportFragmentManager();
            mPermissionFragment = (PermissionFragment) fragmentManager.findFragmentByTag(TAG);
            if (mPermissionFragment == null)
            {
                mPermissionFragment = PermissionFragment.newInstance();
                mPermissionFragment.setPermissionListener(mPermissionListener);
                ActivityUtils.addNoUiFgToActivity(fragmentManager, mPermissionFragment, TAG);
            }
            return new PermissionHelper(this);
        }

    }

    public abstract static class PermissionListener implements EasyPermissions.PermissionCallbacks
    {

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
                grantResults)
        {
        }

        @Override
        public void onPermissionsDenied(int requestCode, List<String> perms)
        {

        }
    }

    public static class PermissionFragment extends Fragment implements EasyPermissions.PermissionCallbacks
    {

        private static final String KEY_PERMISSION_LISTENER = "PERMISSION_LISTENER";

        PermissionListener mPermissionCallbacks;

        public static PermissionFragment newInstance()
        {
            Bundle args = new Bundle();
//            args.putSerializable(KEY_PERMISSION_LISTENER, permissionListener);
            PermissionFragment fragment = new PermissionFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
//            setRetainInstance(true);
//            Bundle arguments = getArguments();
//            mPermissionCallbacks = (PermissionListener) arguments.getSerializable(KEY_PERMISSION_LISTENER);
        }

        public void setPermissionListener(PermissionListener permissionListener)
        {
            this.mPermissionCallbacks = permissionListener;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            L.e("onActivityCreated(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() +
                    ")" + "");
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
                grantResults)
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, mPermissionCallbacks);
        }

        @Override
        public void onPermissionsGranted(int requestCode, List<String> perms)
        {
            mPermissionCallbacks.onPermissionsGranted(requestCode, perms);
        }

        @Override
        public void onPermissionsDenied(int requestCode, List<String> perms)
        {
            mPermissionCallbacks.onPermissionsDenied(requestCode, perms);
        }
    }

}
