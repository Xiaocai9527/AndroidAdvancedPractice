package com.xiaokun.httpexceptiondemo.ui.big_mvp.task;

import com.xiaokun.httpexceptiondemo.network.OkhttpHelper;
import com.xiaokun.httpexceptiondemo.network.RetrofitHelper;
import com.xiaokun.httpexceptiondemo.network.api.ApiService;
import com.xiaokun.httpexceptiondemo.network.entity.ListResEntity;
import com.xiaokun.httpexceptiondemo.rx.transform.RxSchedulers;

import io.reactivex.Observable;

/**
 * Created by 肖坤 on 2018/6/3.
 * 任务仓库
 *
 * @author 肖坤
 * @date 2018/6/3
 */

public class TasksRepository
{
    ApiService mApiService = RetrofitHelper.createService(ApiService.class, RetrofitHelper.
            getRetrofit(OkhttpHelper.getDefaultClient(false), ApiService.baseUrl2));

    private static TasksRepository mTasksRepository = null;

    private TasksRepository()
    {
    }

    public static TasksRepository getInstance()
    {
        synchronized (TasksRepository.class)
        {
            if (mTasksRepository == null)
            {
                mTasksRepository = new TasksRepository();
            }
        }
        return mTasksRepository;
    }

    public Observable<ListResEntity> loadListData()
    {
        return mApiService.loadListData("Android", 20, 1)
                .compose(RxSchedulers.<ListResEntity>io_main());
    }
}
