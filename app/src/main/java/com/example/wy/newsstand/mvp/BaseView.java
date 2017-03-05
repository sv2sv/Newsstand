package com.example.wy.newsstand.mvp;

/**
 * Created by wy on 17-3-5.
 */

public interface BaseView {
    void showProgress();

    void hideProgress();

    void showMsg(String message);
}
