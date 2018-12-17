package com.xiaokun.advance_practive.ui.multi_rv_sample.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaokun.advance_practive.App;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemB;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class TypeBHolder extends BaseMultiHoder<ItemB>
{

    @LayoutRes
    public static final int LAYOUT = R.layout.type_b_layout;
    private TextView mSubTitleTv;
    private TextView mAuthorTv;
    private TextView mDateTv;

    public TypeBHolder(View itemView)
    {
        super(itemView);
        initView(itemView);
    }

    @Override
    public void bind(ItemB multiItem)
    {
        ItemB itemB = (ItemB) multiItem;
        mSubTitleTv.setText(itemB.getDetail());
        mAuthorTv.setText(itemB.getAuthor());
        mDateTv.setText(itemB.getDate());
    }

    private void initView(View itemView)
    {
        mSubTitleTv = itemView.findViewById(R.id.sub_title_tv);
        mAuthorTv = itemView.findViewById(R.id.author_tv);
        mDateTv = itemView.findViewById(R.id.date_tv);

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
