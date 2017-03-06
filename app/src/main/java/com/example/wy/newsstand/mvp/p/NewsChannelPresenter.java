package com.example.wy.newsstand.mvp.p;


import com.example.wy.newsstand.bean.NewsChannelTable;
import com.example.wy.newsstand.mvp.BasePresenter;

/**
 * Created by wy on 2017/1/19.
 */

public interface NewsChannelPresenter extends BasePresenter {
    void onItemSwap(int fromPosition, int toPosition);

    void onItemAddOrRemove(NewsChannelTable newsChannel, boolean isChannelMine);
}
