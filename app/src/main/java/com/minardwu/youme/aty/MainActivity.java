package com.minardwu.youme.aty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.minardwu.youme.R;
import com.minardwu.youme.base.App;
import com.minardwu.youme.fragment.HalfFragment;
import com.minardwu.youme.fragment.WholeFragment;
import com.minardwu.youme.leancloud.GetNews;
import com.minardwu.youme.leancloud.SaveInstallationid;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_toolbar_user;
    private ImageView iv_toolbar_news;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private WholeFragment wholeFragment;
    private HalfFragment halfFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.activityList.add(this);
        AVAnalytics.trackAppOpened(getIntent());
        SaveInstallationid.save();

        wholeFragment = new WholeFragment();
        halfFragment = new HalfFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, wholeFragment).commit();

        iv_toolbar_user = (ImageView) findViewById(R.id.iv_toolbar_user);
        iv_toolbar_news = (ImageView) findViewById(R.id.iv_toolbar_news);
        radioButton1 = (RadioButton) findViewById(R.id.RadioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.RadioButton2);

        radioButton1.setChecked(true);
        radioButton2.setChecked(false);

        radioButton1.setOnClickListener(this);
        radioButton2.setOnClickListener(this);
        iv_toolbar_user.setOnClickListener(this);
        iv_toolbar_news.setOnClickListener(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    private static Boolean readyForExit = false;

    private void exitBy2Click() {
        Timer timer = null;
        if (readyForExit == false) {
            readyForExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    readyForExit = false;
                }
            }, 2000); //如果2秒钟内没有按下返回键，readyForExit右变为false则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toolbar_user:
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                intent.putExtra("userID", AVUser.getCurrentUser().getObjectId());
                startActivity(intent);
                break;
            case R.id.iv_toolbar_news:
                startActivity(new Intent(MainActivity.this, NewsActivity.class));
                break;
            case R.id.RadioButton1:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left).hide(halfFragment).show(wholeFragment).commit();
                break;
            case R.id.RadioButton2:
                if (halfFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).hide(wholeFragment).show(halfFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().hide(wholeFragment).add(R.id.fragment, halfFragment).commit();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
