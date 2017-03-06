package com.example.wy.newsstand.mvp.v;


import com.example.wy.newsstand.bean.NewsSummary;
import com.example.wy.newsstand.com.LoadNewsType;
import com.example.wy.newsstand.mvp.BaseView;

import java.util.List;

/**
 * Created by wy on 2017/1/17.
 */

public interface NewsListView extends BaseView {

    void setNewsList(List<NewsSummary> newsSummary, @LoadNewsType.checker int loadType);

}
