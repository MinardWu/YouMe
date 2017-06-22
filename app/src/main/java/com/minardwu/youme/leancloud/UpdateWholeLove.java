package com.minardwu.youme.leancloud;

import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.youme.base.App;
import com.minardwu.youme.base.User;

import java.util.List;

/**
 * Created by MinardWu on 2016/3/7.
 */
public class UpdateWholeLove {

    public static void update(final String photoID,final String author1ID,final String author2ID, final String direction){

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVObject whole = new AVObject("Whole");
                AVQuery<AVObject> query = new AVQuery<AVObject>("Whole");
                try {
                    whole = query.get(photoID);
                    if (direction.equals("up")){
                        whole.increment("love");
                    }else {
                        whole.increment("love",-1);
                    }
                    whole.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (author1ID.equals(author2ID)){
                                            UpdateNumber.up(author1ID,"love",direction);
                                        }else {
                                            UpdateNumber.up(author1ID,"love",direction);
                                            UpdateNumber.up(author2ID,"love",direction);
                                        }
                                    }
                                }).start();

                            }
                        }
                    });
                } catch (AVException e) {

                }
            }
        }).start();
    }

}
