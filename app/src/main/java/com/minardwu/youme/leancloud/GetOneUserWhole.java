package com.minardwu.youme.leancloud;

import android.content.Intent;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.minardwu.youme.adapter.Whole;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetOneUserWholeReceiver;
import com.minardwu.youme.br.GetWholeReceiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MinardWu on 2016/3/11.
 */
public class GetOneUserWhole {

    private static List<Whole> list_photoinfo = new ArrayList<Whole>();

    public static void get(final String userid){

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> one = AVQuery.getQuery("Whole");
                one.whereEqualTo("author1ID", userid);
                AVQuery<AVObject> two = AVQuery.getQuery("Whole");
                two.whereEqualTo("author2ID", userid);
                List<AVQuery<AVObject>> queries = new ArrayList<AVQuery<AVObject>>();
                queries.add(one);
                queries.add(two);
                AVQuery<AVObject> mainQuery = AVQuery.or(queries);
                mainQuery.orderByDescending("createdAt");
                mainQuery.findInBackground(new FindCallback<AVObject>() {
                    public void done(final List<AVObject> results, AVException e) {
                        if (e==null){
                            if(results.size()>0){
                                list_photoinfo.clear();
                                for(int i=0;i<results.size();i++){
                                    final AVObject avObject = results.get(i);
                                    final int finalI = i;
                                    //加入无意义的数据占位，为了防止后面根据j下标设置数据时出现报错
                                    list_photoinfo.add(new Whole("","","","","",0,"","","","","",""));
                                    results.get(i).getAVObject("author1")
                                            .fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                                public void done(AVObject object, AVException e) {
                                                    final int j = finalI;
                                                    final int love = avObject.getInt("love");
                                                    String time = null;
                                                    long ONE_MINUTE = 60000L;
                                                    long ONE_HOUR = 3600000L;
                                                    long ONE_DAY = 86400000L;
                                                    long ONE_WEEK = 604800000L;
                                                    Date date = avObject.getDate("createdAt");
                                                    long delta = new Date().getTime() - date.getTime();
                                                    if (delta < 1L * ONE_MINUTE) {
                                                        long seconds = delta / 1000;
                                                        time = (seconds <= 0 ? 1 : seconds) + "秒前";
                                                    } else if (delta < 45L * ONE_MINUTE) {
                                                        long minutes = delta / 60000;
                                                        time = (minutes <= 0 ? 1 : minutes) + "分钟前";
                                                    } else if (delta < 24L * ONE_HOUR) {
                                                        long hours = delta / 3600000;
                                                        time = (hours <= 0 ? 1 : hours) + "小时前";
                                                    } else if (delta < 48L * ONE_HOUR) {
                                                        time = "昨天";
                                                    } else {
                                                        time = (date.getMonth() + 1) + "-" + date.getDay() + " " + date.getHours() + ":" + date.getMinutes();
                                                    }
                                                    final String photoID = avObject.getObjectId();
                                                    final String photoUrl = avObject.getAVFile("photo").getUrl();
                                                    final String author1Id = object.getObjectId();
                                                    final String author1Name = object.getString("username");
                                                    String author1PortraitUrl = null;
                                                    if(object.getAVFile("UserPortrait")==null){
                                                        author1PortraitUrl = "http://ac-b2cy6yl4.clouddn.com/fdb31720f58cd084.jpg";
                                                    }else {
                                                        author1PortraitUrl = object.getAVFile("UserPortrait").getUrl();
                                                    }
                                                    final String finalAuthor1PortraitUrl = author1PortraitUrl;
                                                    final String author1installationID = object.getString("installationID");
                                                    final String finalTime = time;
                                                    avObject.getAVObject("author2").fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                                        @Override
                                                        public void done(AVObject avObject, AVException e) {
                                                            final String author2Id = avObject.getObjectId();
                                                            final String author2Name = avObject.getString("username");
                                                            String author2PortraitUrl = null;
                                                            if(avObject.getAVFile("UserPortrait")==null){
                                                                author2PortraitUrl = "http://ac-b2cy6yl4.clouddn.com/fdb31720f58cd084.jpg";
                                                            }else {
                                                                author2PortraitUrl = avObject.getAVFile("UserPortrait").getUrl();
                                                            }
                                                            final String author2installationID = avObject.getString("installationID");
                                                            Whole whole = new Whole(photoID, author1Id, author2Id,author1installationID,author2installationID, love, finalTime, author1Name, author2Name, photoUrl, finalAuthor1PortraitUrl, author2PortraitUrl);
                                                            list_photoinfo.set(j, whole);
                                                            if (list_photoinfo.size() == results.size()) {
                                                                Intent intent = new Intent(GetOneUserWholeReceiver.ACTION);
                                                                intent.putExtra("GetOneUserWholeString","success");
                                                                intent.putExtra("GetOneUserWholeSuccess",(Serializable) list_photoinfo);
                                                                App.getContext().sendBroadcast(intent);
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                }
                            }else {
                                Intent intent = new Intent(GetOneUserWholeReceiver.ACTION);
                                intent.putExtra("GetOneUserWholeString","noWhole");
                                App.getContext().sendBroadcast(intent);
                            }
                        }else {
                            Intent intent = new Intent(GetOneUserWholeReceiver.ACTION);
                            intent.putExtra("GetOneUserWholeString","fail");
                            App.getContext().sendBroadcast(intent);
                        }

                    }
                });
            }
        }).start();

    }

}
