package com.minardwu.youme.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minardwu.youme.R;

/**
 * Created by MinardWu on 2016/3/8.
 */
public class ProgressDialog {

    public static Dialog createLoadingDialog(Context context) {

        //一个普通类获得activity控件的实例！！！！！
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_dialog, null);

        //实例化控件
        LinearLayout linearLayout_dialog_view = (LinearLayout) v.findViewById(R.id.ll_progressDialog);
        ImageView imageView = (ImageView) v.findViewById(R.id.iv_progressDialog);

        //设置控件信息
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.progressdialog);
        imageView.startAnimation(animation);

        Dialog loadingDialog = new Dialog(context, R.style.progress_dialog);// 创建自定义样式dialog
//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(linearLayout_dialog_view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

        return loadingDialog;

    }

}
