package com.xiaokun.httpexceptiondemo.ui.adapter;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.httpexceptiondemo.network.meizi.CategoryResEntity;

/**
 * Created by 肖坤 on 2018/4/30.
 *
 * @author 肖坤
 * @date 2018/4/30
 */

public class NightModeAdapter extends BaseQuickAdapter<CategoryResEntity.ResultsBean, BaseViewHolder>
{
    public NightModeAdapter(int layoutResId)
    {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryResEntity.ResultsBean item)
    {
        String publishedAt = item.getPublishedAt();
        publishedAt = publishedAt.substring(0, 10);
        helper.setText(R.id.category_desc, item.getDesc());
        helper.setText(R.id.category_author, item.getWho());
        helper.setText(R.id.category_date, publishedAt);

        CardView cardView = (CardView) helper.getView(R.id.night_cv);
        cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
