package com.xiaokun.advance_practive.ui.multi_rv_sample.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaokun.advance_practive.App;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemA;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class TypeAHolder extends BaseMultiHoder<ItemA>
{
    @LayoutRes
    public static final int LAYOUT = R.layout.type_a_layout;
    private TextView mTitleTv;

    public TypeAHolder(View itemView)
    {
        super(itemView);
        initView(itemView);
    }

    @Override
    public void bind(ItemA multiItem)
    {
        mTitleTv.setText((multiItem).getTitle());
    }

    private void initView(View itemView)
    {
        mTitleTv = itemView.findViewById(R.id.title_tv);
        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int adapterPosition = getAdapterPosition();
                Toast.makeText(App.getAppContext(), adapterPosition + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
