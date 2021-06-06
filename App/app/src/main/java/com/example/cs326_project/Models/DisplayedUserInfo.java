package com.example.cs326_project.Models;

import com.google.firebase.auth.FirebaseUser;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

public class DisplayedUserInfo implements Serializable {
    private String user_name;
    private String user_image_url;
    private String user_email;
    private String user_contact;

    public DisplayedUserInfo(String user_name, String user_image_url, String user_email, String user_contact) {
        this.user_name = user_name;
        this.user_image_url = user_image_url;
        this.user_email = user_email;
        this.user_contact = user_contact;
    }


    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }

    public String get_email() {
        return user_email;
    }

    public void set_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String android_version_name) {
        this.user_name = android_version_name;
    }

    public String getUser_image_url() {
        return user_image_url;
    }

    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }


}
