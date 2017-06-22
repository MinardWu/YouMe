package com.minardwu.youme.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SendCallback;
import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.youme.R;
import com.minardwu.youme.aty.TakeWholeActivity;
import com.minardwu.youme.aty.UserInfoActivity;
import com.minardwu.youme.base.App;
import com.minardwu.youme.leancloud.DeleteLove;
import com.minardwu.youme.leancloud.CheckLove;
import com.minardwu.youme.leancloud.InsertLove;
import com.minardwu.youme.leancloud.UpdateHalfLove;

import java.util.List;

/**
 * Created by MinardWu on 2016/3/9.
 */
public class HalfAdapter extends ArrayAdapter<Half> {

    private int resourceID;
    private static int lovenumber;

    public HalfAdapter(Context context, int resource, List<Half> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Half half = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
        } else {
            view = convertView;
        }
        //设置大体布局
        LinearLayout ll_right = (LinearLayout) view.findViewById(R.id.ll_right);
        LinearLayout ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
        LinearLayout ll_left = (LinearLayout) view.findViewById(R.id.ll_left);
        LinearLayout ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
        ImageView iv_right = (ImageView) view.findViewById(R.id.iv_right);
        ImageView iv_top = (ImageView) view.findViewById(R.id.iv_top);
        ImageView iv_left = (ImageView) view.findViewById(R.id.iv_left);
        ImageView iv_bottom = (ImageView) view.findViewById(R.id.iv_bottom);

        iv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), TakeWholeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("position", half.getPosition());
                intent.putExtra("bytes", half.getBytes());
                intent.putExtra("author1ID", half.getAuthorID());
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
                intent.putExtra("bytes", half.getBytes());
                App.getContext().startActivity(intent);
            }
        });


        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_half);
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
        SimpleDraweeView iv_userPortrait = (SimpleDraweeView) view.findViewById(R.id.iv_listview_user);
        TextView tv_username = (TextView) view.findViewById(R.id.tv_listview_username);
        SimpleDraweeView iv_whole = (SimpleDraweeView) view.findViewById(R.id.iv_listview_photo);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_listview_time);
        final ImageView iv_love = (ImageView) view.findViewById(R.id.iv_listview_love);
        final TextView tv_love = (TextView) view.findViewById(R.id.tv_listview_love);
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
        tv_love.setText(half.getLove() + "");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_whole.getLayoutParams();
        layoutParams.width = App.ScreenWidth;
        layoutParams.height = App.ScreenWidth;
        iv_whole.setLayoutParams(layoutParams);
        iv_whole.setImageURI(Uri.parse(half.getPohotUrl()));
        //设置爱心点赞
        CheckLove.check(half.getPhotoID(), iv_love);
        iv_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = (int) iv_love.getTag();
                if (state == 1) {
                    Half half = getItem(position);
                    tv_love.setText(half.getLove()+1+ "");
                    half.setLove(half.getLove() + 1);
                    tv_love.setText(lovenumber + "");
                    iv_love.setBackgroundResource(R.drawable.icon_love);
                    iv_love.setTag(2);
                    //点一次赞要对三个表进行操作，插入love表，更新number表用户的赞数，更新whole表照片的赞数
                    InsertLove.insert(half.getPhotoID(), half.getPohotUrl(), half.getAuthorID(), null);
                    UpdateHalfLove.update(half.getPhotoID(), half.getAuthorID(), "up");
                    //若果不是自己的照片则推送消息
                    if (!half.getAuthorID().equals(AVUser.getCurrentUser().getObjectId())) {
                        AVQuery pushQuery = AVInstallation.getQuery();
                        pushQuery.whereEqualTo("installationId", half.getAuthorinstallationID());
                        AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " 赞了你的照片", pushQuery, new SendCallback() {
                            @Override
                            public void done(AVException e) {
                            }
                        });
                    }
                }
            }
        });

        return view;
    }


}
