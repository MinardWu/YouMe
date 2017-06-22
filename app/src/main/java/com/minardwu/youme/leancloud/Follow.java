package com.minardwu.youme.leancloud;

import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.minardwu.youme.base.App;
import com.minardwu.youme.base.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinardWu on 2016/3/14.
 */
public class Follow {

    public static void follow(final String followeeID){

        final AVObject love = new AVObject("Follow");
        love.put("followee", AVObject.createWithoutData("_User",followeeID));
        love.put("follower", AVUser.getCurrentUser());
        love.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    //关注人的粉丝数加1
                    UpdateNumber.up(followeeID, "follower", "up");
                    //用户的关注人加1
                    UpdateNumber.up(AVUser.getCurrentUser().getObjectId(), "followee", "up");
                    //推送
                    //获取关注的人的installationId
                    AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
                    query.whereEqualTo("objectId",followeeID);
                    query.findInBackground(new FindCallback<AVObject>() {
                        public void done(List<AVObject> avObjects, AVException e) {
                            if (e == null) {
                                //发送
                                final String followeeInstallationID = avObjects.get(0).getString("installationID");
                                AVQuery pushQuery = AVInstallation.getQuery();
                                pushQuery.whereEqualTo("installationId", followeeInstallationID);
                                AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " 关注了你", pushQuery, new SendCallback() {
                                    @Override
                                    public void done(AVException e) {

                                    }
                                });
                            }
                        }
                    });

                }else{
                    Toast.makeText(App.getContext(), "关注失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
