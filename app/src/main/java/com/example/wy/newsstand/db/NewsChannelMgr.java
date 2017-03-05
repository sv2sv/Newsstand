package com.example.wy.newsstand.db;

/**
 * Created by wy on 17-3-5.
 */



import java.util.Arrays;
import java.util.List;


import com.example.wy.newsstand.R;
import com.example.wy.newsstand.WYNSApplication;
import com.example.wy.newsstand.WYNSDepend;
import com.example.wy.newsstand.bean.NewsChannelTable;
import com.example.wy.newsstand.com.Constants;
import com.example.wy.newsstand.com.NewApi;
import com.example.wy.newsstand.greendao.NewsChannelTableDao;
import com.example.wy.newsstand.utils.SharedPreferenceUtils;

import org.greenrobot.greendao.query.Query;


/**
 * Created by Eric on 2017/1/16.
 */

public class NewsChannelMgr {

    /**
     * 首次打开程序初始化db
     */
    public static void initDB() {
        if (!SharedPreferenceUtils.getSharedPreferences().getBoolean(Constants.INIT_DB, false)) {
            NewsChannelTableDao dao = WYNSDepend.getDaoSession().getNewsChannelTableDao();
            List<String> channelName = Arrays.asList(WYNSDepend.getAppContext().getResources()
                    .getStringArray(R.array.channel_name));
            List<String> channelId = Arrays.asList(WYNSDepend.getAppContext().getResources()
                    .getStringArray(R.array.channel_id));
            for (int i = 0; i < channelName.size(); i++) {
                NewsChannelTable entity = new NewsChannelTable(channelName.get(i), channelId.get(i)
                        , NewApi.getType(channelId.get(i)), i <= 5, i, i == 0);
                dao.insert(entity);
            }
            SharedPreferenceUtils.getSharedPreferences().edit().putBoolean(Constants.INIT_DB, true).apply();
        }
    }

    public static List<NewsChannelTable> loadNewsChannelsMine() {
        Query<NewsChannelTable> newsChannelTableQuery = WYNSDepend.getDaoSession().getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelSelect.eq(true))
                .orderAsc(NewsChannelTableDao.Properties.NewsChannelIndex).build();
        return newsChannelTableQuery.list();
    }

    public static List<NewsChannelTable> loadNewsChannelsMore() {
        Query<NewsChannelTable> newsChannelTableQuery = WYNSDepend.getDaoSession().getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelSelect.eq(false))
                .orderAsc(NewsChannelTableDao.Properties.NewsChannelIndex).build();
        return newsChannelTableQuery.list();
    }

    public static NewsChannelTable loadNewsChannel(int position) {
        return WYNSDepend.getDaoSession().getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex.eq(position)).build().unique();
    }

    public static void update(NewsChannelTable newsChannelTable) {
        WYNSDepend.getDaoSession().getNewsChannelTableDao().update(newsChannelTable);
    }

    public static List<NewsChannelTable> loadNewsChannelsWithin(int from, int to) {
        Query<NewsChannelTable> newsChannelTableQuery = WYNSDepend.getDaoSession().getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex
                        .between(from, to)).build();
        return newsChannelTableQuery.list();
    }

    public static List<NewsChannelTable> loadNewsChannelsIndexGt(int channelIndex) {
        Query<NewsChannelTable> newsChannelTableQuery = WYNSDepend.getDaoSession().getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex
                        .gt(channelIndex)).build();
        return newsChannelTableQuery.list();
    }

    public static List<NewsChannelTable> loadNewsChannelsIndexLtAndIsUnselect(int channelIndex) {
        Query<NewsChannelTable> newsChannelTableQuery = WYNSDepend.getDaoSession().getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex.lt(channelIndex),
                        NewsChannelTableDao.Properties.NewsChannelSelect.eq(false)).build();
        return newsChannelTableQuery.list();
    }

    public static int getAllSize() {
        return WYNSDepend.getDaoSession().getNewsChannelTableDao().loadAll().size();
    }

    public static int getNewsChannelSelectSize() {
        return (int) WYNSDepend.getDaoSession().getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelSelect.eq(true))
                .buildCount().count();
    }
}

