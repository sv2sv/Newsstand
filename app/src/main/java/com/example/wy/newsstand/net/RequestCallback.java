package com.example.wy.newsstand.net;

/**
 * Created by wy on 17-3-5.
 */

public interface RequestCallback<T> {
    void beforeRequest();

    void success(T data);

    void onError(String errorMsg);
}
