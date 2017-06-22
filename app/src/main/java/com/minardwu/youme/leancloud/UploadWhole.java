package com.minardwu.youme.leancloud;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.UploadWholeReceiver;

import java.io.FileNotFoundException;

/**
 * Created by MinardWu on 2016/3/7.
 */
public class UploadWhole {

    public static void upload(final String path, final String author1ID) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                AVFile file = null;
                try {
                    file = AVFile.withAbsoluteLocalPath(time + ".jpg", path);
                } catch (FileNotFoundException e) {
                    Log.v("UploadWhole",e.toString());
                    e.printStackTrace();
                }
                AVObject post = new AVObject("Whole");
                String tableName = "_User";
                AVObject user1 = new AVObject(tableName);
                AVObject user2 = new AVObject(tableName);
                AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
                try {
                    user1 = query.get(author1ID);
                    post.put("author1", user1);
                    post.put("author2", AVUser.getCurrentUser());
                    post.put("author1ID", author1ID);
                    post.put("author2ID",AVUser.getCurrentUser().getObjectId());
                    post.put("photo", file);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                UpdateNumber.up(AVUser.getCurrentUser().getObjectId(), "whole", "up");
                                GetWholeInfo.get(4);
                                Intent intent = new Intent(UploadWholeReceiver.ACTION);
                                intent.putExtra("UploadWholeString", "success");
                                App.getContext().sendBroadcast(intent);
                            } else {
                                Intent intent = new Intent(UploadWholeReceiver.ACTION);
                                intent.putExtra("UploadWholeString", "fail");
                                App.getContext().sendBroadcast(intent);
                            }
                        }
                    });

                } catch (AVException e) {
                    Log.v("UploadWhole",e.toString());
                    e.printStackTrace();
                }

            }
        }).start();
    }


}
