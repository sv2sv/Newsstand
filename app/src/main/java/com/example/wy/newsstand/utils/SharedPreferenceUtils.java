package com.example.wy.newsstand.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.wy.newsstand.com.Constants;
import com.example.wy.newsstand.WYNSDepend;

/**
 * Created by wy on 17-3-5.
 */

public class SharedPreferenceUtils {
    public static boolean isNightMode() {
        SharedPreferences preferences = WYNSDepend.getAppContext().getSharedPreferences(
                Constants.SHARES_COLOURFUL_NEWS, Activity.MODE_PRIVATE
        );
        return preferences.getBoolean(Constants.NIGHT_THEME_MODE, false);
    }

    public static void saveTheme(boolean isNight) {
        SharedPreferences preferences = WYNSDepend.getAppContext().getSharedPreferences(
                Constants.SHARES_COLOURFUL_NEWS, Activity.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.NIGHT_THEME_MODE, isNight);
        editor.apply();
    }

    public static SharedPreferences getSharedPreferences() {
        return WYNSDepend.getAppContext()
                .getSharedPreferences(Constants.SHARES_COLOURFUL_NEWS, Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String key,
                                   final String defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    public static void setString(Context context, final String key,
                                 final String value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).apply();
    }

    public static boolean getBoolean(Context context, final String key,
                                     final boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, final String key,
                                  final boolean value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).apply();
    }

    public static void setInt(Context context, final String key,
                              final int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, final String key,
                             final int defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    public static void setFloat(Context context, final String key,
                                final float value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putFloat(key, value).apply();
    }

    public static float getFloat(Context context, final String key,
                                 final float defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getFloat(key, defaultValue);
    }

    public static void setLong(Context context, final String key,
                               final long value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putLong(key, value).apply();
    }

    public static long getLong(Context context, final String key,
                               final long defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getLong(key, defaultValue);
    }

    public static void clear(Context context,
                             final SharedPreferences p) {
        final SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.apply();
    }
}
