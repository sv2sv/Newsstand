package com.example.wy.newsstand.mvp.v;

import android.util.Log;

import com.example.wy.newsstand.R;
import com.example.wy.newsstand.WYNSDepend;
import com.example.wy.newsstand.bean.NewsChannelTable;
import com.example.wy.newsstand.com.Constants;
import com.example.wy.newsstand.db.NewsChannelMgr;
import com.example.wy.newsstand.mvp.p.BasePresenterImpl;
import com.example.wy.newsstand.mvp.p.NewsPresenter;
import com.example.wy.newsstand.net.RequestCallback;
import com.example.wy.newsstand.utils.UITransform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wy on 17-3-5.
 */

class HomePresenterImpl extends BasePresenterImpl<NewsView, List<NewsChannelTable>>implements NewsPresenter{
    private ExecutorService mSingleThreadPool;

    @Inject
    public HomePresenterImpl(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSubscription= loadNewsChannels(this);
    }


    @Override
    public void success(List<NewsChannelTable> data) {
        super.success(data);
        mView.initViewPager(data);
    }

    @Override
    public void onChannelDbChanged() {

        mSubscription = loadNewsChannels(this);
    }

    public Subscription loadNewsChannels(final RequestCallback<List<NewsChannelTable>> callback) {
        return Observable.create(new Observable.OnSubscribe<List<NewsChannelTable>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannelTable>> subscriber) {
                NewsChannelMgr.initDB();
                subscriber.onNext(NewsChannelMgr.loadNewsChannelsMine());
                subscriber.onCompleted();
            }
        })
                .compose(UITransform.<List<NewsChannelTable>>schedulers())
                .subscribe(new Subscriber<List<NewsChannelTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(WYNSDepend.getAppContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(List<NewsChannelTable> newsChannelTables) {
                        callback.success(newsChannelTables);
                    }
                });
    }






}
