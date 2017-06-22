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
import android.widget.Toast;

import com.minardwu.youme.R;
import com.minardwu.youme.adapter.FollowAdapter;
import com.minardwu.youme.adapter.ForFollow;
import com.minardwu.youme.br.GetFolloweeReceiver;
import com.minardwu.youme.leancloud.GetFollowee;

import java.util.List;

public class FolloweeActivity extends AppCompatActivity {

    private List<ForFollow> list;
    private ImageView iv_toolabr_back;
    private ListView listView;
    private FollowAdapter followAdapter;
    private GetFolloweeReceiver getFolloweeReceiver;
    private TextView tv_noFollowee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followee);

        String userID = getIntent().getStringExtra("userID");

        listView = (ListView) findViewById(R.id.listview_followee);
        tv_noFollowee = (TextView) findViewById(R.id.tv_noFollowee);
        iv_toolabr_back = (ImageView) findViewById(R.id.iv_toolbar_back);

        iv_toolabr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_noFollowee.setVisibility(View.GONE);

        GetFollowee.get(userID);
        getFolloweeReceiver= new GetFolloweeReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("GetFolloweeString").equals("success")){
                    list = (List<ForFollow>) intent.getSerializableExtra("GetFolloweeData");
                    followAdapter = new FollowAdapter(FolloweeActivity.this,R.layout.listview_for_follow,list);
                    listView.setAdapter(followAdapter);
                }else if(intent.getStringExtra("GetFolloweeString").equals("noFollowee")){
                    listView.setVisibility(View.GONE);
                    tv_noFollowee.setVisibility(View.VISIBLE);
                }
            }
        };
        registerReceiver(getFolloweeReceiver,new IntentFilter(GetFolloweeReceiver.ACTION));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForFollow forFollow = (ForFollow) listView.getAdapter().getItem(position);
                Intent intent = new Intent(FolloweeActivity.this,UserInfoActivity.class);
                intent.putExtra("userID", forFollow.getUserID());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(getFolloweeReceiver);
    }
}
