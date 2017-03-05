package com.example.wy.newsstand.mvp;

import android.support.annotation.NonNull;

/**
 * Created by wy on 17-3-5.
 */

public interface BasePresenter {
    void onCreate();

    void attachView(@NonNull BaseView view);

    void onDestroy();
}
