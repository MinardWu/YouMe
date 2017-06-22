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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.youme.R;
import com.minardwu.youme.adapter.Half;
import com.minardwu.youme.base.App;
import com.minardwu.youme.leancloud.CheckLove;
import com.minardwu.youme.leancloud.DeleteHalf;
import com.minardwu.youme.leancloud.DeleteLove;
import com.minardwu.youme.leancloud.InsertLove;
import com.minardwu.youme.leancloud.UpdateHalfLove;

import me.drakeet.materialdialog.MaterialDialog;

public class ShowOneHalfActivity extends AppCompatActivity {

    private Half half;
    private String userID;
    private long firstClick;
    private long lastClick;
    private int count;
    private static int lovenumber;
    private static int forcount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_one_half);

        ImageView iv_toolbar_delete = (ImageView) findViewById(R.id.iv_toolbar_delete);
        ImageView iv_toolabr_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        half = (Half) getIntent().getSerializableExtra("half");
        userID = (String) getIntent().getSerializableExtra("userID");

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
                final MaterialDialog mMaterialDialog = new MaterialDialog(ShowOneHalfActivity.this);
                mMaterialDialog.setTitle("操作")
                        .setMessage("删除该照片？")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DeleteHalf.delete(half.getPhotoID());
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

        //设置大体布局
        LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);
        LinearLayout ll_top = (LinearLayout) findViewById(R.id.ll_top);
        LinearLayout ll_left = (LinearLayout) findViewById(R.id.ll_left);
        LinearLayout ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        ImageView iv_right = (ImageView) findViewById(R.id.iv_right);
        ImageView iv_top = (ImageView) findViewById(R.id.iv_top);
        ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
        ImageView iv_bottom = (ImageView) findViewById(R.id.iv_bottom);

        iv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), TakeWholeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("position", half.getPosition());
                intent.putExtra("bytes", half.getBytes());
                intent.putExtra("author1ID", half.getAuthorID());
                intent.putExtra("author1name", half.getAuthorName());
                intent.putExtra("author1portraiturl", half.getAuthorPortraitUrl());
                App.getContext().startActivity(intent);
            }
        });
        iv_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), TakeWholeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("position", half.getPosition());
                intent.putExtra("author1ID", half.getAuthorID());
                intent.putExtra("author1name", half.getAuthorName());
                intent.putExtra("author1portraiturl", half.getAuthorPortraitUrl());
                intent.putExtra("bytes", half.getBytes());
                App.getContext().startActivity(intent);
            }
        });
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), TakeWholeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("position", half.getPosition());
                intent.putExtra("author1ID", half.getAuthorID());
                intent.putExtra("author1name", half.getAuthorName());
                intent.putExtra("author1portraiturl", half.getAuthorPortraitUrl());
                intent.putExtra("bytes", half.getBytes());
                App.getContext().startActivity(intent);
            }
        });
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), TakeWholeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("position", half.getPosition());
                intent.putExtra("author1ID", half.getAuthorID());
                intent.putExtra("author1name", half.getAuthorName());
                intent.putExtra("author1portraiturl", half.getAuthorPortraitUrl());
                intent.putExtra("bytes", half.getBytes());
                App.getContext().startActivity(intent);
            }
        });


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_half);
        LinearLayout.LayoutParams rl_LaLayoutParams = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
        rl_LaLayoutParams.height = App.ScreenWidth;
        rl_LaLayoutParams.width = App.ScreenWidth;
        relativeLayout.setLayoutParams(rl_LaLayoutParams);


        RelativeLayout.LayoutParams iv_top_LayoutParams = (RelativeLayout.LayoutParams) ll_top.getLayoutParams();
        iv_top_LayoutParams.height = App.ScreenWidth / 2;
        iv_top_LayoutParams.width = App.ScreenWidth;
        ll_top.setLayoutParams(iv_top_LayoutParams);

        RelativeLayout.LayoutParams iv_left_LayoutParams = (RelativeLayout.LayoutParams) ll_left.getLayoutParams();
        iv_left_LayoutParams.height = App.ScreenWidth;
        iv_left_LayoutParams.width = App.ScreenWidth / 2;
        ll_left.setLayoutParams(iv_left_LayoutParams);

        RelativeLayout.LayoutParams iv_bottom_LayoutParams = (RelativeLayout.LayoutParams) ll_bottom.getLayoutParams();
        iv_bottom_LayoutParams.height = App.ScreenWidth / 2;
        iv_bottom_LayoutParams.width = App.ScreenWidth;
        ll_bottom.setLayoutParams(iv_bottom_LayoutParams);

        RelativeLayout.LayoutParams iv_right_LayoutParams = (RelativeLayout.LayoutParams) ll_right.getLayoutParams();
        iv_right_LayoutParams.height = App.ScreenWidth;
        iv_right_LayoutParams.width = App.ScreenWidth / 2;
        ll_right.setLayoutParams(iv_right_LayoutParams);

        if (half.getPosition().equals("left")) {
            ll_left.setVisibility(View.GONE);
            ll_top.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);
            ll_right.setVisibility(View.VISIBLE);
        } else if (half.getPosition().equals("right")) {
            ll_top.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);
            ll_right.setVisibility(View.GONE);
            ll_left.setVisibility(View.VISIBLE);
        } else if (half.getPosition().equals("top")) {
            ll_left.setVisibility(View.GONE);
            ll_top.setVisibility(View.GONE);
            ll_right.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.VISIBLE);
        } else if (half.getPosition().equals("bottom")) {
            ll_left.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);
            ll_right.setVisibility(View.GONE);
            ll_top.setVisibility(View.VISIBLE);
        }
        //实例化控件
        SimpleDraweeView iv_userPortrait = (SimpleDraweeView) findViewById(R.id.iv_listview_user);
        TextView tv_username = (TextView) findViewById(R.id.tv_listview_username);
        SimpleDraweeView iv_half = (SimpleDraweeView) findViewById(R.id.iv_listview_photo);
        TextView tv_time = (TextView) findViewById(R.id.tv_listview_time);
        final ImageView iv_listview_love = (ImageView)findViewById(R.id.iv_listview_love);
        final TextView tv_listview_love = (TextView) findViewById(R.id.tv_listview_love);
        //设置头像点击事件
        iv_userPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.activityList.get(0), UserInfoActivity.class);
                intent.putExtra("userID", half.getAuthorID());
                intent.putExtra("userName", half.getAuthorName());
                intent.putExtra("userPortraitUrl", half.getAuthorPortraitUrl());
                App.activityList.get(0).startActivity(intent);
            }
        });
        //设置具体UI数据
        iv_userPortrait.setImageURI(Uri.parse(half.getAuthorPortraitUrl()));
        tv_username.setText(half.getAuthorName());
        tv_time.setText(half.getTime());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_half.getLayoutParams();
        layoutParams.height = App.ScreenWidth;
        layoutParams.width = App.ScreenWidth;
        iv_half.setLayoutParams(layoutParams);
        iv_half.setImageURI(Uri.parse(half.getPohotUrl()));
        //每次点击爱心getview函数都会再次执行，若不加条件lovenumber一直等于half.getLove()，所以应该价格条件让lovenumber初始化一次
        if (forcount == 0) {
            lovenumber = half.getLove();
        }
        tv_listview_love.setText(lovenumber + "");
        //设置爱心点赞
        CheckLove.check(half.getPhotoID(), iv_listview_love);
        iv_listview_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = (int) iv_listview_love.getTag();
                if (state == 1) {
                    forcount++;
                    lovenumber = lovenumber + 1;
                    tv_listview_love.setText(lovenumber + "");
                    iv_listview_love.setBackgroundResource(R.drawable.icon_love);
                    iv_listview_love.setTag(2);
                    InsertLove.insert(half.getPhotoID(), half.getPohotUrl(), half.getAuthorID(), null);
                    UpdateHalfLove.update(half.getPhotoID(), half.getAuthorID(), "up");
                } else if (state == 2) {
                    forcount++;
                    lovenumber = lovenumber - 1;
                    tv_listview_love.setText(lovenumber + "");
                    iv_listview_love.setBackgroundResource(R.drawable.icon_nolove);
                    iv_listview_love.setTag(1);
                    DeleteLove.delete(half.getPhotoID());
                    UpdateHalfLove.update(half.getPhotoID(), half.getAuthorID(), "down");
                }
            }
        });

    }
}
