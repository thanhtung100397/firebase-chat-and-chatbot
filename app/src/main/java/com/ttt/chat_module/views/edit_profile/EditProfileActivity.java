package com.ttt.chat_module.views.edit_profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.custom_view.ClearableEditText;
import com.ttt.chat_module.common.custom_view.ClearableNotEditableText;
import com.ttt.chat_module.common.custom_view.LoadingDialog;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.common.utils.Utils;
import com.ttt.chat_module.models.UserProfile;
import com.ttt.chat_module.presenters.edit_profile.EditProfilePresenter;
import com.ttt.chat_module.presenters.edit_profile.EditProfilePresenterImpl;
import com.ttt.chat_module.views.base.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity<EditProfilePresenter> implements EditProfileView, View.OnClickListener {
    private static final int REQUEST_CODE_PICK_AVATAR = 1;
    private static final int REQUEST_CODE_PICK_COVER = 2;

    @BindView(R.id.btn_avatar)
    ImageButton buttonAvatar;
    @BindView(R.id.btn_cover)
    ImageButton buttonCover;

    @BindView(R.id.img_avatar)
    CircleImageView imageAvatar;
    @BindView(R.id.img_cover)
    ImageView imageCover;

    @BindView(R.id.edt_last_name)
    ClearableEditText editLastName;
    @BindView(R.id.edt_first_name)
    ClearableEditText editFirstName;
    @BindView(R.id.edt_birthday)
    ClearableNotEditableText editBirthday;
    @BindView(R.id.edt_email)
    ClearableEditText editEmail;
    @BindView(R.id.edt_phone)
    ClearableEditText editPhone;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private LoadingDialog loadingDialog;

    private UserProfile userProfile;
    private Map<String, Uri> uploadedImages;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setTitle(R.string.edit_profile);
        }

        buttonAvatar.setOnClickListener(this);
        buttonCover.setOnClickListener(this);

        loadingDialog = new LoadingDialog(this);

        editBirthday.setOnEditTextClickListener(v -> {
            Date dateOfBirth = userProfile.getDateOfBirth();
            if(dateOfBirth == null) {
                dateOfBirth = new Date();
            }
            long birthdayLong = dateOfBirth.getTime();
            Calendar calendar = Calendar.getInstance();
            if (birthdayLong != -1) {
                calendar.setTimeInMillis(birthdayLong);
            }
            new DatePickerDialog(EditProfileActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        userProfile.setDateOfBirth(calendar.getTime());
                        editBirthday.setText(Utils.getDateFromYearMonthDay(year, month + 1, dayOfMonth));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        uploadedImages = new HashMap<>(2);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userProfile = (UserProfile) getIntent().getSerializableExtra(Constants.KEY_USER_PROFILE);
        addDataToForm(userProfile);
    }

    public void addDataToForm(UserProfile userProfile) {
        GlideApp.with(this)
                .load(userProfile.getCoverUrl())
                .placeholder(R.drawable.wall_placeholder)
                .into(imageCover);

        GlideApp.with(this)
                .load(userProfile.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(imageAvatar);

        editLastName.setText(userProfile.getLastName());
        editFirstName.setText(userProfile.getFirstName());

        Date birthday = userProfile.getDateOfBirth();
        if (birthday != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY_DATE_FORMAT, Locale.US);
            editBirthday.setText(simpleDateFormat.format(birthday));
        }

        editEmail.setText(userProfile.getEmail());
        editPhone.setText(userProfile.getPhone());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
            break;

            case R.id.action_submit: {
                getPresenter().validateUserProfile(uploadedImages, userProfile.getId(),
                        userProfile.getAvatarUrl(), userProfile.getCoverUrl(),
                        editFirstName.getText(), editLastName.getText(),
                        userProfile.getDateOfBirth(),
                        editEmail.getText(),
                        editPhone.getText());
            }
            break;

            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected EditProfilePresenter initPresenter() {
        return new EditProfilePresenterImpl(this, this);
    }

    @Override
    public void showFistNameInputError(String message) {
        editFirstName.setError(message);
    }

    @Override
    public void showLastNameInputError(String message) {
        editLastName.setError(message);
    }

    @Override
    public void showEmailInputError(String message) {
        editEmail.setText(message);
    }

    @Override
    public void showPhoneInputError(String message) {
        editPhone.setError(message);
    }

    @Override
    public void showProgress() {
        loadingDialog.show();
    }

    @Override
    public void hideProgress() {
        loadingDialog.dismiss();
    }

    @Override
    public void navigateToProfileScreen(UserProfile userProfile) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_USER_PROFILE, userProfile);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_avatar: {
                FishBun.with(this)
                        .setImageAdapter(new GlideAdapter())
                        .setMaxCount(1)
                        .setMinCount(1)
                        .setActionBarColor(getResources().getColor(R.color.colorPrimary),
                                getResources().getColor(R.color.colorPrimaryDark),
                                false)
                        .setActionBarTitleColor(getResources().getColor(android.R.color.white))
                        .setButtonInAlbumActivity(false)
                        .setCamera(true)
                        .exceptGif(true)
                        .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_back))
                        .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_tick))
                        .setAllViewTitle(getResources().getString(R.string.selected))
                        .setActionBarTitle(getResources().getString(R.string.pick_avatar))
                        .textOnNothingSelected(getResources().getString(R.string.must_pick_one_image))
                        .setRequestCode(REQUEST_CODE_PICK_AVATAR)
                        .startAlbum();

            }
            break;

            case R.id.btn_cover: {
                FishBun.with(this)
                        .setImageAdapter(new GlideAdapter())
                        .setMaxCount(1)
                        .setMinCount(1)
                        .setActionBarColor(getResources().getColor(R.color.colorPrimary),
                                getResources().getColor(R.color.colorPrimaryDark),
                                false)
                        .setActionBarTitleColor(getResources().getColor(android.R.color.white))
                        .setButtonInAlbumActivity(false)
                        .setCamera(true)
                        .exceptGif(true)
                        .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_back))
                        .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_tick))
                        .setAllViewTitle(getResources().getString(R.string.selected))
                        .setActionBarTitle(getResources().getString(R.string.pick_cover))
                        .textOnNothingSelected(getResources().getString(R.string.must_pick_one_image))
                        .setRequestCode(REQUEST_CODE_PICK_COVER)
                        .startAlbum();

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
            case REQUEST_CODE_PICK_AVATAR: {
                if (resultCode == RESULT_OK) {
                    ArrayList<Uri> avatarPaths = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    Uri avatarUri = avatarPaths.get(0);
                    uploadedImages.put(Constants.FIREBASE_AVATAR_FILE_NAME, avatarUri);

                    GlideApp.with(this)
                            .load(avatarUri)
                            .placeholder(R.drawable.avatar_placeholder)
                            .into(imageAvatar);
                }
            }
            break;

            case REQUEST_CODE_PICK_COVER: {
                if (resultCode == RESULT_OK) {
                    ArrayList<Uri> coverPaths = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    Uri coverUri = coverPaths.get(0);
                    uploadedImages.put(Constants.FIREBASE_COVER_FILE_NAME, coverUri);

                    GlideApp.with(this)
                            .load(coverUri)
                            .placeholder(R.drawable.wall_placeholder)
                            .into(imageCover);
                }
            }
            break;

            default: {
                break;
            }
        }
    }
}
