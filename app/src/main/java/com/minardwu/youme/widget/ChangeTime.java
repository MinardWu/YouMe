package com.minardwu.youme.widget;

import android.text.format.DateUtils;

import com.minardwu.youme.base.App;

import java.util.Date;

/**
 * Created by MinardWu on 2016/3/17.
 */
public class ChangeTime {

    public static String change(Date date) {
        String time = null;
        int flags = 0;
        long dateTime = date.getTime();
        long delta = new Date().getTime() - date.getTime();

        long ONE_MINUTE = 60000L;
        long ONE_HOUR = 3600000L;
        long ONE_DAY = 86400000L;
        long ONE_WEEK = 604800000L;

        if (DateUtils.isToday(dateTime)) {
//            flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR;
//            time = (String) DateUtils.formatDateTime(App.getContext(), dateTime, flags);
            if (delta < 1L * ONE_MINUTE) {
                long seconds = delta / 1000;
                time = (seconds <= 0 ? 1 : seconds) + "秒前";
            } else if (delta < 45L * ONE_MINUTE) {
                long minutes = delta / 60000;
                time = (minutes <= 0 ? 1 : minutes) + "分钟前";
            } else if (delta < 24L * ONE_HOUR) {
                long hours = delta / 3600000;
                time = (hours <= 0 ? 1 : hours) + "小时前";
            }
        } else {
            flags = DateUtils.FORMAT_SHOW_DATE;
            time = (String) DateUtils.formatDateTime(App.getContext(), dateTime, flags);
//            if (delta < 48L * ONE_HOUR) {
//                time = "昨天";
//            } else {
//                time = (date.getMonth() + 1) + "-" + date.getDay() + " " + date.getHours() + ":" + date.getMinutes();
//            }
        }
        return time;
    }
}
