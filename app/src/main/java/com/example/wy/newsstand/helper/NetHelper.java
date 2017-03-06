package com.example.wy.newsstand.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.wy.newsstand.R;
import com.example.wy.newsstand.WYNSDepend;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by wy on 17-3-5.
 */

public class NetHelper {
    /**
     * 检查当前网络是否可用
     * @return 是否连接到网络
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) WYNSDepend.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNetworkErrThenShowMsg() {
        if (!isNetworkAvailable()) {
            //TODO: 刚启动app Snackbar不起作用，延迟显示也不好使，这是why？
            Toast.makeText(WYNSDepend.getAppContext(), "networld error",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static String analyzeNetworkError(Throwable e) {
        String errMsg = WYNSDepend.getAppContext().getString(R.string.load_error);
        if (e instanceof HttpException) {
            int state = ((HttpException) e).code();
            if (state == 403) {
                errMsg = WYNSDepend.getAppContext().getString(R.string.retry_after);
            }
        }
        return errMsg;
    }
}
