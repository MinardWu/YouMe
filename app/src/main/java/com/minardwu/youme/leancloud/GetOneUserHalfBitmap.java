package com.minardwu.youme.leancloud;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetHalfBitmapReceiver;
import com.minardwu.youme.br.GetOneUserHalfBitmapReceiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MinardWu on 2016/3/15.
 */
public class GetOneUserHalfBitmap {

    public static void get(final String userID) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> query = new AVQuery<AVObject>("Half");
                query.orderByDescending("createdAt");
                query.whereEqualTo("authorID", userID);
                query.findInBackground(new FindCallback<AVObject>() {
                    public void done(final List<AVObject> avObjects, AVException e) {
                        final List<Bitmap> list_bitmap = new ArrayList<Bitmap>();
                        final List<byte[]> list_byte = new ArrayList<byte[]>();
                        if (e == null) {
                            if(avObjects.size()>0){
                                for (int i = 0; i < avObjects.size(); i++) {
                                    AVFile avFile = avObjects.get(i).getAVFile("photo");
                                    avFile.getDataInBackground(new GetDataCallback() {
                                        public void done(final byte[] data, AVException e) {
                                            if (e == null) {
                                                if (data != null) {
                                                    list_byte.add(data);
                                                    if (list_byte.size() == avObjects.size()) {
                                                        Intent intent = new Intent(GetOneUserHalfBitmapReceiver.ACTION);
                                                        intent.putExtra("GetOneUserHalfBitmapString", "success");
                                                        intent.putExtra("GetOneUserHalfBitmapData", (Serializable) list_byte);
                                                        App.getContext().sendBroadcast(intent);
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            }else {
                                Intent intent = new Intent(GetOneUserHalfBitmapReceiver.ACTION);
                                intent.putExtra("GetOneUserHalfBitmapString", "noHalf");
                                App.getContext().sendBroadcast(intent);
                            }
                        } else {
                            Intent intent = new Intent(GetOneUserHalfBitmapReceiver.ACTION);
                            intent.putExtra("GetOneUserHalfBitmapString", "fail");
                            App.getContext().sendBroadcast(intent);
                        }
                    }
                });
            }
        }).start();

    }


}
