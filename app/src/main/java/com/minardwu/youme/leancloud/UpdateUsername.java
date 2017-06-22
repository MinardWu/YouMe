package com.minardwu.youme.leancloud;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.youme.base.User;
import com.minardwu.youme.br.GetUserNPReceiver;

/**
 * Created by MinardWu on 2016/3/5.
 */
public class UpdateUsername {

    public static void update( final Context context,final String newUsername) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String tableName = "_User";
                AVObject post = new AVObject(tableName);
                AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
                try {
                    post = query.get(AVUser.getCurrentUser().getObjectId());
                } catch (AVException e) {
                    e.printStackTrace();
                }
                post.put("username",newUsername);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                            GetUserInfo.getUsernameAndPortraitUrl(AVUser.getCurrentUser().getObjectId());
                        } else {
                            if(e.toString().equals("com.avos.avoscloud.AVException: {\"code\":202,\"error\":\"Username has already been taken\"}")){
                                Toast.makeText(context, "该用户名已经被使用", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }).start();

    }

}
