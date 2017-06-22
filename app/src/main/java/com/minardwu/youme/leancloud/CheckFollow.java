package com.minardwu.youme.leancloud;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by MinardWu on 2016/3/14.
 */
public class CheckFollow {

    public static void check(final String followeeID, final Button btn_follow, final Activity activity) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("Follow");
        query.whereEqualTo("followee", AVObject.createWithoutData("_User", followeeID));
        query.whereEqualTo("follower", AVUser.getCurrentUser());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        btn_follow.setTag(0);
                        btn_follow.setText("＋ 关注");
                        btn_follow.setSelected(true);
                        btn_follow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ((int) btn_follow.getTag() == 0) {
                                    Follow.follow(followeeID);
                                    btn_follow.setTag(1);
                                    btn_follow.setText("√ 关注");
                                    btn_follow.setSelected(false);
                                } else if ((int) btn_follow.getTag() == 1) {
                                    final MaterialDialog mMaterialDialog = new MaterialDialog(activity);
                                    mMaterialDialog.setTitle("操作")
                                            .setMessage("取消关注？")
                                            .setPositiveButton("确定", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Unfollow.unfollow(followeeID);
                                                    mMaterialDialog.dismiss();
                                                    btn_follow.setTag(0);
                                                    btn_follow.setText("＋ 关注");
                                                    btn_follow.setSelected(true);
                                                }
                                            }).setNegativeButton("取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mMaterialDialog.dismiss();
                                        }
                                    });
                                    mMaterialDialog.show();
                                }
                            }
                        });
                    } else {
                        btn_follow.setTag(1);
                        btn_follow.setText("√ 关注");
                        btn_follow.setSelected(false);
                        btn_follow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ((int) btn_follow.getTag() == 0) {
                                    Follow.follow(followeeID);
                                    btn_follow.setTag(1);
                                    btn_follow.setText("√ 关注");
                                    btn_follow.setSelected(false);
                                } else if ((int) btn_follow.getTag() == 1) {
                                    final MaterialDialog mMaterialDialog = new MaterialDialog(activity);
                                    mMaterialDialog.setTitle("操作")
                                            .setMessage("取消关注？")
                                            .setPositiveButton("确定", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Unfollow.unfollow(followeeID);
                                                    mMaterialDialog.dismiss();
                                                    btn_follow.setTag(0);
                                                    btn_follow.setText("＋ 关注");
                                                    btn_follow.setSelected(true);
                                                }
                                            }).setNegativeButton("取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mMaterialDialog.dismiss();
                                        }
                                    });
                                    mMaterialDialog.show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(App.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
