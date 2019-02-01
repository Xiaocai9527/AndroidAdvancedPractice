package com.xiaokun.advance_practive.ui.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.network.meizi.CategoryResEntity.ResultsBean;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/01/31
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class NightModeHolder extends BaseMultiHodler<ResultsBean> {

    public static int TYPE_FOOTER = R.layout.item_other;
    private CardView mNightCv;
    private RelativeLayout mNightRl;
    private TextView mCategoryDesc;
    private TextView mCategoryAuthor;
    private TextView mCategoryDate;

    public NightModeHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    private void initView(View itemView) {
        mCategoryDesc = itemView.findViewById(R.id.category_desc);
        mCategoryAuthor = itemView.findViewById(R.id.category_author);
        mCategoryDate = itemView.findViewById(R.id.category_date);
    }

    @Override
    public void bind(ResultsBean multiItem) {
        mCategoryAuthor.setText(multiItem.getWho());
        mCategoryDate.setText(multiItem.getPublishedAt());
        mCategoryDesc.setText(multiItem.getDesc());
    }
}
