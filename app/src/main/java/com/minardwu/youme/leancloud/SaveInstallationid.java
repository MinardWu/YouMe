package com.minardwu.youme.leancloud;

import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.youme.base.App;

/**
 * Created by MinardWu on 2016/3/16.
 */
public class SaveInstallationid {

    public static void save(){
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    final String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
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
                            post.put("installationID",installationId);
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

}
