package com.example.wy.newsstand.mvp.p;


import com.example.wy.newsstand.mvp.BasePresenter;

/**
 * Created by wy on 2017/1/17.
 */

public interface NewsListPresenter extends BasePresenter {
    void setNewsTypeAndId(String newsType, String newsId);

    void refreshData();

    void loadMore();
}
