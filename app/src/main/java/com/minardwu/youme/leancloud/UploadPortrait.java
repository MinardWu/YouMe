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
import com.avos.avoscloud.SaveCallback;
import com.minardwu.youme.br.GetUserNPReceiver;

import java.io.FileNotFoundException;

/**
 * Created by MinardWu on 2016/3/5.
 */
public class UploadPortrait {


    public static void upload(final String path, final Context context) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                AVFile file = null;
                try {
                    file = AVFile.withAbsoluteLocalPath(time + ".jpg", path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String tableName = "_User";
                AVObject post = new AVObject(tableName);
                AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
                try {
                    post = query.get(AVUser.getCurrentUser().getObjectId());
                } catch (AVException e) {
                    e.printStackTrace();
                }
                post.put("UserPortrait", file);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                            GetUserInfo.getUsernameAndPortraitUrl(AVUser.getCurrentUser().getObjectId());
                        } else {
                            Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }


}
