package com.minardwu.youme.adapter;

/**
 * Created by MinardWu on 2016/3/5.
 */
public class VerifyUserInfo {

    private String title;
    private String value;
    private String portraitUrl;

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public VerifyUserInfo(String title,String value,String portraitUrl){
        this.title = title;
        this.value = value;
        this.portraitUrl = portraitUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
