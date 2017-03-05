package com.example.wy.newsstand.mvp.v;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.wy.newsstand.R;
import com.example.wy.newsstand.bean.NewsChannelTable;
import com.example.wy.newsstand.com.Constants;
import com.example.wy.newsstand.event.ChannelChangedEvent;
import com.example.wy.newsstand.mvp.BaseView;
import com.example.wy.newsstand.mvp.NSActivity;
import com.example.wy.newsstand.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class HomeActivity extends NSActivity
        implements NewsView {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.add_channel_iv)
    ImageView mAddChannelIv;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.nav_view)
    NavigationView NavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Inject
    HomePresenterImpl mNewsPresenter;

    private List<Fragment> mNewsFragmentList = new ArrayList<>();

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {

    }

    @Override
    protected void initViews() {
        mNavView = NavView;

        mPresenter = mNewsPresenter;
        mPresenter.attachView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initInjector() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(ChannelChangedEvent.class).subscribe(new Action1<ChannelChangedEvent>() {
            @Override
            public void call(ChannelChangedEvent channelChangedEvent) {
                mNewsPresenter.onChannelDbChanged();
            }
        });
    }

    @OnClick({R.id.fab, R.id.add_channel_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
               /* RxBus.getInstance().post(new ScrollToTopEvent());*/
                break;
            case R.id.add_channel_iv:
                Intent intent = new Intent(this, NewsChannelActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void initViewPager(List<NewsChannelTable> newsChannelTables) {
        final List<String> channelNames = new ArrayList<>();
        if (newsChannelTables != null) {
            setNewsList(newsChannelTables, channelNames);
            setViewPager(channelNames);
        }
    }

    private void setNewsList(List<NewsChannelTable> newsChannels, List<String> channelNames) {
        mNewsFragmentList.clear();
        for (NewsChannelTable newsChannel : newsChannels) {
            NewsListFragment newsListFragment = createListFragments(newsChannel);
            mNewsFragmentList.add(newsListFragment);
            channelNames.add(newsChannel.getNewsChannelName());
        }
    }

    private NewsListFragment createListFragments(NewsChannelTable newsChannel) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NEWS_ID, newsChannel.getNewsChannelId());
        bundle.putString(Constants.NEWS_TYPE, newsChannel.getNewsChannelType());
        bundle.putInt(Constants.CHANNEL_POSITION, newsChannel.getNewsChannelIndex());
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setViewPager(List<String> channelNames) {
        NewsFragmentPagerAdapter adapter = new NewsFragmentPagerAdapter(
                getSupportFragmentManager(), channelNames, mNewsFragmentList);
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);
        MyUtils.dynamicSetTabLayoutMode(mTabs);
//        mTabs.setTabsFromPagerAdapter(adapter);
        setPageChangeListener();

        mChannelNames = channelNames;
        int currentViewPagerPosition = getCurrentViewPagerPosition();
        mViewPager.setCurrentItem(currentViewPagerPosition, false);
    }
}
