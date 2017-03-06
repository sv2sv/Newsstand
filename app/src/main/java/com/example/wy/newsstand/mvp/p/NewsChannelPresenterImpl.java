package com.example.wy.newsstand.mvp.p;

import android.util.Log;

import com.example.wy.newsstand.R;
import com.example.wy.newsstand.WYNSDepend;
import com.example.wy.newsstand.bean.NewsChannelTable;
import com.example.wy.newsstand.com.Constants;
import com.example.wy.newsstand.db.NewsChannelMgr;
import com.example.wy.newsstand.event.ChannelChangedEvent;
import com.example.wy.newsstand.mvp.v.NewsChannelView;
import com.example.wy.newsstand.net.RequestCallback;
import com.example.wy.newsstand.utils.RxBus;
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
 * Created by wy on 2017/1/19.
 */

public class NewsChannelPresenterImpl extends BasePresenterImpl<NewsChannelView,
        Map<Integer, List<NewsChannelTable>>> implements NewsChannelPresenter {


    private boolean mIsChannelChanged;

    @Inject
    public NewsChannelPresenterImpl() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIsChannelChanged) {
            RxBus.getInstance().post(new ChannelChangedEvent());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lodeNewsChannels(this);
    }

    @Override
    public void success(Map<Integer, List<NewsChannelTable>> data) {
        super.success(data);
        mView.initRecyclerViews(data.get(Constants.NEWS_CHANNEL_MINE), data.get(Constants.NEWS_CHANNEL_MORE));
    }

    @Override
    public void onItemSwap(int fromPosition, int toPosition) {
        swapDb(fromPosition, toPosition);
        mIsChannelChanged = true;
    }

    @Override
    public void onItemAddOrRemove(NewsChannelTable newsChannel, boolean isChannelMine) {
        updateDb(newsChannel, isChannelMine);
        mIsChannelChanged = true;
    }

    private ExecutorService mSingleThreadPool;



    public Subscription lodeNewsChannels(final RequestCallback<Map<Integer, List<NewsChannelTable>>> callback) {
        return Observable.create(new Observable.OnSubscribe<Map<Integer, List<NewsChannelTable>>>() {
            @Override
            public void call(Subscriber<? super Map<Integer, List<NewsChannelTable>>> subscriber) {
                Map<Integer, List<NewsChannelTable>> newsChannelListMap = getNewsChannelData();
                subscriber.onNext(newsChannelListMap);
                subscriber.onCompleted();
            }

        }).compose(UITransform.<Map<Integer, List<NewsChannelTable>>>schedulers())
                .subscribe(new Subscriber<Map<Integer, List<NewsChannelTable>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(WYNSDepend.getAppContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(Map<Integer, List<NewsChannelTable>> newsChannelListMap) {
                        callback.success(newsChannelListMap);
                    }
                });
    }

    private Map<Integer, List<NewsChannelTable>> getNewsChannelData() {
        Map<Integer, List<NewsChannelTable>> map = new HashMap<>();
        List<NewsChannelTable> channelListMine = NewsChannelMgr.loadNewsChannelsMine();
        List<NewsChannelTable> channelListMore = NewsChannelMgr.loadNewsChannelsMore();
        map.put(Constants.NEWS_CHANNEL_MINE, channelListMine);
        map.put(Constants.NEWS_CHANNEL_MORE, channelListMore);
        return map;
    }


    public void swapDb(final int fromPosition, final int toPosition) {
        createThreadPool();
        mSingleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("swapDb run",Thread.currentThread().getName());
                Log.d("swapDb run","fromPosition: " + fromPosition + "； toPosition: " + toPosition);

                NewsChannelTable fromNewsChannel = NewsChannelMgr.loadNewsChannel(fromPosition);
                NewsChannelTable toNewsChannel = NewsChannelMgr.loadNewsChannel(toPosition);

                if (isAdjacent(fromPosition, toPosition)) {
                    swapAdjacentIndexAndUpdate(fromNewsChannel, toNewsChannel);
                } else if (fromPosition - toPosition > 0) {
                    List<NewsChannelTable> newsChannels = NewsChannelMgr
                            .loadNewsChannelsWithin(toPosition, fromPosition - 1);

                    increaseOrReduceIndexAndUpdate(newsChannels, true);
                    changeFromChannelIndexAndUpdate(fromNewsChannel, toPosition);
                } else if (fromPosition - toPosition < 0) {
                    List<NewsChannelTable> newsChannels = NewsChannelMgr
                            .loadNewsChannelsWithin(fromPosition + 1, toPosition);

                    increaseOrReduceIndexAndUpdate(newsChannels, false);
                    changeFromChannelIndexAndUpdate(fromNewsChannel, toPosition);
                }
            }

            private boolean isAdjacent(int fromChannelIndex, int toChannelIndex) {
                return Math.abs(fromChannelIndex - toChannelIndex) == 1;
            }

            private void swapAdjacentIndexAndUpdate(NewsChannelTable fromNewsChannel,
                                                    NewsChannelTable toNewsChannel) {
                fromNewsChannel.setNewsChannelIndex(toPosition);
                toNewsChannel.setNewsChannelIndex(fromPosition);

                NewsChannelMgr.update(fromNewsChannel);
                NewsChannelMgr.update(toNewsChannel);
            }
        });
    }

    private void increaseOrReduceIndexAndUpdate(List<NewsChannelTable> newsChannels, boolean isIncrease) {
        for (NewsChannelTable newsChannel : newsChannels) {
            increaseOrReduceIndex(isIncrease, newsChannel);
            NewsChannelMgr.update(newsChannel);
        }
    }

    private void increaseOrReduceIndex(boolean isIncrease, NewsChannelTable newsChannel) {
        int targetIndex;
        if (isIncrease) {
            targetIndex = newsChannel.getNewsChannelIndex() + 1;
        } else {
            targetIndex = newsChannel.getNewsChannelIndex() - 1;
        }
        newsChannel.setNewsChannelIndex(targetIndex);
    }

    private void changeFromChannelIndexAndUpdate(NewsChannelTable fromNewsChannel, int toPosition) {
        fromNewsChannel.setNewsChannelIndex(toPosition);
        NewsChannelMgr.update(fromNewsChannel);
    }


    public void updateDb(final NewsChannelTable newsChannel, final boolean isChannelMine) {
        createThreadPool();
        mSingleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("updateDb run",Thread.currentThread().getName());

                int channelIndex = newsChannel.getNewsChannelIndex();
                if (isChannelMine) {
                    List<NewsChannelTable> newsChannels = NewsChannelMgr.loadNewsChannelsIndexGt(channelIndex);
                    increaseOrReduceIndexAndUpdate(newsChannels, false);

                    int targetIndex = NewsChannelMgr.getAllSize();
                    ChangeIsSelectAndIndex(targetIndex, false);
                } else {
                    List<NewsChannelTable> newsChannels = NewsChannelMgr.loadNewsChannelsIndexLtAndIsUnselect(channelIndex);
                    increaseOrReduceIndexAndUpdate(newsChannels, true);

                    int targetIndex = NewsChannelMgr.getNewsChannelSelectSize();
                    ChangeIsSelectAndIndex(targetIndex, true);
                }

            }

            private void ChangeIsSelectAndIndex(int targetIndex, boolean isSelect) {
                newsChannel.setNewsChannelSelect(isSelect);
                changeFromChannelIndexAndUpdate(newsChannel, targetIndex);
            }
        });
    }

    private void createThreadPool() {
        if (mSingleThreadPool == null) {
            mSingleThreadPool = Executors.newSingleThreadExecutor();
        }
    }
}
