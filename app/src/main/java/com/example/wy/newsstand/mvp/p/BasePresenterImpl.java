package com.example.wy.newsstand.mvp.p;

import android.support.annotation.NonNull;

import com.example.wy.newsstand.mvp.BasePresenter;
import com.example.wy.newsstand.mvp.BaseView;
import com.example.wy.newsstand.net.RequestCallback;

import rx.Subscription;

/**
 * Created by wy on 17-3-5.
 */

public class BasePresenterImpl<T extends BaseView,E> implements BasePresenter, RequestCallback<E> {

    protected T mView;
    protected Subscription mSubscription;

    @Override
    public void onCreate() {

    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mView = (T) view;
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void beforeRequest() {

    }

    @Override
    public void success(E data) {
        mView.hideProgress();
    }

    @Override
    public void onError(String errorMsg) {
        mView.hideProgress();
        mView.showMsg(errorMsg);
    }
}
