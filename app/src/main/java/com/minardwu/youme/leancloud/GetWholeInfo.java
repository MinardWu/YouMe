package com.minardwu.youme.leancloud;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.minardwu.youme.R;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetSkipWholeReceiver;
import com.minardwu.youme.br.GetWholeReceiver;
import com.minardwu.youme.adapter.Whole;
import com.minardwu.youme.widget.ChangeTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MinardWu on 2016/3/7.
 */
public class GetWholeInfo {


    public static void get(final int i) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> query = new AVQuery<AVObject>("Whole");
                query.orderByDescending("createdAt");
                query.setLimit(i);
                query.findInBackground(new FindCallback<AVObject>() {
                                           public void done(final List<AVObject> avObjects, AVException e) {
                                               final List<Whole> list_photoinfo = new ArrayList<Whole>(1000);
                                               if (e == null) {
                                                   for (int i = 0; i < avObjects.size(); i++) {
                                                       //立即获得四个whole对象，但是还是要从这四个对象发出请求获取authhor1/2的信息
                                                       //可看成这些请求是同时发出的，所去获取的数据并不一定按照时间排序，所以list_photoinfo不能直接用add方法
                                                       final AVObject avObject = avObjects.get(i);
                                                       final int finalI = i;
                                                       //加入无意义的数据占位，为了防止后面根据j下标设置数据时出现报错
                                                       list_photoinfo.add(new Whole("","","","","",0,"","","","","",""));
                                                       avObjects.get(i).getAVObject("author1")
                                                               .fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                                                   public void done(AVObject object, AVException e) {
                                                                       final int j = finalI;
                                                                       final int love = avObject.getInt("love");
                                                                       final String time = ChangeTime.change(avObject.getDate("createdAt"));
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
                                                                               if (list_photoinfo.size() == avObjects.size()) {
                                                                                   Intent intent = new Intent(GetWholeReceiver.ACTION);
                                                                                   intent.putExtra("GetWholeInfoString", "ok");
                                                                                   intent.putExtra("GetWholeInfo", (Serializable) list_photoinfo);
                                                                                   App.getContext().sendBroadcast(intent);
                                                                               }
                                                                           }
                                                                       });
                                                                   }
                                                               });
                                                   }

                                               } else {
                                                   Intent intent = new Intent(GetWholeReceiver.ACTION);
                                                   intent.putExtra("GetWholeInfoString", "fail");
                                                   App.getContext().sendBroadcast(intent);
                                                   Log.d("GetWholeInfo", "查询错误: " + e.getMessage());
                                               }
                                           }
                                       }

                );
            }
        }).start();

    }

    public static void skip(final int num){
        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> query = new AVQuery<AVObject>("Whole");
                query.orderByDescending("createdAt");
                query.setSkip(num);
                query.setLimit(4);
                query.findInBackground(new FindCallback<AVObject>() {
                                           public void done(final List<AVObject> avObjects, AVException e) {
                                               final List<Whole> list_photoinfo = new ArrayList<Whole>(1000);
                                               if (e == null) {
                                                   if(avObjects.size()==0){
                                                       Intent intent = new Intent(GetSkipWholeReceiver.ACTION);
                                                       intent.putExtra("GetSkipWholeInfoString", "nomore");
                                                       App.getContext().sendBroadcast(intent);
                                                   }
                                                   for (int i = 0; i < avObjects.size(); i++) {
                                                       //立即获得多个whole对象，但是还是要从这多个对象发出请求获取authhor1/2的信息
                                                       //可看成这些请求是同时发出的，所去获取authhor1/2的数据时间并不一定相同，所以加入list并不一定按照时间排序，所以list_photoinfo不能直接用add方法
                                                       final AVObject avObject = avObjects.get(i);
                                                       final int finalI = i;
                                                       //加入无意义的数据占位，为了防止后面根据j下标设置数据时出现报错
                                                       list_photoinfo.add(new Whole("","","","","",0,"","","","","",""));
                                                       avObjects.get(i).getAVObject("author1")
                                                               .fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                                                   public void done(AVObject object, AVException e) {
                                                                       final int j = finalI;
                                                                       final int love = avObject.getInt("love");
                                                                       final String time = ChangeTime.change(avObject.getDate("createdAt"));
                                                                       final String photoID = avObject.getObjectId();
                                                                       final String photoUrl = avObject.getAVFile("photo").getUrl();
                                                                       final String author1Id = object.getObjectId();
                                                                       final String author1Name = object.getString("username");
                                                                       final String author1PortraitUrl = object.getAVFile("UserPortrait").getUrl();
                                                                       final String author1installationID = object.getString("installationID");
                                                                       final String finalTime = time;
                                                                       avObject.getAVObject("author2").fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                                                           @Override
                                                                           public void done(AVObject avObject, AVException e) {
                                                                               final String author2Id = avObject.getObjectId();
                                                                               final String author2Name = avObject.getString("username");
                                                                               final String author2PortraitUrl = avObject.getAVFile("UserPortrait").getUrl();
                                                                               final String author2installationID = avObject.getString("installationID");
                                                                               Whole whole = new Whole(photoID, author1Id, author2Id,author1installationID,author2installationID, love, finalTime, author1Name, author2Name, photoUrl, author1PortraitUrl, author2PortraitUrl);
                                                                               list_photoinfo.set(j, whole);
                                                                               if (j == avObjects.size()-1) {
                                                                                   Intent intent = new Intent(GetSkipWholeReceiver.ACTION);
                                                                                   intent.putExtra("GetSkipWholeInfoString", "success");
                                                                                   intent.putExtra("GetSkipWholeInfo", (Serializable) list_photoinfo);
                                                                                   App.getContext().sendBroadcast(intent);
                                                                                   Log.v("asdfghjfghd", "send");
                                                                               }
                                                                           }
                                                                       });
                                                                   }
                                                               });
                                                   }

                                               } else {
                                                   Intent intent = new Intent(GetSkipWholeReceiver.ACTION);
                                                   intent.putExtra("GetSkipWholeInfoString", "fail");
                                                   App.getContext().sendBroadcast(intent);
                                                   Log.d("GetSkipWholeInfo", "查询错误: " + e.getMessage());
                                               }
                                           }
                                       }

                );
            }
        }).start();
    }
}
