package com.ttt.chat_module.models;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class User implements Parcelable {
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String AVATAR_URL = "avatarUrl";
    public static final String COVER_URL = "coverUrl";

    public static final String IS_ONLINE = "isOnline";

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String coverUrl;
    private boolean isOnline = false;

    public User(String id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(SharedPreferences sharedPreferences) {
        setId(sharedPreferences.getString(ID, null));
        setEmail(sharedPreferences.getString(EMAIL, null));
        setFirstName(sharedPreferences.getString(FIRST_NAME, null));
        setLastName(sharedPreferences.getString(LAST_NAME, null));
        setAvatarUrl(sharedPreferences.getString(AVATAR_URL, null));
        setCoverUrl(sharedPreferences.getString(COVER_URL, null));
    }

    public User() {
    }

    public void writeToSharePreferences(SharedPreferences.Editor editor) {
        editor.putString(ID, getId());
        editor.putString(EMAIL, getEmail());
        editor.putString(FIRST_NAME, getFirstName());
        editor.putString(LAST_NAME, getLastName());
        editor.putString(AVATAR_URL, getAvatarUrl());
        editor.putString(COVER_URL, getCoverUrl());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User) obj).email.equals(this.email);
    }

    @Override
    public int hashCode() {
        if (email != null) {
            return email.hashCode();
        }
        return super.hashCode();
    }

    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        avatarUrl = in.readString();
        coverUrl = in.readString();
        isOnline = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(avatarUrl);
        dest.writeString(coverUrl);
        dest.writeByte((byte) (isOnline ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
