package com.minardwu.youme.adapter;

import java.io.Serializable;

/**
 * Created by MinardWu on 2016/3/15.
 */
public class ForNews implements Serializable {

    private String userID;
    private String username;
    private String portraitUrl;
    private String type;
    private String photoUrl;

    public ForNews(String userID, String username, String ortraitUrl,String type,String photoUrl) {
        this.userID = userID;
        this.portraitUrl = ortraitUrl;
        this.username = username;
        this.type = type;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }

    public String getType() {
        return type;
    }
}
