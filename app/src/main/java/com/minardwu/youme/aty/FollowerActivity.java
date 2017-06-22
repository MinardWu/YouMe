package com.minardwu.youme.aty;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minardwu.youme.R;
import com.minardwu.youme.adapter.ForFollow;
import com.minardwu.youme.adapter.FollowAdapter;
import com.minardwu.youme.br.GetFollowerReceiver;
import com.minardwu.youme.leancloud.GetFollower;

import java.util.List;

public class FollowerActivity extends AppCompatActivity {

    private List<ForFollow> list;
    private ListView listView;
    private FollowAdapter followAdapter;
    private GetFollowerReceiver getFollowerReceiver;
    private ImageView iv_toolabr_back;
    private TextView tv_noFollower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        String userID = getIntent().getStringExtra("userID");

        listView = (ListView) findViewById(R.id.listview_follower);
        tv_noFollower = (TextView) findViewById(R.id.tv_noFollower);
        iv_toolabr_back = (ImageView) findViewById(R.id.iv_toolbar_back);

        iv_toolabr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_noFollower.setVisibility(View.GONE);

        GetFollower.get(userID);
        getFollowerReceiver= new GetFollowerReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("GetFollowerString").equals("success")){
                    list = (List<ForFollow>) intent.getSerializableExtra("GetFollowerData");
                    followAdapter = new FollowAdapter(FollowerActivity.this,R.layout.listview_for_follow,list);
                    listView.setAdapter(followAdapter);
                }else if(intent.getStringExtra("GetFollowerString").equals("noFollower")){
                    listView.setVisibility(View.GONE);
                    tv_noFollower.setVisibility(View.VISIBLE);
                }
            }
        };
        registerReceiver(getFollowerReceiver,new IntentFilter(GetFollowerReceiver.ACTION));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForFollow forFollow = (ForFollow) listView.getAdapter().getItem(position);
                Intent intent = new Intent(FollowerActivity.this,UserInfoActivity.class);
                intent.putExtra("userID", forFollow.getUserID());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(getFollowerReceiver);
    }
}
