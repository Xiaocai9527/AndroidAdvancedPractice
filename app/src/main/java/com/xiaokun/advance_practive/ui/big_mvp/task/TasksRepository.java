package com.xiaokun.advance_practive.ui.big_mvp.task;

import com.xiaokun.baselib.network.OkhttpHelper;
import com.xiaokun.baselib.network.RetrofitHelper;
import com.xiaokun.advance_practive.network.api.ApiService;
import com.xiaokun.advance_practive.network.entity.ListResEntity;
import com.xiaokun.baselib.rx.transform.RxSchedulers;

import io.reactivex.Observable;

/**
 * Created by 肖坤 on 2018/6/3.
 * 任务仓库
 *
 * @author 肖坤
 * @date 2018/6/3
 */

public class TasksRepository {
    ApiService mApiService = RetrofitHelper.getInstance().createService(ApiService.class,
            RetrofitHelper.getInstance().getRetrofit(OkhttpHelper.getDefaultClient(), ApiService.baseUrl2));

    private static TasksRepository mTasksRepository = null;

    private TasksRepository() {
    }

    public static TasksRepository getInstance() {
        synchronized (TasksRepository.class) {
            if (mTasksRepository == null) {
                mTasksRepository = new TasksRepository();
            }
        }
        return mTasksRepository;
    }

    public Observable<ListResEntity> loadListData() {
        return mApiService.loadListData("Android", 20, 1)
                .compose(RxSchedulers.<ListResEntity>io_main());
    }
}
