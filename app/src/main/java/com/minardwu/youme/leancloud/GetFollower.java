package com.minardwu.youme.leancloud;

import android.content.Intent;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.minardwu.youme.adapter.ForFollow;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetFollowerReceiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MinardWu on 2016/3/14.
 */
public class GetFollower {

    public static void get(final String userID){

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> query = new AVQuery<AVObject>("Follow");
                query.whereEqualTo("followee",AVObject.createWithoutData("_User",userID));
                query.orderByDescending("createdAt");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(final List<AVObject> list, AVException e) {
                        if(e==null){
                            final List<ForFollow> list_follower = new ArrayList<ForFollow>();
                            if(list.size()>0){
                                for (int i=0;i<list.size();i++){
                                    list.get(i).getAVObject("follower").fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                        @Override
                                        public void done(AVObject avObject, AVException e) {
                                            String userID = avObject.getObjectId();
                                            String username = avObject.getString("username");
                                            String portraitUrl=null;
                                            if(avObject.getAVFile("UserPortrait") == null){
                                                portraitUrl = "http://ac-b2cy6yl4.clouddn.com/fdb31720f58cd084.jpg";
                                            }else {
                                                portraitUrl = avObject.getAVFile("UserPortrait").getUrl();
                                            }
                                            ForFollow forFollow = new ForFollow(userID,username,portraitUrl);
                                            list_follower.add(forFollow);
                                            if (list_follower.size() == list.size()) {
                                                Intent intent = new Intent(GetFollowerReceiver.ACTION);
                                                intent.putExtra("GetFollowerString", "success");
                                                intent.putExtra("GetFollowerData", (Serializable) list_follower);
                                                App.getContext().sendBroadcast(intent);
                                            }
                                        }
                                    });
                                }

                            }else {
                                Intent intent = new Intent(GetFollowerReceiver.ACTION);
                                intent.putExtra("GetFollowerString", "noFollower");
                                App.getContext().sendBroadcast(intent);
                            }
                        }
                    }
                });
            }
        }).start();


    }

}
