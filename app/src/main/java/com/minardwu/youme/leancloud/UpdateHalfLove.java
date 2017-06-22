package com.minardwu.youme.leancloud;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.List;

/**
 * Created by MinardWu on 2016/3/10.
 */
public class UpdateHalfLove {

    public static void update(final String photoID, final String authorID, final String type) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVObject half = new AVObject("Half");
                AVQuery<AVObject> query = new AVQuery<AVObject>("Half");
                try {
                    half = query.get(photoID);
                    if (type.equals("up")) {
                        half.increment("love");
                    } else {
                        half.increment("love",-1);
                    }
                    half.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UpdateNumber.up(authorID, "love", type);
                                    }
                                }).start();

                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
