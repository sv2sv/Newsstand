package com.example.wy.newsstand.mvp.p;



import com.example.wy.newsstand.R;
import com.example.wy.newsstand.WYNSDepend;
import com.example.wy.newsstand.bean.NewsChannelTable;
import com.example.wy.newsstand.db.NewsChannelMgr;
import com.example.wy.newsstand.mvp.v.NewsView;
import com.example.wy.newsstand.net.RequestCallback;
import com.example.wy.newsstand.utils.UITransform;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wy on 2017/1/16.
 */
public class NewsPresenterImpl extends BasePresenterImpl<NewsView, List<NewsChannelTable>>
        implements NewsPresenter {




    @Override
    public void onCreate() {
        super.onCreate();
        loadNewsChannels();
    }

    private void loadNewsChannels() {
        mSubscription = lodeNewsChannels(this);
    }

    @Override
    public void success(List<NewsChannelTable> data) {
        super.success(data);
        mView.initViewPager(data);
    }

    @Override
    public void onChannelDbChanged() {
        loadNewsChannels();
    }


    public Subscription lodeNewsChannels(final RequestCallback<List<NewsChannelTable>> callBack) {
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
                        callBack.onError(WYNSDepend.getAppContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(List<NewsChannelTable> newsChannelTables) {
                        callBack.success(newsChannelTables);
                    }
                });
    }
}
