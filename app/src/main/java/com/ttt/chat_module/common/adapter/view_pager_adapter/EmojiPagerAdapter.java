package com.ttt.chat_module.common.adapter.view_pager_adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.views.emoji.EmojiFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmojiPagerAdapter extends FragmentPagerAdapter {
    private List<EmojiFragmentWrapper> emojiFragmentsWrapper;

    public EmojiPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        loadEmojiFolderFromAssets(context);
    }

    private void loadEmojiFolderFromAssets(Context context) {
        emojiFragmentsWrapper = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            String[] emojiTypes = assetManager.list(Constants.EMOJI_ROOT_ASSETS_FOLDER_PATH);
            String packageName = context.getPackageName();

            for (String emojiType : emojiTypes) {
                int tabIconID = context.getResources().getIdentifier(emojiType, "drawable", packageName);
                if (tabIconID == 0) {
                    tabIconID = R.drawable.face_emoji;
                }
                emojiFragmentsWrapper.add(new EmojiFragmentWrapper(tabIconID, emojiType));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupTabLayout(TabLayout tabLayout, ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(emojiFragmentsWrapper.get(i).tabIconID);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return emojiFragmentsWrapper.get(position).fragment;
    }

    @Override
    public int getCount() {
        return emojiFragmentsWrapper.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    private class EmojiFragmentWrapper {
        private int tabIconID;
        private EmojiFragment fragment;

        public EmojiFragmentWrapper(int tabIconID, String emojiType) {
            this.tabIconID = tabIconID;
            this.fragment = new EmojiFragment();
            Bundle emojiFragArgs = new Bundle();
            emojiFragArgs.putString(Constants.KEY_EMOJI_TYPE, emojiType);
            this.fragment.setArguments(emojiFragArgs);
        }
    }
}
