package com.xiaokun.httpexceptiondemo.ui.big_mvp.list;

import android.support.annotation.NonNull;

import com.xiaokun.httpexceptiondemo.network.entity.ListResEntity;
import com.xiaokun.httpexceptiondemo.ui.big_mvp.task.TasksRepository;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.xiaokun.baselib.util.Preconditions.checkNotNull;

/**
 * Created by 肖坤 on 2018/6/3.
 *
 * @author 肖坤
 * @date 2018/6/3
 */

public class ListPresenter implements ListContract.Presenter
{
    @NonNull
    private final CompositeDisposable mCompositeDisposable;
    @NonNull
    private TasksRepository mTasksRepository;
    @NonNull
    private ListContract.View mView;

    public ListPresenter(@NonNull TasksRepository tasksRepository, @NonNull ListContract.View view)
    {
        mTasksRepository = checkNotNull(tasksRepository);
        mView = checkNotNull(view);

        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe()
    {
        loadGankList();
    }

    @Override
    public void unsubscribe()
    {
        mCompositeDisposable.clear();
    }

    @Override
    public void loadGankList()
    {
        Disposable disposable = mTasksRepository.loadListData().subscribe(new Consumer<ListResEntity>()
        {
            @Override
            public void accept(ListResEntity listResEntity) throws Exception
            {
                mView.showList(listResEntity.getResults());
            }
        }, new Consumer<Throwable>()
        {
            @Override
            public void accept(Throwable throwable) throws Exception
            {
                mView.showListError(throwable.getMessage());
            }
        });
        mCompositeDisposable.add(disposable);
    }
}
