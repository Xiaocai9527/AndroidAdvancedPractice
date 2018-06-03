package com.xiaokun.httpexceptiondemo.ui.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 肖坤 on 2018/5/10.
 *
 * @author 肖坤
 * @date 2018/5/10
 */

public class VpAdapter extends FragmentPagerAdapter
{

    private List<String> mDatas;

    public VpAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public VpAdapter(FragmentManager fm, List<String> datas)
    {
        super(fm);
        this.mDatas = datas;
    }

    @Override
    public Fragment getItem(int position)
    {
        return VpFragment.newInstance(mDatas.get(position));
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }
}
