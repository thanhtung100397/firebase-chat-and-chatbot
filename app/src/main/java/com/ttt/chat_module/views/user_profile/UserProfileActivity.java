package com.ttt.chat_module.views.user_profile;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.user_profile.UserProfilePresenter;
import com.ttt.chat_module.presenters.user_profile.UserProfilePresenterImpl;
import com.ttt.chat_module.views.base.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends BaseActivity<UserProfilePresenter> implements UserProfileView {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.img_cover)
    ImageView imgCover;
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.txt_name)
    TextView txtFullName;

    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.txt_birthday)
    TextView txtBirthday;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_user_profile;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setTitle(R.string.user_profile);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
            }
            break;

            default:{
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String userID = getIntent().getStringExtra(Constants.KEY_USER_ID);
        if(userID != null) {
            getPresenter().fetchUserProfile(userID);
        }
    }

    @Override
    protected UserProfilePresenter initPresenter() {
        return new UserProfilePresenterImpl(this, this);
    }

    @Override
    public void showUserProfile(User user) {
        GlideApp.with(this)
                .load(user.getCoverUrl())
                .placeholder(R.drawable.wall_placeholder)
                .into(imgCover);

        GlideApp.with(this)
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(imgAvatar);

        txtFullName.setText(user.getLastName() + " " + user.getFirstName());
        txtEmail.setText(user.getEmail());
        if (user.getPhone() != null) {
            txtPhone.setText(user.getPhone());
        } else {
            txtPhone.setText(Constants.NOT_AVAILABLE_SYMBOL);
        }

        if (user.getDateOfBirth() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY_DATE_FORMAT, Locale.US);
            txtBirthday.setText(simpleDateFormat.format(user.getDateOfBirth()));
        } else {
            txtBirthday.setText(Constants.NOT_AVAILABLE_SYMBOL);
        }
    }
}
