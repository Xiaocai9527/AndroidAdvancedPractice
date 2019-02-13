package com.xiaokun.advance_practive.ui.multi_rv_sample;

import android.view.View;
import android.widget.TextView;

import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemA;
import com.xiaokun.baselib.muti_rv.HolderFactory;
import com.xiaokun.baselib.muti_rv.MultiAdapter;
import com.xiaokun.baselib.muti_rv.MultiItem;

import java.util.List;

/**
 * Created by xiaokun on 2019/2/14.
 *
 * @author xiaokun
 * @date 2019/2/14
 */
public class StickHeaderAdapter extends MultiAdapter {
    public StickHeaderAdapter(HolderFactory holderFactory) {
        super(holderFactory);
    }

    public StickHeaderAdapter(HolderFactory holderFactory, List<MultiItem> multiItems) {
        super(holderFactory, multiItems);
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {
        super.bindHeaderData(header, headerPosition);
        TextView titleTv = header.findViewById(R.id.title_tv);
        titleTv.setText(((ItemA) mData.get(headerPosition)).getTitle());

    }
}
