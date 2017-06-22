package com.minardwu.youme.adapter;

import java.io.Serializable;

/**
 * Created by MinardWu on 2016/3/14.
 */
public class ForFollow implements Serializable {

    private String userID;
    private String username;
    private String portraitUrl;

    public ForFollow(String userID, String username, String ortraitUrl) {
        this.userID = userID;
        this.portraitUrl = ortraitUrl;
        this.username = username;
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
}
