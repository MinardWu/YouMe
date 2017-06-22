package com.minardwu.youme.aty;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.youme.R;
import com.minardwu.youme.adapter.Whole;
import com.minardwu.youme.base.App;
import com.minardwu.youme.leancloud.CheckLove;
import com.minardwu.youme.leancloud.DeleteLove;
import com.minardwu.youme.leancloud.DeleteWhole;
import com.minardwu.youme.leancloud.InsertLove;
import com.minardwu.youme.leancloud.Unfollow;
import com.minardwu.youme.leancloud.UpdateWholeLove;

import me.drakeet.materialdialog.MaterialDialog;

public class ShowOneWholeActivity extends AppCompatActivity {

    private int resourceID;
    private long firstClick;
    private String userID;
    private String photoID;
    private long lastClick;
    private int count;
    private int lovenumber;
    private static int forcount = 0;
    private Whole whole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_one_whole);

        whole = (Whole) getIntent().getSerializableExtra("whole");
        userID = (String) getIntent().getSerializableExtra("userID");
        ImageView iv_toolabr_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        ImageView iv_toolbar_delete = (ImageView) findViewById(R.id.iv_toolbar_delete);

        if(!userID.equals(AVUser.getCurrentUser().getObjectId())){
            iv_toolbar_delete.setVisibility(View.GONE);
        }

        iv_toolabr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_toolbar_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog mMaterialDialog = new MaterialDialog(ShowOneWholeActivity.this);
                mMaterialDialog.setTitle("操作")
                        .setMessage("删除该照片？")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DeleteWhole.delete(whole.getPhotoID());
                                mMaterialDialog.dismiss();
                                finish();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
                mMaterialDialog.show();
            }
        });

        //activity_show_one_whole的内容即为listview_whole,以下均是wholeadapter中的内容
        TextView tv_username1 = (TextView) findViewById(R.id.tv_listview_username1);
        TextView tv_username2 = (TextView) findViewById(R.id.tv_listview_username2);
        SimpleDraweeView iv_user1Portrait = (SimpleDraweeView) findViewById(R.id.iv_listview_user1);
        SimpleDraweeView iv_user2Portrait = (SimpleDraweeView) findViewById(R.id.iv_listview_user2);
        SimpleDraweeView iv_whole = (SimpleDraweeView) findViewById(R.id.iv_listview_photo);
        TextView tv_time = (TextView) findViewById(R.id.tv_listview_time);
        final ImageView iv_love = (ImageView) findViewById(R.id.iv_listview_love);
        final TextView tv_love = (TextView) findViewById(R.id.tv_listview_love);
        //设置控件数据
        tv_username1.setText(whole.getAuthor1());
        tv_username2.setText(whole.getAuthor2());
        iv_user1Portrait.setImageURI(Uri.parse(whole.getAuthor1PortraitUrl()));
        iv_user2Portrait.setImageURI(Uri.parse(whole.getAuthor2PortraitUrl()));
        tv_time.setText(whole.getTime());
        tv_love.setText(whole.getLove() + "");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_whole.getLayoutParams();
        layoutParams.height = App.ScreenWidth;
        layoutParams.width = App.ScreenWidth;
        iv_whole.setLayoutParams(layoutParams);
        iv_whole.setImageURI(Uri.parse(whole.getPohotUrl()));
        //每次点击爱心getview函数都会再次执行，若不加条件lovenumber一直等于half.getLove()，所以应该价格条件让lovenumber初始化一次
        if (forcount == 0) {
            lovenumber = whole.getLove();
        }
        tv_love.setText(lovenumber + "");
        //设置头像点击事件
        iv_user1Portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.activityList.get(0), UserInfoActivity.class);
                intent.putExtra("userID", whole.getAuthor1ID());
                intent.putExtra("userName", whole.getAuthor1());
                intent.putExtra("userPortraitUrl", whole.getAuthor1PortraitUrl());
                App.activityList.get(0).startActivity(intent);
            }
        });
        iv_user2Portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.activityList.get(0), UserInfoActivity.class);
                intent.putExtra("userID", whole.getAuthor2ID());
                intent.putExtra("userName", whole.getAuthor2());
                intent.putExtra("userPortraitUrl", whole.getAuthor2PortraitUrl());
                App.activityList.get(0).startActivity(intent);
            }
        });
        //设置爱心点赞
        CheckLove.check(whole.getPhotoID(), iv_love);
        iv_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = (int) iv_love.getTag();
                if (state == 1) {
                    forcount++;
                    lovenumber++;
                    tv_love.setText(lovenumber + "");
                    iv_love.setBackgroundResource(R.drawable.icon_love);
                    iv_love.setTag(2);
                    InsertLove.insert(whole.getPhotoID(), whole.getPohotUrl()
                            , whole.getAuthor1ID(), whole.getAuthor2ID());
                    UpdateWholeLove.update(whole.getPhotoID(), whole.getAuthor1ID(), whole.getAuthor2ID(), "up");
                } else if (state == 2) {
                    forcount++;
                    lovenumber--;
                    tv_love.setText(lovenumber + "");
                    iv_love.setBackgroundResource(R.drawable.icon_nolove);
                    iv_love.setTag(1);
                    DeleteLove.delete(whole.getPhotoID());
                    UpdateWholeLove.update(whole.getPhotoID(), whole.getAuthor1ID(), whole.getAuthor2ID(), "down");
                }
            }
        });

    }
}
