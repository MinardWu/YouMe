package com.minardwu.youme.leancloud;

import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.minardwu.youme.R;
import com.minardwu.youme.base.App;

import java.util.List;

/**
 * Created by MinardWu on 2016/3/13.
 */
public class CheckLove {

    public static void check(String photoID, final ImageView iv_listview_love) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("Love");
        query.whereEqualTo("photoID", photoID);
        query.whereEqualTo("lover", AVUser.getCurrentUser());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list!=null){
                    if(list.size()==0){
                        iv_listview_love.setTag(1);
                        iv_listview_love.setBackgroundResource(R.drawable.icon_nolove);
                    }else {
                        iv_listview_love.setTag(2);
                        iv_listview_love.setBackgroundResource(R.drawable.icon_love);
                    }
                }
            }
        });
    }

}
