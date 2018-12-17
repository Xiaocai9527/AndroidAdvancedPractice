package com.xiaokun.advance_practive.ui.mvp;

import com.xiaokun.advance_practive.network.entity.UniversalResEntity;

import java.util.List;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/03
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public interface UniversalView extends BaseView
{

    void getUniversalSuc(List<UniversalResEntity> entity);

    void getUniversalFailed(String errorMsg);

}
