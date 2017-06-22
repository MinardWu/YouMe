package com.minardwu.youme.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.minardwu.youme.R;
import com.minardwu.youme.aty.NewsActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by MinardWu on 2016/3/1.
 */
public class App extends Application {

    public static Context context;
    public static int ScreenWidth;
    public static int ScreenHeight;
    public static String phoneNumber;
    public static List<Activity> activityList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fresco.initialize(context);
        AVOSCloud.initialize(this, "b2CY6yl4QJevmfGNsyyl9cil-gzGzoHsz", "NJTFogXqHLPSCRnQUgsIyIsz");
        // 启用崩溃错误统计
        AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(true);
        // 设置默认打开的 Activity
        PushService.setDefaultPushCallback(this,NewsActivity.class);
    }

    public static Context getContext() {
        return context;
    }


    public static void setSnackBarTextColor(Snackbar snackbar, int color1, int color2) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color1);
        ((TextView) view.findViewById(R.id.snackbar_action)).setTextColor(color2);
    }

    public static void finishAllActivity(){
        for (Activity activity : activityList) {
            activity.finish();
        }
        if (activityList.size() == 0)
            activityList.clear();
    }

}
