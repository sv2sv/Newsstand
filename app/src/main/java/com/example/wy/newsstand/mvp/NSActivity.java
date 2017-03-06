package com.example.wy.newsstand.mvp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.wy.newsstand.R;
import com.example.wy.newsstand.WYNSApplication;
import com.example.wy.newsstand.com.Constants;
import com.example.wy.newsstand.ioc.component.ActivityComponent;
import com.example.wy.newsstand.ioc.component.DaggerActivityComponent;
import com.example.wy.newsstand.ioc.module.ActivityModule;
import com.example.wy.newsstand.mvp.v.HomeActivity;
import com.example.wy.newsstand.utils.SharedPreferenceUtils;

import butterknife.ButterKnife;
import rx.Subscription;



public abstract  class NSActivity<T extends BasePresenter> extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected ActivityComponent mActivityComponent;
    private boolean mIsChangeTheme;
    public Activity mActivity;
    protected NavigationView mNavView;
    private DrawerLayout mDrawerLayout;
    protected T mPresenter;
    protected boolean mIsHasNavigationView = false;
    private Class mClass;
    protected Subscription mSubscription;

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
        Toast.makeText(this,"oncreate",Toast.LENGTH_LONG).show();
        initActivityComponent();
        int layoutId = getLayoutId();
        setContentView(layoutId);
        ButterKnife.bind(this);
        initInjector();
        initToolBar();
        initViews();
        if(this instanceof HomeActivity){
            mIsHasNavigationView = true;
        }
        if(mIsHasNavigationView){
        initDrawerLayout();
           }
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
        initNightModeSwitch();
    }



    private void initActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((WYNSApplication)getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }


    private void initNightModeSwitch() {
        if (this instanceof HomeActivity) {
            MenuItem menuNightMode = mNavView.getMenu().findItem(R.id.nav_night_mode);
            SwitchCompat dayNightSwitch = (SwitchCompat) MenuItemCompat
                    .getActionView(menuNightMode);
            setCheckedState(dayNightSwitch);
            setCheckedEvent(dayNightSwitch);
        }
    }

    private void setCheckedEvent(SwitchCompat dayNightSwitch) {
        dayNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferenceUtils.setBoolean(mActivity, Constants.ISNIGHT, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    SharedPreferenceUtils.setBoolean(mActivity, Constants.ISNIGHT, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                mIsChangeTheme = true;
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void setCheckedState(SwitchCompat dayNightSwitch) {
        boolean isNight = SharedPreferenceUtils.getBoolean(this, Constants.ISNIGHT, false);
        if (isNight) {
            dayNightSwitch.setChecked(true);
        } else {
            dayNightSwitch.setChecked(false);
        }
    }
    protected abstract void initViews();

    protected abstract int getLayoutId();

    public abstract void initInjector();

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (navView != null) {
            navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch (item.getItemId()) {
                       /* case R.id.nav_news:
                            mClass = NewsActivity.class;
                            break;
                        *//*case R.id.nav_photo:
                            //mClass = PhotoActivity.class;
                            break;
                        case R.id.nav_video:
                            Toast.makeText(BaseActivity.this, "施工准备中...", Toast.LENGTH_SHORT).show();
                            break;*//*
                        case R.id.nav_about:
                            mClass = AboutActivity.class;
                            break;
                        case R.id.nav_night_mode:
                            break;*/
                    }
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        }

        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (mClass != null) {
                    Intent intent = new Intent(NSActivity.this, mClass);
                    // 此标志用于启动一个Activity的时候，若栈中存在此Activity实例，则把它调到栈顶。不创建多一个
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    mClass = null;
                }

                if (mIsChangeTheme) {
                    mIsChangeTheme = false;
                    getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                    recreate();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
