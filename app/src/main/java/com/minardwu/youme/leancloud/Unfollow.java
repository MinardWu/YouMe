package com.minardwu.youme.leancloud;

import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.minardwu.youme.base.App;

import java.util.List;

/**
 * Created by MinardWu on 2016/3/14.
 */
public class Unfollow {

    public static void unfollow(final String followeeID) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("Follow");
        query.whereEqualTo("followee", AVObject.createWithoutData("_User",followeeID));
        query.whereEqualTo("follower", AVUser.getCurrentUser());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {
                if (e == null) {
                    String objectID = list.get(0).getObjectId();
                    AVQuery.doCloudQueryInBackground("delete from Follow where objectId = '" + objectID + "'", new CloudQueryCallback<AVCloudQueryResult>() {
                        @Override
                        public void done(AVCloudQueryResult result, AVException cqlException) {
                            if (cqlException == null) {
                                //关注人的粉丝数减1
                                UpdateNumber.up(followeeID,"follower","down");
                                //用户的关注人减1
                                UpdateNumber.up(AVUser.getCurrentUser().getObjectId(), "followee", "down");
                            }
                        }
                    });

                }

            }
        });

    }
}
