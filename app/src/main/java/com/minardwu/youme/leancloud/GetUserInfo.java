package com.minardwu.youme.leancloud;

import android.content.Intent;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.minardwu.youme.base.App;
import com.minardwu.youme.base.User;
import com.minardwu.youme.br.GetHalfBitmapReceiver;
import com.minardwu.youme.br.GetUserNPReceiver;
import com.minardwu.youme.br.GetUserNumberReceiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MinardWu on 2016/3/5.
 */
public class GetUserInfo {

    public static void getUsernameAndPortraitUrl(final String userID){
        final List<String> list_NP = new ArrayList<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AVObject user = new AVObject("_User");
                AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
                try {
                    user = query.get(userID);
                    list_NP.add(user.getString("username"));
                    if(user.getAVFile("UserPortrait")==null){
                        list_NP.add("http://ac-b2cy6yl4.clouddn.com/fdb31720f58cd084.jpg");
                    }else {
                        list_NP.add(user.getAVFile("UserPortrait").getUrl());
                    }
                    list_NP.add(user.getString("gender"));
                    Intent intent = new Intent(GetUserNPReceiver.ACTION);
                    intent.putExtra("getUsernameAndPortraitUrl", (Serializable) list_NP);
                    App.getContext().sendBroadcast(intent);
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static void getUserNumber(final String userID){
        final List<String> list_Number = new ArrayList<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> query = new AVQuery<AVObject>("Number");
                query.whereEqualTo("userID",userID);
                query.findInBackground(new FindCallback<AVObject>() {
                    public void done(List<AVObject> avObjects, AVException e) {
                        if (e == null) {
                            list_Number.add(avObjects.get(0).getInt("love")+"");
                            list_Number.add(avObjects.get(0).getInt("follower")+"");
                            list_Number.add(avObjects.get(0).getInt("followee")+"");
                            Intent intent = new Intent(GetUserNumberReceiver.ACTION);
                            intent.putExtra("getUserNumber", (Serializable) list_Number);
                            App.getContext().sendBroadcast(intent);
                        } else {

                        }
                    }
                });
            }
        }).start();

    }

}
