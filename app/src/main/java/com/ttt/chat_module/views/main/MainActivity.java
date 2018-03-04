package com.ttt.chat_module.views.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.adapter.view_pager_adapter.HomeFragmentPagerAdapter;
import com.ttt.chat_module.presenters.BasePresenter;
import com.ttt.chat_module.views.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        HomeFragmentPagerAdapter homeFragmentPagerAdapter =
                new HomeFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(homeFragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.setSelectedItemId(HomeFragmentPagerAdapter.getMenuIDByPosition(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:{
                toolbar.setTitle(R.string.title_home);
                viewPager.setCurrentItem(HomeFragmentPagerAdapter.HOME_FRAGMENT_POSITION);
            }
            break;

            case R.id.navigation_friends:{
                toolbar.setTitle(R.string.title_friends);
                viewPager.setCurrentItem(HomeFragmentPagerAdapter.FRIENDS_FRAGMENT_POSITION);
            }
            break;

            case R.id.navigation_account:{
                toolbar.setTitle(R.string.title_account);
                viewPager.setCurrentItem(HomeFragmentPagerAdapter.ACCOUNT_FRAGMENT_POSITION);
            }
            break;

            default:{
                break;
            }
        }
        return true;
    }
}
