package com.ttt.chat_module.views.main.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.UserProfile;
import com.ttt.chat_module.presenters.BasePresenter;
import com.ttt.chat_module.views.base.fragment.BaseFragment;
import com.ttt.chat_module.views.edit_profile.EditProfileActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public class AccountFragment extends BaseFragment implements AccountView, View.OnClickListener {
    private static final int EDIT_PROFILE_RESULT_CODE = 0;

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

    private UserProfile userProfile;

    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        ButterKnife.bind(this, rootView);

        rootView.findViewById(R.id.fab_edit).setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        userProfile = UserAuth.getProfile(getActivity());
        updateUserProfile(userProfile);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    public void updateUserProfile(UserProfile userProfile) {
        Context context = getActivity();

        GlideApp.with(context)
                .load(userProfile.getCoverUrl())
                .placeholder(R.drawable.wall_placeholder)
                .into(imgCover);

        GlideApp.with(context)
                .load(userProfile.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(imgAvatar);

        txtFullName.setText(userProfile.getLastName() + " " + userProfile.getFirstName());
        txtEmail.setText(userProfile.getEmail());
        if (userProfile.getPhone() != null) {
            txtPhone.setText(userProfile.getPhone());
        } else {
            txtPhone.setText(Constants.NOT_AVAILABLE_SYMBOL);
        }

        if (userProfile.getDateOfBirth() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY_DATE_FORMAT, Locale.US);
            txtBirthday.setText(simpleDateFormat.format(userProfile.getDateOfBirth()));
        } else {
            txtBirthday.setText(Constants.NOT_AVAILABLE_SYMBOL);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_edit: {
                Context context = getContext();
                Intent intent = new Intent(context, EditProfileActivity.class);
                intent.putExtra(Constants.KEY_USER_PROFILE, UserAuth.getProfile(context));
                startActivityForResult(intent, EDIT_PROFILE_RESULT_CODE);
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_PROFILE_RESULT_CODE: {
                if (resultCode == Activity.RESULT_OK) {
                    this.userProfile = (UserProfile) data.getSerializableExtra(Constants.KEY_USER_PROFILE);
                    updateUserProfile(userProfile);
                }
            }
            break;

            default: {
                break;
            }
        }
    }
}
