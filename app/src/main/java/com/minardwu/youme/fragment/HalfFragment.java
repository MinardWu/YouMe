package com.minardwu.youme.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.minardwu.youme.adapter.Half;
import com.minardwu.youme.adapter.HalfAdapter;
import com.minardwu.youme.adapter.Whole;
import com.minardwu.youme.aty.TakeHalfActivity;
import com.minardwu.youme.aty.TakeWholeActivity;
import com.minardwu.youme.aty.UserInfoActivity;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetHalfReceiver;
import com.minardwu.youme.br.GetSkipHalfReceiver;
import com.minardwu.youme.br.GetSkipWholeReceiver;
import com.minardwu.youme.leancloud.CheckLove;
import com.minardwu.youme.leancloud.GetHalfInfo;
import com.minardwu.youme.leancloud.GetWholeInfo;
import com.minardwu.youme.leancloud.InsertLove;
import com.minardwu.youme.leancloud.UpdateHalfLove;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by MinardWu on 2016/3/7.
 */
public class HalfFragment extends android.support.v4.app.Fragment {

    private ListView listview;
    private List<Half> list;
    private ArrayAdapter adapter;
    private GetHalfReceiver getHalfReceiver;
    private GetSkipHalfReceiver getSkipHalfReceiver;
    private FloatingActionButton fab;
    private Button btn_load;
    private RotateAnimation rotateAnimation;
    private View view;

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
        GetHalfInfo.get(loadNum);
        //定义加载页面
        view = inflater.inflate(R.layout.fragment_half, container, false);
        btn_load = (Button) view.findViewById(R.id.btn_forload);
        btn_load.setEnabled(false);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(100);
        btn_load.startAnimation(rotateAnimation);
        //定义页面listview
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        listview = (ListView) view.findViewById(R.id.listview_half);
        list = new ArrayList<Half>();
//        moreView = inflater.inflate(R.layout.listview_loadmore, null);
//        tv_loadmore = (TextView) moreView.findViewById(R.id.tv_loadmore);
//        btn_loadmore = (Button) moreView.findViewById(R.id.btn_loadmore);
//        tv_loadmore.setVisibility(View.GONE);
//        btn_loadmore.setEnabled(false);
//        listview.addFooterView(moreView);
//        moreView.setVisibility(View.INVISIBLE);
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
                GetHalfInfo.get(loadNum);
            }
        });

        getHalfReceiver = new GetHalfReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getStringExtra("GetHalfInfoString")!=null){
                    if (intent.getStringExtra("GetHalfInfoString").equals("fail")) {
                        ptrFrameLayout.refreshComplete();
                        listview.setVisibility(View.GONE);
                        btn_load.clearAnimation();
                        btn_load.setEnabled(true);
                        btn_load.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                btn_load.setEnabled(false);
                                btn_load.startAnimation(rotateAnimation);
                                GetHalfInfo.get(loadNum);
                            }
                        });
                    } else if (intent.getStringExtra("GetHalfInfoString").equals("success")) {
                        ptrFrameLayout.refreshComplete();
                        List<Half> getlist = (List<Half>) intent.getSerializableExtra("GetHalfInfoList");
                        if (getlist != null) {
                            btn_load.clearAnimation();
                            btn_load.setVisibility(View.GONE);
                            list.clear();
                            for (int i = 0; i < getlist.size(); i++) {
                                list.add(getlist.get(i));
                                adapter.notifyDataSetChanged();
                                listview.setVisibility(View.VISIBLE);
                            }
                        }
                        listSize = list.size();
//                        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
//                            @Override
//                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                                lastItem = firstVisibleItem + visibleItemCount-1;
//                            }
//
//                            @Override
//                            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                                scrollStates = scrollState;
//                                App.getContext().registerReceiver(getSkipHalfReceiver,new IntentFilter(GetSkipHalfReceiver.ACTION));
//                                if(lastItem == listSize  && scrollState == this.SCROLL_STATE_IDLE){
//                                    int tempNum = skipNum *c;
//                                    moreView.setVisibility(View.VISIBLE);
//                                    GetHalfInfo.skip(tempNum);
//                                }
//                            }
//                        });
                    }
                }
            }

        };
        App.getContext().registerReceiver(getHalfReceiver, new IntentFilter(GetHalfReceiver.ACTION));
        //上拉刷新
//        getSkipHalfReceiver = new GetSkipHalfReceiver(){
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String response = intent.getStringExtra("GetSkipHalfInfoString");
//                if(response!=null){
//                    if(response.equals("success")){
//                        Toast.makeText(App.getContext(), "1", Toast.LENGTH_SHORT).show();
//                        List<Half> foraddList = (List<Half>) intent.getSerializableExtra("GetSkipHalfInfoList");
//                        list.addAll(foraddList);
//                        listSize = list.size();
//                        adapter.notifyDataSetChanged();
//                        moreView.setVisibility(View.VISIBLE);
//                        c++;
//                    }else if(response.equals("nomore")){
//                        Toast.makeText(App.getContext(), "2", Toast.LENGTH_SHORT).show();
//                        btn_loadmore.setVisibility(View.GONE);
//                        tv_loadmore.setVisibility(View.VISIBLE);
//                    }else {
//                        Toast.makeText(App.getContext(), "3", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(App.getContext(), "加载失败", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        };

        adapter = new HalfAdapter(App.getContext(), R.layout.listview_half, list);
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


    //=========================华丽的分割线=========================


    public class HalfAdapter extends ArrayAdapter<Half> {

        private int resourceID;

        class ViewHolder {
            int position = 0;
            private TextView tv_love;
            private ImageView iv_love;
        }

        public HalfAdapter(Context context, int resource, List<Half> objects) {
            super(context, resource, objects);
            resourceID = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Half half = getItem(position);
            View view;
            final ViewHolder holder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceID, null);
                holder = new ViewHolder();
                //下这步很重要，保证值更新特定item
                final ViewHolder fHolder = holder;
                holder.iv_love = (ImageView) view.findViewById(R.id.iv_listview_love);
                holder.tv_love = (TextView) view.findViewById(R.id.tv_listview_love);
                holder.iv_love.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int state = (int) holder.iv_love.getTag();
                        if (state == 1) {
                            String lovenumber = holder.tv_love.getText().toString();
                            int after = Integer.parseInt(lovenumber);
                            updataItem(fHolder.position, after + 1 + "");
                            holder.iv_love.setBackgroundResource(R.drawable.icon_love);
                            holder.iv_love.setTag(2);
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
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
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
            //初始化lovenumber数据
            holder.position = position;
            holder.tv_love.setText(half.getLove()+"");
            //设置具体UI数据
            iv_userPortrait.setImageURI(Uri.parse(half.getAuthorPortraitUrl()));
            tv_username.setText(half.getAuthorName());
            tv_time.setText(half.getTime());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_whole.getLayoutParams();
            layoutParams.width = App.ScreenWidth;
            layoutParams.height = App.ScreenWidth;
            iv_whole.setLayoutParams(layoutParams);
            iv_whole.setImageURI(Uri.parse(half.getPohotUrl()));
            //检查是否点赞
            CheckLove.check(half.getPhotoID(), holder.iv_love);
            return view;
        }


        public void updataItem(int position, String fornum) {
            if (scrollStates == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                int firstvisible = listview.getFirstVisiblePosition();
                int lastvisibale = listview.getLastVisiblePosition();
                if (position >= firstvisible && position <= lastvisibale) {
                    View view = listview.getChildAt(position - firstvisible);
                    HalfAdapter.ViewHolder viewHolder = (HalfAdapter.ViewHolder) view.getTag();
                    //updataItem有两步需要进行
                    //第一是更新whole的数据，因为不然listview滚动实现getview方法，其中holder.tv_love.setText(whole.getLove()+"")会使数据会变回原来的样子
                    Half half = getItem(position);
                    half.setLove(Integer.parseInt(fornum));
                    //第二是及时更新界面数据
                    viewHolder.tv_love.setText(Integer.parseInt(fornum)+"");
                }
            }

        }

    }

}


