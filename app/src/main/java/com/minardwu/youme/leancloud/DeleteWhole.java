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
 * Created by MinardWu on 2016/3/15.
 */
public class DeleteWhole {

    public static void delete(String photoID) {

        AVQuery.doCloudQueryInBackground("delete from Whole where objectId = '" + photoID + "'", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult result, AVException cqlException) {
                if (cqlException == null) {
                    UpdateNumber.up(AVUser.getCurrentUser().getObjectId(), "whole", "down");
                    Toast.makeText(App.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(App.getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
