package com.example.wy.newsstand;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;

import com.example.wy.newsstand.utils.SharedPreferenceUtils;
import com.example.wy.newsstand.com.Constants;
import com.example.wy.newsstand.greendao.DaoMaster;
import com.example.wy.newsstand.greendao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wy on 16-12-25.
 */

public class WYNSDepend {
    private  Context mAppContext;
    private static WYNSDepend impl;
    private DaoSession mDaoSession;

    public static void setup(WYNSApplication context){
        impl = new WYNSDepend();
        impl.mAppContext = context;
        impl.initActivityLifecycleCallback(context);
       // impl.initStrictMode();
        impl.initDayNightMode();
        impl.setupDatabase();
    }

    public static float dp2px(float dp) {
        final float scale = impl.mAppContext.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(float sp) {
        final float scale = impl.mAppContext.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int getScreenSize() {
        return impl.mAppContext.getResources().getDisplayMetrics().widthPixels;
    }

    private   void initActivityLifecycleCallback(WYNSApplication application){
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            private String TAG = "ActivityLifecycle";
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.i(TAG, activity+"onActivityCreated: ");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.i(TAG,activity+ "onActivityStarted: ");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.i(TAG, activity + "onActivityResumed: ");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.i(TAG, activity + "onActivityPaused: ");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.i(TAG, activity + "onActivityStopped: ");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.i(TAG, activity + "onActivitySaveInstanceState: ");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.i(TAG, activity + "onActivityDestroyed: ");
            }
        });
    }

    private void initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyDialog() // 弹出违规提示对话框
                            .penaltyLog() // 在logcat中打印违规异常信息
                            .build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build());
        }
    }

    private void initDayNightMode() {
        if (SharedPreferenceUtils.isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static Context getAppContext(){
        return impl.mAppContext;
    }

    private void setupDatabase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mAppContext, Constants.DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = BuildConfig.DEBUG;
        QueryBuilder.LOG_VALUES = BuildConfig.DEBUG;
    }

    public static DaoSession getDaoSession(){
        return impl.mDaoSession;
    }

    public static int getColor(int nightColor, int dayColor) {
        int color;
        if (!SharedPreferenceUtils.isNightMode()) {
            color = nightColor;
        } else {
            color = dayColor;
        }
        return color;
    }

    // click time
    private static long mLastClickTime = 0;
    private static final int SPACE_TIME = 500;

    public static boolean isFastDoubleClick() {
        long time = SystemClock.elapsedRealtime();
        if (time - mLastClickTime <= SPACE_TIME) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }

    public static void dynamicSetTabLayoutMode(TabLayout tabLayout) {
        int tabWidth = calculateTabWidth(tabLayout);
        int screenWidth = getScreenWith();

        if (tabWidth <= screenWidth) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    public static int getScreenWith() {
        return impl.mAppContext.getResources().getDisplayMetrics().widthPixels;
    }

    private static int calculateTabWidth(TabLayout tabLayout) {
        int tabWidth = 0;
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            final View view = tabLayout.getChildAt(i);
            view.measure(0, 0); // 通知父view测量，以便于能够保证获取到宽高
            tabWidth += view.getMeasuredWidth();
        }
        return tabWidth;
    }

    /**
     * from yyyy-MM-dd HH:mm:ss to MM-dd HH:mm
     */
    public static String formatDate(String before) {
        String after;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .parse(before);
            after = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            Log.e("formatDate","转换新闻日期格式异常：" + e.toString());
            return before;
        }
        return after;
    }

    public static boolean isHavePhoto() {
        return SharedPreferenceUtils.getSharedPreferences().getBoolean(Constants.SHOW_NEWS_PHOTO, true);
    }

}
