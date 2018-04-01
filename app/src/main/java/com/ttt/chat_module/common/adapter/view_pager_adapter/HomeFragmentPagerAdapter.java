package com.ttt.chat_module.common.adapter.view_pager_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ttt.chat_module.R;
import com.ttt.chat_module.views.main.account.AccountFragment;
import com.ttt.chat_module.views.main.friends.FriendsFragment;
import com.ttt.chat_module.views.main.home.HomeFragment;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    public static final int HOME_FRAGMENT_POSITION = 0;
    public static final int FRIENDS_FRAGMENT_POSITION = 1;
    public static final int PROFILE_FRAGMENT_POSITION = 2;

    private Fragment[] fragments;

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[3];

        fragments[HOME_FRAGMENT_POSITION] = new HomeFragment();
        fragments[FRIENDS_FRAGMENT_POSITION] = new FriendsFragment();
        fragments[PROFILE_FRAGMENT_POSITION] = new AccountFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    public static int getMenuIDByPosition(int position) {
        switch (position) {
            case HOME_FRAGMENT_POSITION:{
                return R.id.navigation_home;
            }

            case FRIENDS_FRAGMENT_POSITION:{
                return R.id.navigation_friends;
            }

            case PROFILE_FRAGMENT_POSITION:{
                return R.id.navigation_account;
            }

            default:{
                return R.id.navigation_home;
            }
        }
    }
}
