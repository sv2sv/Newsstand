package com.example.wy.newsstand.mvp.v;



import com.example.wy.newsstand.bean.NewsChannelTable;
import com.example.wy.newsstand.mvp.BaseView;

import java.util.List;

/**
 * Created by wy on 2017/1/19.
 */

public interface NewsChannelView extends BaseView {
    void initRecyclerViews(List<NewsChannelTable> newsChannelsMine, List<NewsChannelTable> newsChannelsMore);
}
