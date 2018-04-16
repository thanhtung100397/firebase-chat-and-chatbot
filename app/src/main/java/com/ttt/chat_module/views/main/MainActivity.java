package com.ttt.chat_module.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ttt.chat_module.AppStatusApplication;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.adapter.view_pager_adapter.HomeFragmentPagerAdapter;
import com.ttt.chat_module.common.custom_view.LoadingDialog;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.BasePresenter;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;
import com.ttt.chat_module.presenters.main.MainActivityPresenter;
import com.ttt.chat_module.presenters.main.MainActivityPresenterImpl;
import com.ttt.chat_module.views.auth.login.LoginActivity;
import com.ttt.chat_module.views.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainActivityPresenter> implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private LoadingDialog loadingDialog;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        loadingDialog = new LoadingDialog(this);

        MainActivityPresenter presenter = getPresenter();
        presenter.registerUsersOnlineStateChangeListener();
        presenter.registerChatRoomLastMessageChangeListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivityPresenter presenter = getPresenter();
        presenter.unregisterUsersOnlineStateChangeListener();
        presenter.unregisterChatRoomLastMessageChangeListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(Gravity.START);
            }
            break;

            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home: {
                    viewPager.setCurrentItem(HomeFragmentPagerAdapter.HOME_FRAGMENT_POSITION);
                    drawerLayout.closeDrawer(Gravity.START);
                    return true;
                }

                case R.id.nav_friends: {
                    viewPager.setCurrentItem(HomeFragmentPagerAdapter.FRIENDS_FRAGMENT_POSITION);
                    drawerLayout.closeDrawer(Gravity.START);
                    return true;
                }

                case R.id.nav_profile: {
                    viewPager.setCurrentItem(HomeFragmentPagerAdapter.PROFILE_FRAGMENT_POSITION);
                    drawerLayout.closeDrawer(Gravity.START);
                    return true;
                }

                case R.id.nav_logout: {
                    logout();
                    return false;
                }

                default: {
                    return false;
                }
            }
        }
    };

    private void logout() {
        loadingDialog.show();
        ((AppStatusApplication) getApplication()).getApplicationPresenter()
                .changeOnlineState(false, new OnRequestCompleteListener() {
                    @Override
                    public void onRequestSuccess() {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(UserAuth.getUserID());
                        FirebaseAuth.getInstance().signOut();
                        UserAuth.saveUser(MainActivity.this, null);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        loadingDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onRequestError(String message) {
                        loadingDialog.dismiss();
                        ToastUtils.quickToast(MainActivity.this, R.string.logout_failure);
                    }
                });

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
    protected MainActivityPresenter initPresenter() {
        return new MainActivityPresenterImpl();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home: {
                toolbar.setTitle(R.string.title_home);
                viewPager.setCurrentItem(HomeFragmentPagerAdapter.HOME_FRAGMENT_POSITION);
                navigationView.setCheckedItem(R.id.nav_home);
            }
            break;

            case R.id.navigation_friends: {
                toolbar.setTitle(R.string.title_friends);
                viewPager.setCurrentItem(HomeFragmentPagerAdapter.FRIENDS_FRAGMENT_POSITION);
                navigationView.setCheckedItem(R.id.nav_friends);
            }
            break;

            case R.id.navigation_account: {
                toolbar.setTitle(R.string.title_profile);
                viewPager.setCurrentItem(HomeFragmentPagerAdapter.PROFILE_FRAGMENT_POSITION);
                navigationView.setCheckedItem(R.id.nav_profile);
            }
            break;

            default: {
                break;
            }
        }
        return true;
    }
}
