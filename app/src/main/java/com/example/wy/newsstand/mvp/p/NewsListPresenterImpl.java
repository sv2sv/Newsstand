package com.example.wy.newsstand.mvp.p;


import android.util.Log;

import com.example.wy.newsstand.WYNSDepend;
import com.example.wy.newsstand.bean.NewsSummary;
import com.example.wy.newsstand.com.Host;
import com.example.wy.newsstand.com.LoadNewsType;
import com.example.wy.newsstand.com.NewApi;
import com.example.wy.newsstand.helper.NetHelper;
import com.example.wy.newsstand.mvp.v.NewsListView;
import com.example.wy.newsstand.net.RequestCallback;
import com.example.wy.newsstand.net.RetrofitManager;
import com.example.wy.newsstand.utils.UITransform;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by wy on 2017/1/17.
 */

public class NewsListPresenterImpl extends BasePresenterImpl<NewsListView, List<NewsSummary>>
        implements NewsListPresenter, RequestCallback<List<NewsSummary>> {

    private String mNewsType;
    private String mNewsId;
    private int mStartPage;

    private boolean misFirstLoad;
    private boolean mIsRefresh = true;

    @Inject
    public NewsListPresenterImpl(){

    }

    @Override
    public void onCreate() {
        if (mView != null) {
            loadNewsData();
        }
    }

    @Override
    public void beforeRequest() {
        if (!misFirstLoad) {
            mView.showProgress();
        }
    }

    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
        if (mView != null) {
            int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_ERROR : LoadNewsType.TYPE_LOAD_MORE_ERROR;
            mView.setNewsList(null, loadType);
        }
    }

    @Override
    public void success(List<NewsSummary> items) {
        misFirstLoad = true;
        if (items != null) {
            mStartPage += 20;
        }

        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setNewsList(items, loadType);
            mView.hideProgress();
        }

    }

    @Override
    public void setNewsTypeAndId(String newsType, String newsId) {
        mNewsType = newsType;
        mNewsId = newsId;
    }

    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        loadNewsData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        loadNewsData();
    }

    private void loadNewsData() {
        mSubscription =loadNews(this, mNewsType, mNewsId, mStartPage);
    }


    public Subscription loadNews(final RequestCallback<List<NewsSummary>> listener, String type,
                                 final String id, int startPage) {
//        mIsNetError = false;
        // 对API调用了observeOn(MainThread)之后，线程会跑在主线程上，包括onComplete也是，
        // unsubscribe也在主线程，然后如果这时候调用call.cancel会导致NetworkOnMainThreadException
        // 加一句unsubscribeOn(io)
        return RetrofitManager.getInstance(Host.NETEASE_NEWS_VIDEO).getNewsListObservable(type, id, startPage)
                .flatMap(new Func1<Map<String, List<NewsSummary>>, Observable<NewsSummary>>() {
                    @Override
                    public Observable<NewsSummary> call(Map<String, List<NewsSummary>> map) {
                        if (id.endsWith(NewApi.HOUSE_ID)) {
                            // 房产实际上针对地区的它的id与返回key不同
                            return Observable.from(map.get("北京"));
                        }
                        return Observable.from(map.get(id));
                    }
                })
                .map(new Func1<NewsSummary, NewsSummary>() {
                    @Override
                    public NewsSummary call(NewsSummary newsSummary) {
                        String ptime = WYNSDepend.formatDate(newsSummary.getPtime());
                        newsSummary.setPtime(ptime);
                        return newsSummary;
                    }
                })
//                .toList()
                .distinct()
                .toSortedList(new Func2<NewsSummary, NewsSummary, Integer>() {
                    @Override
                    public Integer call(NewsSummary newsSummary, NewsSummary newsSummary2) {
                        return newsSummary2.getPtime().compareTo(newsSummary.getPtime());
                    }
                })
                .compose(UITransform.<List<NewsSummary>>schedulers())
                .subscribe(new Subscriber<List<NewsSummary>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("loadNews","onCompleted");
//                        checkNetState(listener);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("loadNews","onError"+ e.toString());
//                        checkNetState(listener);
//                        if (!NetUtil.isNetworkAvailable(App.getAppContext())) {
                        listener.onError(NetHelper.analyzeNetworkError(e));
//                        }
                    }

                    @Override
                    public void onNext(List<NewsSummary> newsSummaries) {
                        Log.d("loadNews","onNext");
                        listener.success(newsSummaries);
                    }
                });

    }

}
