package com.minardwu.youme.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SendCallback;
import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.youme.R;
import com.minardwu.youme.aty.UserInfoActivity;
import com.minardwu.youme.base.App;
import com.minardwu.youme.leancloud.DeleteLove;
import com.minardwu.youme.leancloud.CheckLove;
import com.minardwu.youme.leancloud.InsertLove;
import com.minardwu.youme.leancloud.UpdateWholeLove;

import java.util.List;

public class WholesAdapter extends ArrayAdapter<Whole> {

    private int resourceID;
    private long firstClick;
    private long lastClick;
    private int count;
    private int lovenumber;
    private static int forcount = 0;
    private List<Whole> list;

    public WholesAdapter(Context context, int resource, List<Whole> objects) {
        super(context, resource, objects);
        resourceID = resource;
        this.list = objects;
    }

    class ViewHolder{
        private TextView tv_love;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Whole whole = getItem(position);
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            holder = new ViewHolder();
            holder.tv_love = (TextView) view.findViewById(R.id.tv_listview_love);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_love.setText(whole.getLove() + "");
        //实例化控件
        TextView tv_list_username1 = (TextView) view.findViewById(R.id.tv_listview_username1);
        TextView tv_list_username2 = (TextView) view.findViewById(R.id.tv_listview_username2);
        SimpleDraweeView iv_listview_user1 = (SimpleDraweeView) view.findViewById(R.id.iv_listview_user1);
        SimpleDraweeView iv_listview_user2 = (SimpleDraweeView) view.findViewById(R.id.iv_listview_user2);
        final SimpleDraweeView iv_listview_photo = (SimpleDraweeView) view.findViewById(R.id.iv_listview_photo);
        //设置进度条
        //enericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(App.getContext().getResources());
        //GenericDraweeHierarchy hierarchy = builder
        //        .setProgressBarImage(new ProgressBarDrawable())
        //        .build();
        //iv_listview_photo.setHierarchy(hierarchy);
        TextView ty_listview_time = (TextView) view.findViewById(R.id.tv_listview_time);
        final ImageView iv_listview_love = (ImageView) view.findViewById(R.id.iv_listview_love);
//        final TextView tv_listview_love = (TextView) view.findViewById(R.id.tv_listview_love);
        //设置控件数据
        tv_list_username1.setText(whole.getAuthor1());
        tv_list_username2.setText(whole.getAuthor2());
        iv_listview_user1.setImageURI(Uri.parse(whole.getAuthor1PortraitUrl()));
        iv_listview_user2.setImageURI(Uri.parse(whole.getAuthor2PortraitUrl()));
        ty_listview_time.setText(whole.getTime());
//        tv_listview_love.setText(whole.getLove() + "");
        //每次点击爱心getview函数都会再次执行，若不加条件lovenumber一直等于half.getLove()，所以应该价格条件让lovenumber初始化一次
        if (forcount == 0) {
            lovenumber = whole.getLove();
        }
        //设置头像点击事件
        iv_listview_user1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.activityList.get(0), UserInfoActivity.class);
                intent.putExtra("userID", whole.getAuthor1ID());
                intent.putExtra("userName", whole.getAuthor1());
                intent.putExtra("userPortraitUrl", whole.getAuthor1PortraitUrl());
                App.activityList.get(0).startActivity(intent);
            }
        });
        iv_listview_user2.setOnClickListener(new View.OnClickListener() {
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
        CheckLove.check(whole.getPhotoID(), iv_listview_love);
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(1000);
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(0, 500, 0, 500, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation1.setDuration(1000);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation2.setDuration(500);
        animationSet.addAnimation(scaleAnimation1);
        animationSet.addAnimation(scaleAnimation2);
        iv_listview_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "sd", Toast.LENGTH_SHORT).show();
//                int state = (int) iv_listview_love.getTag();
//                if (state == 1) {
//                    forcount++;
//                    lovenumber++;
////                    tv_listview_love.setText(lovenumber + "");
//                    iv_listview_love.setBackgroundResource(R.drawable.icon_love);
//                    iv_listview_love.setTag(2);
//                    iv_listview_love.setClickable(false);
//                    iv_love_anim.startAnimation(animationSet);
//                    //点一次赞要对三个表进行操作，插入love表，更新number表用户的赞数，更新whole表照片的赞数
//                    InsertLove.insert(whole.getPhotoID(), whole.getPohotUrl(), whole.getAuthor1ID(), whole.getAuthor2ID());
//                    UpdateWholeLove.update(whole.getPhotoID(), whole.getAuthor1ID(), whole.getAuthor2ID(), "up");
//                    //推送
//                    //同一个人发的就推送一次
//                    if (whole.getAuthor1ID().equals(whole.getAuthor2ID())) {
//                        //不是自己发的才推送
//                        if (!whole.getAuthor1ID().equals(AVUser.getCurrentUser().getObjectId())) {
//                            AVQuery pushQuery = AVInstallation.getQuery();
//                            pushQuery.whereEqualTo("installationId", whole.getAuthor1installationID());
//                            AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " 赞了你的照片", pushQuery, new SendCallback() {
//                                @Override
//                                public void done(AVException e) {
//                                }
//                            });
//                        }
//                    } else {
//                        if (!whole.getAuthor1ID().equals(AVUser.getCurrentUser().getObjectId())) {
//                            AVQuery pushQuery = AVInstallation.getQuery();
//                            pushQuery.whereEqualTo("installationId", whole.getAuthor1installationID());
//                            AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " 赞了你的照片", pushQuery, new SendCallback() {
//                                @Override
//                                public void done(AVException e) {
//                                }
//                            });
//                        }
//                        if (!whole.getAuthor2ID().equals(AVUser.getCurrentUser().getObjectId())) {
//                            AVQuery pushQuery2 = AVInstallation.getQuery();
//                            pushQuery2.whereEqualTo("installationId", whole.getAuthor2installationID());
//                            AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " 赞了你的照片", pushQuery2, new SendCallback() {
//                                @Override
//                                public void done(AVException e) {
//                                }
//                            });
//                        }
//                    }
//                } else if (state == 2) {
//                    forcount++;
//                    lovenumber--;
////                    tv_listview_love.setText(lovenumber + "");
//                    iv_listview_love.setBackgroundResource(R.drawable.icon_nolove);
//                    iv_listview_love.setTag(1);
//                    DeleteLove.delete(whole.getPhotoID());
//                    UpdateWholeLove.update(whole.getPhotoID(), whole.getAuthor1ID(), whole.getAuthor2ID(), "down");
//                }


            }
        });


        //设置照片双击点赞
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_listview_photo.getLayoutParams();
        layoutParams.height = App.ScreenWidth;
        iv_listview_photo.setLayoutParams(layoutParams);
        iv_listview_photo.setImageURI(Uri.parse(whole.getPohotUrl()));
        iv_listview_photo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
                        if (firstClick != 0 && System.currentTimeMillis() - firstClick > 300) {
                            count = 0;
                        }
                        count++;
                        if (count == 1) {
                            firstClick = System.currentTimeMillis();
                        } else if (count == 2) {
                            lastClick = System.currentTimeMillis();
                            // 两次点击小于300ms 也就是连续点击
                            if (lastClick - firstClick < 300) {
                                // 判断是否是执行了双击事件
                                //只有state为1，即未被点赞时才能才触发动画
                                int state = (int) iv_listview_love.getTag();
                                if (state == 1) {
//                                    iv_listview_love.setBackgroundResource(R.drawable.icon_love);
//                                    iv_listview_love.setTag(2);
//                                    iv_listview_love.setActivated(true);
//                                    iv_love_anim.startAnimation(animationSet);
                                }

                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });

        return view;
    }

}
