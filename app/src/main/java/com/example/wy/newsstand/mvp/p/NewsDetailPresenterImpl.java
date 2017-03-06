package com.example.wy.newsstand.mvp.p;

import android.util.Log;

import com.example.wy.newsstand.WYNSDepend;
import com.example.wy.newsstand.bean.NewsDetail;
import com.example.wy.newsstand.com.Host;
import com.example.wy.newsstand.utils.NetHelper;
import com.example.wy.newsstand.mvp.v.NewsDetailView;
import com.example.wy.newsstand.net.RequestCallback;
import com.example.wy.newsstand.net.RetrofitManager;
import com.example.wy.newsstand.utils.UITransform;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;


/**
 * Created by wy on 2017/1/18.
 */

public class NewsDetailPresenterImpl extends BasePresenterImpl<NewsDetailView, NewsDetail>
        implements NewsDetailPresenter {

    private String mPostId;

    @Inject
    public NewsDetailPresenterImpl(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSubscription = loadNewsDetail(this, mPostId);
    }

    @Override
    public void success(NewsDetail data) {
        super.success(data);
        mView.setNewsDetail(data);
    }

    @Override
    public void setPosId(String postId) {
        mPostId = postId;
    }




    public Subscription loadNewsDetail(final RequestCallback<NewsDetail> callBack, final String postId) {
        return RetrofitManager.getInstance(Host.NETEASE_NEWS_VIDEO).getNewsDetailObservable(postId)
                .map(new Func1<Map<String, NewsDetail>, NewsDetail>() {
                    @Override
                    public NewsDetail call(Map<String, NewsDetail> map) {
                        Log.d("loadNewsDetail ",Thread.currentThread().getName());

                        NewsDetail newsDetail = map.get(postId);
                        changeNewsDetail(newsDetail);
                        return newsDetail;
                    }
                })
                .compose(UITransform.<NewsDetail>schedulers())
                .subscribe(new Observer<NewsDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("loadnewDetails",e.toString());
                        callBack.onError(NetHelper.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(NewsDetail newsDetail) {
                        callBack.success(newsDetail);
                    }
                });
    }

    private void changeNewsDetail(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
        if (isChange(imgSrcs)) {
            String newsBody = newsDetail.getBody();
            newsBody = changeNewsBody(imgSrcs, newsBody);
            newsDetail.setBody(newsBody);
        }
    }
    private boolean isChange(List<NewsDetail.ImgBean> imgSrcs) {
        return imgSrcs != null && imgSrcs.size() >= 2 && WYNSDepend.isHavePhoto();
    }

    private String changeNewsBody(List<NewsDetail.ImgBean> imgSrcs, String newsBody) {
        for (int i = 0; i < imgSrcs.size(); i++) {
            String oldChars = "<!--IMG#" + i + "-->";
            String newChars;
            if (i == 0) {
                newChars = "";
            } else {
                newChars = "<img src=\"" + imgSrcs.get(i).getSrc() + "\" />";
            }
            newsBody = newsBody.replace(oldChars, newChars);
        }
        return newsBody;
    }
}
