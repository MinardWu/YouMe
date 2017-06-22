package com.minardwu.youme.aty;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.minardwu.youme.R;
import com.minardwu.youme.adapter.FollowAdapter;
import com.minardwu.youme.adapter.ForFollow;
import com.minardwu.youme.adapter.ForNews;
import com.minardwu.youme.adapter.NewsAdapter;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetFollowerReceiver;
import com.minardwu.youme.br.GetNewsReceiver;
import com.minardwu.youme.leancloud.GetHalfInfo;
import com.minardwu.youme.leancloud.GetNews;

import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private ImageView iv_toolabr_back;
    private List<ForNews> list;
    private ListView listView;
    private NewsAdapter newsAdapter;
    private GetNewsReceiver getNewsReceiver;
    private TextView tv_noNews;
    private Button btn_load;
    private RotateAnimation rotateAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        App.activityList.add(this);

        GetNews.get();

        iv_toolabr_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        listView = (ListView) findViewById(R.id.listview_news);
        tv_noNews = (TextView) findViewById(R.id.tv_noNews);
        btn_load = (Button) findViewById(R.id.btn_forload);


        iv_toolabr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_noNews.setVisibility(View.GONE);
        btn_load.setEnabled(false);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(1000);
        btn_load.startAnimation(rotateAnimation);

        GetNews.get();
        getNewsReceiver = new GetNewsReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("GetNewsString")!=null){
                    if(intent.getStringExtra("GetNewsString").equals("success")){
                        tv_noNews.setVisibility(View.GONE);
                        btn_load.clearAnimation();
                        btn_load.setVisibility(View.GONE);
                        list = (List<ForNews>) intent.getSerializableExtra("GetNewsData");
                        //把自己对自己的赞去掉
                        for (int i=0;i<list.size();i++){
                            if (list.get(i).getUserID().equals(AVUser.getCurrentUser().getObjectId())) {
                                list.remove(i);
                                i--;
                            }
                        }
                        //如果全是自己的赞则消除后list为空
                        if(list.size() == 0){
                            tv_noNews.setVisibility(View.VISIBLE);
                        }
                        newsAdapter = new NewsAdapter(NewsActivity.this,R.layout.listview_news,list);
                        listView.setAdapter(newsAdapter);
                    }else if(intent.getStringExtra("GetNewsString").equals("fail")){
                        tv_noNews.setVisibility(View.GONE);
                        btn_load.clearAnimation();
                        btn_load.setEnabled(true);
                        btn_load.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                btn_load.setEnabled(false);
                                btn_load.startAnimation(rotateAnimation);
                                GetNews.get();
                            }
                        });
                    }else if(intent.getStringExtra("GetNewsString").equals("noNews")){
                        btn_load.clearAnimation();
                        btn_load.setVisibility(View.GONE);
                        tv_noNews.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        registerReceiver(getNewsReceiver,new IntentFilter(GetNewsReceiver.ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(getNewsReceiver);
    }
}
