package com.ttt.chat_module.models;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.Serializable;

/**
 * Created by TranThanhTung on 18/03/2018.
 */

public class UserInfo implements Parcelable, Serializable{
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String AVATAR_URL = "avatarUrl";

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private boolean isOnline;
    private String fcmToken;

    public UserInfo(User user) {
        setId(user.getId());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setAvatarUrl(user.getAvatarUrl());
        setIsOnline(user.getIsOnline());
        setFcmToken(user.getFcmToken());
    }

    public UserInfo(SharedPreferences sharedPreferences) {
        setId(sharedPreferences.getString(ID, null));
        setEmail(sharedPreferences.getString(EMAIL, null));
        setFirstName(sharedPreferences.getString(FIRST_NAME, null));
        setLastName(sharedPreferences.getString(LAST_NAME, null));
        setAvatarUrl(sharedPreferences.getString(AVATAR_URL, null));
        setFcmToken(FirebaseInstanceId.getInstance().getToken());
    }

    public UserInfo() {
    }

    public void update(UserInfo userInfo) {
        setId(userInfo.getId());
        setEmail(userInfo.getEmail());
        setFirstName(userInfo.getFirstName());
        setLastName(userInfo.getLastName());
        setAvatarUrl(userInfo.getAvatarUrl());
        setIsOnline(userInfo.getIsOnline());
        setFcmToken(userInfo.getFcmToken());
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Exclude
    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    protected UserInfo(Parcel in) {
        id = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        avatarUrl = in.readString();
        isOnline = in.readByte() != 0;
        fcmToken = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(avatarUrl);
        dest.writeByte((byte) (isOnline ? 1 : 0));
        dest.writeString(fcmToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
