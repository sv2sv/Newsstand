package com.example.wy.newsstand.mvp.v;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.example.wy.newsstand.R;
import com.example.wy.newsstand.adapter.PhotoPagerAdapter;
import com.example.wy.newsstand.bean.NewsPhotoDetail;
import com.example.wy.newsstand.com.Constants;
import com.example.wy.newsstand.event.PhotoDetailOnClickEvent;
import com.example.wy.newsstand.mvp.NSActivity;
import com.example.wy.newsstand.utils.RxBus;
import com.example.wy.newsstand.weight.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wy on 2017/2/10.
 */

public class NewsPhotoDetailActivity extends NSActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    PhotoViewPager mViewpager;
    @BindView(R.id.photo_detail_title_tv)
    TextView mPhotoDetailTitleTv;

    private List<PhotoDetailFragment> mPhotoDetailFragmentList = new ArrayList<>();
    private NewsPhotoDetail mNewsPhotoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(PhotoDetailOnClickEvent.class)
                .subscribe(new Action1<PhotoDetailOnClickEvent>() {
                    @Override
                    public void call(PhotoDetailOnClickEvent photoDetailOnClickEvent) {
                        if (mPhotoDetailTitleTv.getVisibility() == View.VISIBLE) {
                            startAnimation(View.GONE, 0.9f, 0.5f);
                        } else {
                            mPhotoDetailTitleTv.setVisibility(View.VISIBLE);
                            startAnimation(View.VISIBLE, 0.5f, 0.9f);
                        }
                    }
                });
    }

    private void startAnimation(final int endState, float startValue, float endValue) {
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(mPhotoDetailTitleTv, "alpha", startValue, endValue)
                .setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mPhotoDetailTitleTv.setVisibility(endState);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_photo_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        mNewsPhotoDetail = getIntent().getParcelableExtra(Constants.PHOTO_DETAIL);
        createFragment(mNewsPhotoDetail);
        initViewPager();
        setPhotoDetailTitle(0);
    }

    private void createFragment(NewsPhotoDetail newsPhotoDetail) {
        mPhotoDetailFragmentList.clear();
        for (NewsPhotoDetail.Picture picture : newsPhotoDetail.getPictures()) {
            PhotoDetailFragment fragment = new PhotoDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PHOTO_DETAIL_IMGSRC, picture.getImgSrc());
            fragment.setArguments(bundle);
            mPhotoDetailFragmentList.add(fragment);
        }
    }

    private void initViewPager() {
        PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), mPhotoDetailFragmentList);
        mViewpager.setAdapter(photoPagerAdapter);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPhotoDetailTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setPhotoDetailTitle(int position) {
        String title = getTitle(position);
        mPhotoDetailTitleTv.setText(getString(R.string.photo_detail_title, position + 1,
                mPhotoDetailFragmentList.size(), title));
    }

    private String getTitle(int position) {
        String title = mNewsPhotoDetail.getPictures().get(position).getTitle();
        if (title == null) {
            title = mNewsPhotoDetail.getTitle();
        }
        return title;
    }

}
