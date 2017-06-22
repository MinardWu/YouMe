package com.minardwu.youme.leancloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.minardwu.youme.aty.LoginActivity;
import com.minardwu.youme.aty.SettingActivity;
import com.minardwu.youme.base.User;

/**
 * Created by MinardWu on 2016/3/5.
 */
public class UpdatePassword {

    public static void update(final Activity context,final String oldPassword,final String newPassword){


        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String e = (String) msg.obj;
                if(e.toString().equals("com.avos.avoscloud.AVException: {\"code\":210,\"error\":\"The username and password mismatch.\"}")){
                    Toast.makeText(context,"旧密码错误",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "重置成功", Toast.LENGTH_SHORT).show();
                }

            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVUser userA = null;//请确保用户当前的有效登录状态
                try {
                    userA = AVUser.logIn(User.username,oldPassword);
                    userA.updatePasswordInBackground(oldPassword,newPassword, new UpdatePasswordCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e==null){
                                Toast.makeText(context, "重置成功,请重新登录", Toast.LENGTH_SHORT).show();
                                AVUser.logOut();   //清除缓存用户对象
                                AVUser currentUser = AVUser.getCurrentUser(); // 现在的currentUser是null了
                                context.finish();
                                context.startActivity(new Intent(context, LoginActivity.class));
                            }else {
                                Log.v("resetPassword1", e.toString());
                            }
                        }
                    });
                } catch (AVException e) {
                    Message message = handler.obtainMessage();
                    message.obj = e.toString();
                    handler.sendMessage(message);
                    Log.v("resetPassword2", e.toString());
                }

            }
        }).start();


    }
}
