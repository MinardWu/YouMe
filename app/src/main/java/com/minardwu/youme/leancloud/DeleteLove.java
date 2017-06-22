package com.minardwu.youme.leancloud;

import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.minardwu.youme.base.App;

import java.util.List;

/**
 * Created by MinardWu on 2016/3/13.
 */
public class DeleteLove {

    public static void delete(String photoID){
        AVQuery<AVObject> query = new AVQuery<AVObject>("Love");
        query.whereEqualTo("photoID", photoID);
        query.whereEqualTo("lover", AVUser.getCurrentUser());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {
                if (e==null){
                    if(list!=null){
                        String objectID = list.get(0).getObjectId();
                        AVQuery.doCloudQueryInBackground("delete from Love where objectId = '"+objectID+"'",new CloudQueryCallback<AVCloudQueryResult>() {
                            @Override
                            public void done(AVCloudQueryResult result, AVException cqlException) {
                                if (cqlException == null) {
                                    Log.v("DeleteLove", "ok");
                                }else {
                                    Log.v("DeleteLove",cqlException.toString());
                                }
                            }
                        });
                    }
                }
            }
        });

    }
}
