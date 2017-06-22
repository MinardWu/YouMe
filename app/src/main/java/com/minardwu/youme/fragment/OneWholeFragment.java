package com.minardwu.youme.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.minardwu.youme.R;
import com.minardwu.youme.adapter.OneUserWholeAdapter;
import com.minardwu.youme.adapter.Whole;
import com.minardwu.youme.aty.ShowOneWholeActivity;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetOneUserWholeReceiver;
import com.minardwu.youme.leancloud.GetOneUserHalf;
import com.minardwu.youme.leancloud.GetOneUserWhole;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class OneWholeFragment extends android.support.v4.app.Fragment {


    private GetOneUserWholeReceiver getOneUserWholeReceiver;
    private Button btn_load;
    private RotateAnimation rotateAnimation;
    private GridView gv_whole;
    private List<Whole> list;
    private OneUserWholeAdapter oneUserWholeAdapter;
    private String userID;
    private TextView tv_nowhole;

    public OneWholeFragment(String userID){
        this.userID = userID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //初始化控件
        View view = inflater.inflate(R.layout.fragment_one_whole,container,false);
        gv_whole = (GridView) view.findViewById(R.id.gv_whole);
        btn_load = (Button) view.findViewById(R.id.btn_forload);
        tv_nowhole = (TextView) view.findViewById(R.id.tv_nowhole);
        //加载动画
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(100);
        //设置控件状态
        btn_load.setEnabled(false);
        btn_load.startAnimation(rotateAnimation);
        tv_nowhole.setVisibility(View.GONE);

        //获取个人whole
        list = new ArrayList<Whole>();
        getOneUserWholeReceiver = new GetOneUserWholeReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getStringExtra("GetOneUserWholeString").equals("success")) {
                    btn_load.clearAnimation();
                    btn_load.setVisibility(View.GONE);
                    list.clear();
                    list.addAll((List<Whole>) intent.getSerializableExtra("GetOneUserWholeSuccess"));
                    oneUserWholeAdapter.notifyDataSetChanged();
                } else if (intent.getStringExtra("GetOneUserWholeString").equals("fail")) {
                    btn_load.clearAnimation();
                    btn_load.setEnabled(true);
                    btn_load.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btn_load.setEnabled(false);
                            btn_load.startAnimation(rotateAnimation);
                            GetOneUserWhole.get(userID);
                        }
                    });
                }else if (intent.getStringExtra("GetOneUserWholeString").equals("noWhole")) {
                    btn_load.setVisibility(View.GONE);
                    gv_whole.setVisibility(View.GONE);
                    tv_nowhole.setVisibility(View.VISIBLE);
                }
            }
        };
        App.getContext().registerReceiver(getOneUserWholeReceiver, new IntentFilter(GetOneUserWholeReceiver.ACTION));
        oneUserWholeAdapter = new OneUserWholeAdapter(App.getContext(), R.layout.listview_gridview_whole, list);
        gv_whole.setAdapter(oneUserWholeAdapter);

        gv_whole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Whole whole = (Whole) gv_whole.getAdapter().getItem(position);
                Intent intent = new Intent(App.activityList.get(0),ShowOneWholeActivity.class);
                intent.putExtra("whole",whole);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetOneUserWhole.get(userID);
    }
}
