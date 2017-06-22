package com.minardwu.youme.leancloud;

import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.minardwu.youme.base.App;

/**
 * Created by MinardWu on 2016/3/15.
 */
public class DeleteHalf {

    public static void delete(String photoID) {

        AVQuery.doCloudQueryInBackground("delete from Half where objectId = '" + photoID + "'", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult result, AVException cqlException) {
                if (cqlException == null) {
                    UpdateNumber.up(AVUser.getCurrentUser().getObjectId(),"half","down");
                    Toast.makeText(App.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(App.getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
