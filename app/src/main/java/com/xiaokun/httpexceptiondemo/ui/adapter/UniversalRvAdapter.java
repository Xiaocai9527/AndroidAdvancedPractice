package com.xiaokun.httpexceptiondemo.ui.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.httpexceptiondemo.network.entity.UniversalResEntity;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/03
 *     描述   : 通用的页面的列表适配器
 *     版本   : 1.0
 * </pre>
 */
public class UniversalRvAdapter extends BaseQuickAdapter<UniversalResEntity, BaseViewHolder>
{
    public UniversalRvAdapter(int layoutResId)
    {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, UniversalResEntity item)
    {
        String publishedAt = item.getText2();
        publishedAt = publishedAt.substring(0, 10);
        helper.setText(R.id.category_desc, item.getText1());
        helper.setText(R.id.category_author, item.getText3());
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
