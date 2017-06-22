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
import com.minardwu.youme.adapter.Whole;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetHalfBitmapReceiver;
import com.minardwu.youme.br.GetHalfReceiver;
import com.minardwu.youme.br.GetSkipHalfReceiver;
import com.minardwu.youme.widget.ChangeTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MinardWu on 2016/3/9.
 */
public class GetHalfInfo {

    private static GetHalfBitmapReceiver getHalfBitmapReceiver;
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

    public static void get(final int i) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetHalfBitmap.get(i);
                getHalfBitmapReceiver = new GetHalfBitmapReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if(intent.getStringExtra("GetHalfBitmapString").equals("success")){
                            final List<byte[]> list_byte = (List<byte[]>) intent.getSerializableExtra("GetHalfBitmapData");
                            AVQuery<AVObject> query = new AVQuery<AVObject>("Half");
                            query.orderByDescending("createdAt");
//                            query.setLimit(i);
                            query.findInBackground(new FindCallback<AVObject>() {
                                public void done(final List<AVObject> avObjects, AVException e) {
                                    final List<Half> list_halfinfo = new ArrayList<Half>();
                                    if (e == null) {
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
                                                            time = ChangeTime.change(avObject.getDate("createdAt"));
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
                                                            Half half = new Half(photoID, pohotUrl, authorId, authorName, authorPortraitUrl, authorinstallationID, love, time, photoPosition, bytes);
                                                            list_halfinfo.set(j,half);
                                                            if (list_halfinfo.size() == avObjects.size()) {
                                                                Intent intent = new Intent(GetHalfReceiver.ACTION);
                                                                intent.putExtra("GetHalfInfoString", "success");
                                                                intent.putExtra("GetHalfInfoList", (Serializable) list_halfinfo);
                                                                App.getContext().sendBroadcast(intent);
                                                            }
                                                        }
                                                    });
                                        }

                                    } else {
                                        Intent intent = new Intent(GetHalfReceiver.ACTION);
                                        intent.putExtra("GetHalfInfoString", "fail");
                                        App.getContext().sendBroadcast(intent);
                                    }
                                }
                            });
                        }else {
                            Intent intent2 = new Intent(GetHalfReceiver.ACTION);
                            intent.putExtra("GetHalfInfoString", "fail");
                            App.getContext().sendBroadcast(intent2);
                        }
                    }
                };
                App.getContext().registerReceiver(getHalfBitmapReceiver, new IntentFilter(GetHalfBitmapReceiver.ACTION));

            }

        }).start();
    }

    public static void skip(final int num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetHalfBitmap.get(num);
                getHalfBitmapReceiver = new GetHalfBitmapReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if(intent.getStringExtra("GetHalfBitmapString").equals("success")){
                            final List<byte[]> list_byte = (List<byte[]>) intent.getSerializableExtra("GetHalfBitmapData");
                            AVQuery<AVObject> query = new AVQuery<AVObject>("Half");
                            query.orderByDescending("createdAt");
                            query.setSkip(num);
                            query.setLimit(4);
                            query.findInBackground(new FindCallback<AVObject>() {
                                public void done(final List<AVObject> avObjects, AVException e) {
                                    final List<Half> list_halfinfo = new ArrayList<Half>();
                                    if (e == null) {
                                        if(avObjects.size()==0){
                                            Intent intent = new Intent(GetSkipHalfReceiver.ACTION);
                                            intent.putExtra("GetSkipHalfInfoString", "nomore");
                                            App.getContext().sendBroadcast(intent);
                                        }
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
                                                            time = ChangeTime.change(avObject.getDate("createdAt"));
                                                            bytes = bytefromlist;
                                                            photoID = avObject.getObjectId();
                                                            pohotUrl = avObject.getAVFile("photo").getUrl();
                                                            photoPosition = avObject.getString("position");
                                                            authorId = object.getObjectId();
                                                            authorName = object.getString("username");
                                                            authorPortraitUrl = object.getAVFile("UserPortrait").getUrl();
                                                            authorinstallationID = object.getString("installationID");
                                                            Half half = new Half(photoID, pohotUrl, authorId, authorName, authorPortraitUrl, authorinstallationID, love, time, photoPosition, bytes);
                                                            list_halfinfo.set(j,half);
                                                            if (list_halfinfo.size() == avObjects.size()) {
                                                                Intent intent = new Intent(GetSkipHalfReceiver.ACTION);
                                                                intent.putExtra("GetSkipHalfInfoString", "success");
                                                                intent.putExtra("GetSkipHalfInfoList", (Serializable) list_halfinfo);
                                                                App.getContext().sendBroadcast(intent);
                                                            }
                                                        }
                                                    });
                                        }

                                    } else {
                                        Intent intent = new Intent(GetSkipHalfReceiver.ACTION);
                                        intent.putExtra("GetSkipHalfInfoString", "fail");
                                        App.getContext().sendBroadcast(intent);
                                    }
                                }
                            });
                        }else {
                            Intent intent2 = new Intent(GetSkipHalfReceiver.ACTION);
                            intent.putExtra("GetSkipHalfInfoString", "fail");
                            App.getContext().sendBroadcast(intent2);
                        }
                    }
                };
                App.getContext().registerReceiver(getHalfBitmapReceiver, new IntentFilter(GetHalfBitmapReceiver.ACTION));

            }

        }).start();
    }
}
