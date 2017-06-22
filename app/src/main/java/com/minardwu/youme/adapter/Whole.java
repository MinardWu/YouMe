package com.minardwu.youme.adapter;


import java.io.Serializable;

public class Whole implements Serializable {

    private int love;
    private String time;
    private String photoID;
    private String pohotUrl;
    private String author1;
    private String author2;
    private String author1ID;
    private String author2ID;
    private String author1PortraitUrl;
    private String author2PortraitUrl;
    private String author1installationID;
    private String author2installationID;


    public Whole(String photoID, String author1ID, String author2ID, String author1installationID,String author2installationID,int love, String time, String author1, String author2, String pohotUrl, String author1PortraitUrl, String author2PortraitUrl) {
        this.photoID = photoID;
        this.author1ID = author1ID;
        this.author2ID = author2ID;
        this.love = love;
        this.time = time;
        this.author1 = author1;
        this.author2 = author2;
        this.pohotUrl = pohotUrl;
        this.author1PortraitUrl = author1PortraitUrl;
        this.author2PortraitUrl = author2PortraitUrl;
        this.author1installationID = author1installationID;
        this.author2installationID = author2installationID;
    }

    public String getAuthor1ID() {
        return author1ID;
    }


    public String getAuthor2ID() {
        return author2ID;
    }


    public String getPhotoID() {
        return photoID;
    }


    public String getAuthor1() {
        return author1;
    }


    public String getAuthor2PortraitUrl() {
        return author2PortraitUrl;
    }


    public String getAuthor1PortraitUrl() {
        return author1PortraitUrl;
    }


    public String getAuthor2() {
        return author2;
    }


    public int getLove() {
        return love;
    }


    public String getPohotUrl() {
        return pohotUrl;
    }


    public String getTime() {
        return time;
    }

    public String getAuthor1installationID() {
        return author1installationID;
    }

    public String getAuthor2installationID() {
        return author2installationID;
    }

    public void setLove(int love) {
        this.love = love;
    }
}
