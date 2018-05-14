package com.ttt.chat_module.models;

import android.content.SharedPreferences;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class UserProfile implements Serializable{
    private String id;
    private String avatarUrl;
    private String coverUrl;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String phone;

    public UserProfile() {
    }

    public UserProfile(String id,
                       String avatarUrl, String coverUrl,
                       String firstName, String lastName,
                       Date dateOfBirth, String email, String phone) {
        this.id = id;
        this.avatarUrl = avatarUrl;
        this.coverUrl = coverUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
    }

    public UserProfile(SharedPreferences sharedPreferences) {
        setId(sharedPreferences.getString(User.ID, null));
        setAvatarUrl(sharedPreferences.getString(User.AVATAR_URL, null));
        setCoverUrl(sharedPreferences.getString(User.COVER_URL, null));
        setFirstName(sharedPreferences.getString(User.FIRST_NAME, null));
        setLastName(sharedPreferences.getString(User.LAST_NAME, null));
        long dateOfBirth = sharedPreferences.getLong(User.DATE_OF_BIRTH, -1);
        if (dateOfBirth != -1) {
            setDateOfBirth(new Date(dateOfBirth));
        }
        setEmail(sharedPreferences.getString(User.EMAIL, null));
        setPhone(sharedPreferences.getString(User.PHONE,null));
    }

    @Exclude
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void writeToSharePreferences(SharedPreferences.Editor editor) {
        editor.putString(User.EMAIL, getEmail());
        editor.putString(User.FIRST_NAME, getFirstName());
        editor.putString(User.LAST_NAME, getLastName());
        editor.putString(User.AVATAR_URL, getAvatarUrl());
        editor.putString(User.COVER_URL, getCoverUrl());
        editor.putString(User.PHONE, getPhone());
        Date dateOfBirth = getDateOfBirth();
        if (dateOfBirth != null) {
            editor.putLong(User.DATE_OF_BIRTH, dateOfBirth.getTime());
        }
    }
}
