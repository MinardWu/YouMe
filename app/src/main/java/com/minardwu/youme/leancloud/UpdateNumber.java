package com.minardwu.youme.leancloud;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.List;

/**
 * Created by MinardWu on 2016/3/12.
 */
public class UpdateNumber {

    public static void up(final String UserID, final String type, final String direction) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //通过作者的objectID获得Love表中的对象，提取对象的objectId，获得提取对象的objectId才能更新数据
                AVQuery<AVObject> query1 = new AVQuery<AVObject>("Number");
                query1.whereEqualTo("userID", UserID);
                query1.findInBackground(new FindCallback<AVObject>() {
                    public void done(List<AVObject> avObjects, AVException e) {
                        if (e == null) {
                            final String objectid = avObjects.get(0).getObjectId();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    AVObject number = new AVObject("Number");
                                    AVQuery<AVObject> query2 = new AVQuery<AVObject>("Number");
                                    try {
                                        number = query2.get(objectid);
                                        if(type.equals("love")){
                                            if(direction.equals("up")){
                                                number.increment("love");
                                            }else {
                                                number.increment("love",-1);
                                            }
                                        }else if(type.equals("friend")){
                                            if(direction.equals("up")){
                                                number.increment("friend");
                                            }else {
                                                number.increment("friend",-1);
                                            }
                                        }else if(type.equals("half")){
                                            if(direction.equals("up")){
                                                number.increment("half");
                                                number.increment("photo");
                                            }else {
                                                number.increment("half",-1);
                                                number.increment("photo",-1);
                                            }
                                        }else if(type.equals("whole")){
                                            if(direction.equals("up")){
                                                number.increment("whole");
                                                number.increment("photo");
                                            }else {
                                                number.increment("whole",-1);
                                                number.increment("photo",-1);
                                            }
                                        }else if(type.equals("follower")){
                                            if(direction.equals("up")){
                                                number.increment("follower");
                                            }else {
                                                number.increment("follower",-1);
                                            }
                                        }else if(type.equals("followee")){
                                            if(direction.equals("up")){
                                                number.increment("followee");
                                            }else {
                                                number.increment("followee",-1);
                                            }
                                        }
                                        number.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                            }
                                        });
                                    } catch (AVException e1) {
                                        Log.v("updatefail",e1.toString());
                                    }
                                }
                            }).start();

                        }
                    }
                });
            }
        }).start();
    }

}
