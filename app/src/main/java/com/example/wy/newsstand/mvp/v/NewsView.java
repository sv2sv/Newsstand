package com.example.wy.newsstand.mvp.v;

import com.example.wy.newsstand.bean.NewsChannelTable;
import com.example.wy.newsstand.mvp.BaseView;

import java.util.List;

/**
 * Created by wy on 17-3-5.
 */

public interface NewsView extends BaseView {

    void initViewPager(List<NewsChannelTable> newsChannelTables);

}