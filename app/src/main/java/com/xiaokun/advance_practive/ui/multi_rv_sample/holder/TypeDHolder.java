package com.xiaokun.advance_practive.ui.multi_rv_sample.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiaokun.advance_practive.App;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemD;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class TypeDHolder extends BaseMultiHodler<ItemD>
{

    @LayoutRes
    public static final int LAYOUT = R.layout.type_d_layout;

    private ImageView mImg1;
    private ImageView mImg2;
    private ImageView mImg3;

    public TypeDHolder(View itemView)
    {
        super(itemView);
        initView(itemView);
    }

    @Override
    public void bind(ItemD multiItem)
    {
        ItemD itemD = (ItemD) multiItem;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(itemView.getContext()).load(itemD.getImgUrl1()).apply(requestOptions).into(mImg1);
        Glide.with(itemView.getContext()).load(itemD.getImgUrl2()).apply(requestOptions).into(mImg2);
        Glide.with(itemView.getContext()).load(itemD.getImgUrl3()).apply(requestOptions).into(mImg3);
    }

    private void initView(View itemView)
    {
        mImg1 = itemView.findViewById(R.id.img1);
        mImg2 = itemView.findViewById(R.id.img2);
        mImg3 = itemView.findViewById(R.id.img3);

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
