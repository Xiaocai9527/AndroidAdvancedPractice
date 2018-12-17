package com.xiaokun.advance_practive.ui.multi_rv_sample.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiaokun.advance_practive.App;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemC;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class TypeCHolder extends BaseMultiHoder<ItemC>
{

    @LayoutRes
    public static final int LAYOUT = R.layout.type_c_layout;

    private ImageView mTypeCImg;
    private TextView mDetailTv;
    private TextView mAuthorTv;

    public TypeCHolder(View itemView)
    {
        super(itemView);
        initView(itemView);
    }

    @Override
    public void bind(ItemC itemC)
    {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(itemView.getContext()).load(itemC.getImgUrl()).apply(requestOptions).into(mTypeCImg);

        mDetailTv.setText(itemC.getDetail());
        mAuthorTv.setText(itemC.getAuthor());
    }

    private void initView(View itemView)
    {
        mTypeCImg = itemView.findViewById(R.id.type_c_img);
        mDetailTv = itemView.findViewById(R.id.detail_tv);
        mAuthorTv = itemView.findViewById(R.id.author_tv);

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
