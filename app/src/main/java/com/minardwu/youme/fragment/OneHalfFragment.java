package com.minardwu.youme.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import com.minardwu.youme.R;
import com.minardwu.youme.adapter.Half;
import com.minardwu.youme.adapter.OneUserHalfAdapter;
import com.minardwu.youme.aty.ShowOneHalfActivity;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetHalfReceiver;
import com.minardwu.youme.br.GetOneUserHalfReceiver;
import com.minardwu.youme.leancloud.GetOneUserHalf;

import java.util.ArrayList;
import java.util.List;

public class OneHalfFragment extends android.support.v4.app.Fragment {

    private GetHalfReceiver getHalfReceiver;
    private GetOneUserHalfReceiver getOneUserHalfReceiver;
    private OneUserHalfAdapter oneUserHalfAdapter;
    private Button btn_load;
    private RotateAnimation rotateAnimation;
    private GridView gv_half;
    private List<Half> list;
    private String userID;
    private TextView tv_nohalf;

    public OneHalfFragment(String userID) {
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
        View view = inflater.inflate(R.layout.fragment_one_half, container, false);
        gv_half = (GridView) view.findViewById(R.id.gv_whole);
        btn_load = (Button) view.findViewById(R.id.btn_forload);
        tv_nohalf = (TextView) view.findViewById(R.id.tv_nohalf);
        //加载动画
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(100);
        //设置控件状态
        btn_load.setEnabled(false);
        btn_load.startAnimation(rotateAnimation);
        tv_nohalf.setVisibility(View.GONE);
        //获取个人half
        list = new ArrayList<Half>();
        getOneUserHalfReceiver = new GetOneUserHalfReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getStringExtra("GetOneUserHalfString").equals("fail")) {
                    gv_half.setVisibility(View.GONE);
                    btn_load.clearAnimation();
                    btn_load.setEnabled(true);
                    btn_load.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btn_load.setEnabled(false);
                            btn_load.startAnimation(rotateAnimation);
                            GetOneUserHalf.get(userID);
                        }
                    });
                } else if (intent.getStringExtra("GetOneUserHalfString").equals("success")) {
                    btn_load.clearAnimation();
                    btn_load.setVisibility(View.GONE);
                    list.clear();
                    list.addAll((List < Half >) intent.getSerializableExtra("GetOneUserHalfData"));
                    oneUserHalfAdapter.notifyDataSetChanged();
                } else if (intent.getStringExtra("GetOneUserHalfString").equals("noHalf")) {
                    btn_load.setVisibility(View.GONE);
                    gv_half.setVisibility(View.GONE);
                    tv_nohalf.setVisibility(View.VISIBLE);
                }
            }

        };
        App.getContext().registerReceiver(getOneUserHalfReceiver, new IntentFilter(GetOneUserHalfReceiver.ACTION));
        oneUserHalfAdapter = new OneUserHalfAdapter(App.getContext(), R.layout.listview_gridview_whole, list);
        gv_half.setAdapter(oneUserHalfAdapter);

        gv_half.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Half half = (Half) gv_half.getAdapter().getItem(position);
                Intent intent = new Intent(App.activityList.get(0), ShowOneHalfActivity.class);
                intent.putExtra("half", half);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetOneUserHalf.get(userID);
    }
}
