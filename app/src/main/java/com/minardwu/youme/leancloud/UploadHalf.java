package com.minardwu.youme.leancloud;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.UploadHalfReceiver;
import com.minardwu.youme.br.UploadWholeReceiver;

import java.io.FileNotFoundException;

/**
 * Created by MinardWu on 2016/3/9.
 */
public class UploadHalf {
    public static void upload(final String path, final String position) {

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
                AVObject post = new AVObject("Half");
                post.put("author",AVUser.getCurrentUser());
                post.put("authorID", AVUser.getCurrentUser().getObjectId());
                post.put("position",position);
                post.put("photo", file);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.v("UploadHalf", "success");
                            UpdateNumber.up(AVUser.getCurrentUser().getObjectId(), "half", "up");
                            GetHalfInfo.get(4);
                            Intent intent = new Intent(UploadHalfReceiver.ACTION);
                            intent.putExtra("UploadHalfString", "success");
                            App.getContext().sendBroadcast(intent);
                        } else {
                            Log.v("UploadHalf", e.toString());
                            Intent intent = new Intent(UploadHalfReceiver.ACTION);
                            intent.putExtra("UploadHalfString", "fail");
                            App.getContext().sendBroadcast(intent);
                        }
                    }
                });

            }
        }).start();
    }
}
