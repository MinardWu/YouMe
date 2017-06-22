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
 * Created by MinardWu on 2016/3/13.
 */
public class InsertLove {

    public static void insert(String photoID,String photoUrl,String author1ID,String author2ID) {
        AVObject love = new AVObject("Love");
        if (author2ID!=null){
            love.put("author2", AVObject.createWithoutData("_User", author2ID));
        }
        love.put("photoID",photoID);
        love.put("lover", AVUser.getCurrentUser());
        love.put("author1", AVObject.createWithoutData("_User", author1ID));
        love.put("photoUrl", photoUrl);
        love.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    Toast.makeText(App.getContext(), "点赞失败", Toast.LENGTH_SHORT).show();
                    Log.v("sadfesda", e.toString());
                }
            }
        });

    }
}
