package com.minardwu.youme.leancloud;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetHalfBitmapReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by MinardWu on 2016/3/9.
 */
public class GetHalfBitmap {

    public static void get(final int i) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> query = new AVQuery<AVObject>("Half");
//                query.setLimit(i);
                query.orderByDescending("createdAt");
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
                                                        Log.v("dksjfkjashdflsuh1",list_byte.size()+"");
                                                        Intent intent = new Intent(GetHalfBitmapReceiver.ACTION);
                                                        intent.putExtra("GetHalfBitmapString", "success");
                                                        intent.putExtra("GetHalfBitmapData", (Serializable) list_byte);
                                                        App.getContext().sendBroadcast(intent);
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            }else {
                                Intent intent = new Intent(GetHalfBitmapReceiver.ACTION);
                                intent.putExtra("GetHalfBitmapString", "noHalf");
                                App.getContext().sendBroadcast(intent);
                            }
                        } else {
                            Intent intent = new Intent(GetHalfBitmapReceiver.ACTION);
                            intent.putExtra("GetHalfBitmapString", "fail");
                            App.getContext().sendBroadcast(intent);
                        }
                    }
                });
            }
        }).start();

    }


}
