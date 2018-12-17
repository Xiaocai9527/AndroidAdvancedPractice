package com.xiaokun.advance_practive.ui.big_mvp.list;

import com.xiaokun.advance_practive.network.entity.ListResEntity;
import com.xiaokun.advance_practive.ui.big_mvp.BasePresenter;
import com.xiaokun.advance_practive.ui.big_mvp.BaseView;

import java.util.List;

/**
 * Created by 肖坤 on 2018/6/3.
 *
 * @author 肖坤
 * @date 2018/6/3
 */

public interface ListContract
{
    interface View extends BaseView<Presenter>
    {
        void showList(List<ListResEntity.ResultsBean> entity);

        void showListError(String errorMsg);
    }

    interface Presenter extends BasePresenter
    {
        void loadGankList();
    }
}
