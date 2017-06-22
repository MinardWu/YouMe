package com.minardwu.youme.adapter;

import android.graphics.Bitmap;

import com.avos.avoscloud.AVUser;

import java.io.Serializable;

/**
 * Created by MinardWu on 2016/3/9.
 */
public class Half implements Serializable {
    private int love;
    private String time;
    private String photoID;
    private String pohotUrl;
    private String position;
    private String authorName;
    private String authorID;
    private String authorPortraitUrl;
    private String authorinstallationID;
    private byte[] bytes;

    public Half(String photoID, String pohotUrl, String authorID, String authorName, String authorPortraitUrl,String authorinstallationID, int love, String time, String position, byte[] bytes) {
        this.authorID = authorID;
        this.authorName = authorName;
        this.authorPortraitUrl = authorPortraitUrl;
        this.authorinstallationID = authorinstallationID;
        this.photoID = photoID;
        this.pohotUrl = pohotUrl;
        this.position = position;
        this.love = love;
        this.time = time;
        this.bytes = bytes;
    }

    public String getAuthorID() {
        return authorID;
    }


    public String getAuthorName() {
        return authorName;
    }


    public String getAuthorPortraitUrl() {
        return authorPortraitUrl;
    }

    public String getPosition() {
        return position;
    }


    public int getLove() {
        return love;
    }


    public String getPhotoID() {
        return photoID;
    }

    public String getAuthorinstallationID() {
        return authorinstallationID;
    }

    public String getPohotUrl() {
        return pohotUrl;
    }


    public String getTime() {
        return time;
    }


    public byte[] getBytes() {
        return bytes;
    }

    public void setLove(int love) {
        this.love = love;
    }
}
