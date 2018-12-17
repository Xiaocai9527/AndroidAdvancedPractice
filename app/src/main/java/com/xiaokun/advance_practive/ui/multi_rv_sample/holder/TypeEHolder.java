package com.xiaokun.advance_practive.ui.multi_rv_sample.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiaokun.advance_practive.App;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemE;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class TypeEHolder extends BaseMultiHoder<ItemE>
{
    @LayoutRes
    public static final int LAYOUT = R.layout.type_e_layout;
    private ImageView mImg;

    public TypeEHolder(View itemView)
    {
        super(itemView);
        initView(itemView);
    }

    @Override
    public void bind(ItemE multiItem)
    {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(itemView.getContext()).load(multiItem.getImgUrl()).apply(requestOptions).into(mImg);
    }

    private void initView(View itemView)
    {
        mImg = itemView.findViewById(R.id.img);
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
