package com.minardwu.youme.leancloud;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.minardwu.youme.adapter.Half;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetHalfBitmapReceiver;
import com.minardwu.youme.br.GetOneUserHalfBitmapReceiver;
import com.minardwu.youme.br.GetOneUserHalfReceiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MinardWu on 2016/3/13.
 */
public class GetOneUserHalf {

    private static GetOneUserHalfBitmapReceiver getOneUserHalfBitmapReceiver;
    private static String title;
    private static String photoID;
    private static String authorId;
    private static String authorName;
    private static String authorPortraitUrl;
    private static String authorinstallationID;
    private static String pohotUrl;
    private static String photoPosition;
    private static String time;
    private static int love;
    private static byte[] bytes;

    public static void get(final String userID) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetOneUserHalfBitmap.get(userID);
                getOneUserHalfBitmapReceiver = new GetOneUserHalfBitmapReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if(intent.getStringExtra("GetOneUserHalfBitmapString").equals("success")){
                            final List<byte[]> list_byte = (List<byte[]>) intent.getSerializableExtra("GetOneUserHalfBitmapData");
                            AVQuery<AVObject> query = new AVQuery<AVObject>("Half");
                            query.whereEqualTo("authorID", userID);
                            query.orderByDescending("createdAt");
                            query.findInBackground(new FindCallback<AVObject>() {
                                public void done(final List<AVObject> avObjects, AVException e) {
                                    final List<Half> list_halfinfo = new ArrayList<Half>();
                                    if (e == null) {
                                        if(avObjects.size()>0){
                                            for (int i = 0; i < avObjects.size(); i++) {
                                                final AVObject avObject = avObjects.get(i);
                                                final byte[] bytefromlist = list_byte.get(i);
                                                final int finalI = i;
                                                //加入无意义的数据占位，为了防止后面根据j下标设置数据时出现报错
                                                byte[] mybyte = new byte[0];
                                                list_halfinfo.add(new Half("","","","","","",0,"","",mybyte));
                                                avObjects.get(i).getAVObject("author")
                                                        .fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                                            public void done(AVObject object, AVException e) {
                                                                final int j = finalI;
                                                                love = avObject.getInt("love");
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
                                                                bytes = bytefromlist;
                                                                photoID = avObject.getObjectId();
                                                                pohotUrl = avObject.getAVFile("photo").getUrl();
                                                                photoPosition = avObject.getString("position");
                                                                authorId = object.getObjectId();
                                                                authorName = object.getString("username");
                                                                if(object.getAVFile("UserPortrait")==null){
                                                                    authorPortraitUrl="http://ac-b2cy6yl4.clouddn.com/fdb31720f58cd084.jpg";
                                                                }else {
                                                                    authorPortraitUrl = object.getAVFile("UserPortrait").getUrl();
                                                                }
                                                                authorinstallationID = object.getString("installationID");
                                                                Half half = new Half(photoID, pohotUrl, authorId, authorName, authorPortraitUrl, authorinstallationID,love, time, photoPosition, bytes);
                                                                list_halfinfo.set(j,half);
                                                                if (list_halfinfo.size() == avObjects.size()) {
                                                                    Log.d("GetHalfInfo", "查询到" + avObjects.size() + " 条符合条件的数据");
                                                                    Log.d("GetHalfInfo", "得到" + list_halfinfo.size() + " 条符合条件的数据");
                                                                    Intent intent = new Intent(GetOneUserHalfReceiver.ACTION);
                                                                    intent.putExtra("GetOneUserHalfString", "success");
                                                                    intent.putExtra("GetOneUserHalfData", (Serializable) list_halfinfo);
                                                                    App.getContext().sendBroadcast(intent);
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    } else {
                                        Intent intent = new Intent(GetOneUserHalfReceiver.ACTION);
                                        intent.putExtra("GetOneUserHalfBitmapString", "fail");
                                        App.getContext().sendBroadcast(intent);
                                    }
                                }
                            });
                        }else if(intent.getStringExtra("GetOneUserHalfBitmapString").equals("noHalf")){
                            Intent intent2 = new Intent(GetOneUserHalfReceiver.ACTION);
                            intent2.putExtra("GetOneUserHalfString", "noHalf");
                            App.getContext().sendBroadcast(intent2);
                        }

                    }
                };
                App.getContext().registerReceiver(getOneUserHalfBitmapReceiver, new IntentFilter(GetOneUserHalfBitmapReceiver.ACTION));

            }

        }).start();
    }
}
