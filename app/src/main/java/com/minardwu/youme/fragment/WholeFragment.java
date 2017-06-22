package com.minardwu.youme.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.melnykov.fab.FloatingActionButton;
import com.minardwu.youme.R;
import com.minardwu.youme.aty.TakeHalfActivity;
import com.minardwu.youme.aty.UserInfoActivity;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetSkipWholeReceiver;
import com.minardwu.youme.br.GetWholeReceiver;
import com.minardwu.youme.leancloud.CheckLove;
import com.minardwu.youme.leancloud.GetWholeInfo;
import com.minardwu.youme.adapter.Whole;
import com.minardwu.youme.leancloud.InsertLove;
import com.minardwu.youme.leancloud.UpdateWholeLove;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class WholeFragment extends android.support.v4.app.Fragment{

    private ListView listview;
    private List<Whole> list;
    private ArrayAdapter adapter;
    private GetWholeReceiver getWholeReceiver = null;
    private GetSkipWholeReceiver getSkipWholeReceiver = null;
    private FloatingActionButton fab;
    private Button btn_load;
    private RotateAnimation rotateAnimation;

    private int scrollStates;
    private int lastItem;
    private int listSize;

    private View moreView;
    private TextView tv_loadmore;
    private Button btn_loadmore;
    private int loadNum = 4;
    private int skipNum = 4;
    private int c = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //获取数据
        GetWholeInfo.get(loadNum);
        //初始化控件
        View view = inflater.inflate(R.layout.fragment_whole, container, false);
        btn_load = (Button) view.findViewById(R.id.btn_forload);
        btn_load.setEnabled(false);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(100);
        btn_load.startAnimation(rotateAnimation);
        //定义页面listview
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        listview = (ListView) view.findViewById(R.id.listview_mainactivity);
        list = new ArrayList<Whole>();
        moreView = inflater.inflate(R.layout.listview_loadmore, null);
        tv_loadmore = (TextView) moreView.findViewById(R.id.tv_loadmore);
        btn_loadmore = (Button) moreView.findViewById(R.id.btn_loadmore);
        tv_loadmore.setVisibility(View.GONE);
        btn_loadmore.setEnabled(false);
        listview.addFooterView(moreView);
        moreView.setVisibility(View.INVISIBLE);
        //下拉刷新
        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout)view.findViewById(R.id.fragment_ptr);
        StoreHouseHeader header = new StoreHouseHeader(App.getContext());
        header.setPadding(0, 10, 0, 10);
        header.initWithString("Loading");
        header.setTextColor(Color.rgb(43, 53, 56));
        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                GetWholeInfo.get(loadNum);
            }
        });
        //下拉刷新接受广播
        getWholeReceiver = new GetWholeReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getStringExtra("GetWholeInfoString") != null) {
                    if (intent.getStringExtra("GetWholeInfoString").equals("fail")) {
                        ptrFrameLayout.refreshComplete();
                        btn_load.clearAnimation();
                        btn_load.setEnabled(true);
                        btn_load.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                btn_load.setEnabled(false);
                                btn_load.startAnimation(rotateAnimation);
                                GetWholeInfo.get(loadNum);
                            }
                        });
                    } else if (intent.getStringExtra("GetWholeInfoString").equals("ok")) {
                        List<Whole> getlist = (List<Whole>) intent.getSerializableExtra("GetWholeInfo");
                        if (getlist != null) {
                            ptrFrameLayout.refreshComplete();
                            btn_load.clearAnimation();
                            btn_load.setVisibility(View.GONE);
                            list.clear();
                            for (int i = 0; i < getlist.size(); i++) {
                                list.add(getlist.get(i));
                                adapter.notifyDataSetChanged();
                            }
                            listSize = list.size();
                            listview.setOnScrollListener(new AbsListView.OnScrollListener() {

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                    lastItem = firstVisibleItem + visibleItemCount-1;
                                }

                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {
                                    scrollStates = scrollState;
                                    App.getContext().registerReceiver(getSkipWholeReceiver,new IntentFilter(GetSkipWholeReceiver.ACTION));
                                    if(lastItem == listSize  && scrollState == this.SCROLL_STATE_IDLE){
                                        int tempNum = skipNum *c;
                                        moreView.setVisibility(View.VISIBLE);
                                        GetWholeInfo.skip(tempNum);
                                    }
                                }
                            });
                        }
                    }
                }
            }

        };
        App.getContext().registerReceiver(getWholeReceiver, new IntentFilter(GetWholeReceiver.ACTION));
        //上拉刷新接受广播
        getSkipWholeReceiver = new GetSkipWholeReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                String response = intent.getStringExtra("GetSkipWholeInfoString");
                if(response!=null){
                    if(response.equals("success")){
                        List<Whole> foraddList = (List<Whole>) intent.getSerializableExtra("GetSkipWholeInfo");
                        list.addAll(foraddList);
                        listSize = list.size();
                        adapter.notifyDataSetChanged();
                        moreView.setVisibility(View.VISIBLE);
                        c++;
                    }else if(response.equals("nomore")){
                        btn_loadmore.setVisibility(View.GONE);
                        tv_loadmore.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(App.getContext(),"加载失败",Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        adapter = new WholeAdapter(App.getContext(), R.layout.listview_whole, list);
        listview.setAdapter(adapter);

        fab.attachToListView(listview);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(App.activityList.get(0), TakeHalfActivity.class));
            }
        });

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getContext().unregisterReceiver(getWholeReceiver);
        App.getContext().unregisterReceiver(getSkipWholeReceiver);
    }

    //=========================华丽的分割线=========================

    public class WholeAdapter extends ArrayAdapter<Whole> {

        private int resourceID;
        private long firstClick;
        private long lastClick;
        private List<Whole> list;
        private int clickCount;

        class ViewHolder {
            int position = 0;
            private TextView tv_love;
            private ImageView iv_love;
        }

        public WholeAdapter(Context context, int resource, List<Whole> objects) {
            super(context, resource, objects);
            resourceID = resource;
            this.list = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Whole whole = getItem(position);
            View view;
            final ViewHolder holder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceID, null);
                holder = new ViewHolder();
                //下这步很重要，保证值更新特定item
                final ViewHolder fHolder = holder;
                holder.tv_love = (TextView) view.findViewById(R.id.tv_listview_love);
                holder.iv_love = (ImageView) view.findViewById(R.id.iv_listview_love);
                holder.iv_love.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int state = (int) holder.iv_love.getTag();
                        if (state == 1) {
                            String lovenumber = holder.tv_love.getText().toString();
                            int after = Integer.parseInt(lovenumber);
                            updataItem(fHolder.position,after+1+"");
                            holder.iv_love.setBackgroundResource(R.drawable.icon_love);
                            holder.iv_love.setTag(2);
                            //点一次赞要对三个表进行操作，插入love表，更新number表用户的赞数，更新whole表照片的赞数
                            InsertLove.insert(whole.getPhotoID(), whole.getPohotUrl(), whole.getAuthor1ID(), whole.getAuthor2ID());
                            UpdateWholeLove.update(whole.getPhotoID(), whole.getAuthor1ID(), whole.getAuthor2ID(), "up");
                            //推送
                            //同一个人发的就推送一次
                            if (whole.getAuthor1ID().equals(whole.getAuthor2ID())) {
                                //不是自己发的才推送
                                if (!whole.getAuthor1ID().equals(AVUser.getCurrentUser().getObjectId())) {
                                    AVQuery pushQuery = AVInstallation.getQuery();
                                    pushQuery.whereEqualTo("installationId", whole.getAuthor1installationID());
                                    AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " 赞了你的照片", pushQuery, new SendCallback() {
                                        @Override
                                        public void done(AVException e) {
                                        }
                                    });
                                }
                            } else {
                                if (!whole.getAuthor1ID().equals(AVUser.getCurrentUser().getObjectId())) {
                                    AVQuery pushQuery = AVInstallation.getQuery();
                                    pushQuery.whereEqualTo("installationId", whole.getAuthor1installationID());
                                    AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " 赞了你的照片", pushQuery, new SendCallback() {
                                        @Override
                                        public void done(AVException e) {
                                        }
                                    });
                                }
                                if (!whole.getAuthor2ID().equals(AVUser.getCurrentUser().getObjectId())) {
                                    AVQuery pushQuery2 = AVInstallation.getQuery();
                                    pushQuery2.whereEqualTo("installationId", whole.getAuthor2installationID());
                                    AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " 赞了你的照片", pushQuery2, new SendCallback() {
                                        @Override
                                        public void done(AVException e) {
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            CheckLove.check(whole.getPhotoID(),holder.iv_love);
            //初始化lovenumber数据
            holder.position = position;
            holder.tv_love.setText(whole.getLove()+"");
            //实例化控件
            TextView tv_username1 = (TextView) view.findViewById(R.id.tv_listview_username1);
            TextView tv_username2 = (TextView) view.findViewById(R.id.tv_listview_username2);
            SimpleDraweeView iv_user1portrait = (SimpleDraweeView) view.findViewById(R.id.iv_listview_user1);
            SimpleDraweeView iv_user2portrait = (SimpleDraweeView) view.findViewById(R.id.iv_listview_user2);
            SimpleDraweeView iv_whole = (SimpleDraweeView) view.findViewById(R.id.iv_listview_photo);
            TextView tv_time = (TextView) view.findViewById(R.id.tv_listview_time);
            //设置进度条
            //enericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(App.getContext().getResources());
            //GenericDraweeHierarchy hierarchy = builder
            //        .setProgressBarImage(new ProgressBarDrawable())
            //        .build();
            //iv_whole.setHierarchy(hierarchy);
            //设置控件数据
            tv_username1.setText(whole.getAuthor1());
            tv_username2.setText(whole.getAuthor2());
            iv_user1portrait.setImageURI(Uri.parse(whole.getAuthor1PortraitUrl()));
            iv_user2portrait.setImageURI(Uri.parse(whole.getAuthor2PortraitUrl()));
            tv_time.setText(whole.getTime());
            //设置头像点击事件
            iv_user1portrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(App.activityList.get(0), UserInfoActivity.class);
                    intent.putExtra("userID", whole.getAuthor1ID());
                    intent.putExtra("userName", whole.getAuthor1());
                    intent.putExtra("userPortraitUrl", whole.getAuthor1PortraitUrl());
                    App.activityList.get(0).startActivity(intent);
                }
            });
            iv_user2portrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(App.activityList.get(0), UserInfoActivity.class);
                    intent.putExtra("userID", whole.getAuthor2ID());
                    intent.putExtra("userName", whole.getAuthor2());
                    intent.putExtra("userPortraitUrl", whole.getAuthor2PortraitUrl());
                    App.activityList.get(0).startActivity(intent);
                }
            });
            //设置照片大小
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_whole.getLayoutParams();
            layoutParams.width = App.ScreenWidth;
            layoutParams.height = App.ScreenWidth;
            iv_whole.setLayoutParams(layoutParams);
            //设置照片双击点赞
            iv_whole.setImageURI(Uri.parse(whole.getPohotUrl()));
            iv_whole.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
                            if (firstClick != 0 && System.currentTimeMillis() - firstClick > 300) {
                                clickCount = 0;
                            }
                            clickCount++;
                            if (clickCount == 1) {
                                firstClick = System.currentTimeMillis();
                            } else if (clickCount == 2) {
                                lastClick = System.currentTimeMillis();
                                // 两次点击小于300ms 也就是连续点击
                                if (lastClick - firstClick < 300) {
                                    // 判断是否是执行了双击事件
                                    //只有state为1，即未被点赞时才能才触发动画
                                    int state = (int) holder.iv_love.getTag();
                                    if (state == 1) {

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

        public void updataItem(int position, String fornum) {
            if (scrollStates == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                int firstvisible = listview.getFirstVisiblePosition();
                int lastvisibale = listview.getLastVisiblePosition();
                if (position >= firstvisible && position <= lastvisibale) {
                    View view = listview.getChildAt(position - firstvisible);
                    WholeAdapter.ViewHolder viewHolder = (WholeAdapter.ViewHolder) view.getTag();
                    //updataItem有两步需要进行
                    //第一是更新whole的数据，因为不然listview滚动实现getview方法，其中holder.tv_love.setText(whole.getLove()+"")会使数据会变回原来的样子
                    Whole whole = getItem(position);
                    whole.setLove(Integer.parseInt(fornum));
                    //第二是及时更新界面数据
                    viewHolder.tv_love.setText(Integer.parseInt(fornum)+"");
                }
            }

        }
    }
}
