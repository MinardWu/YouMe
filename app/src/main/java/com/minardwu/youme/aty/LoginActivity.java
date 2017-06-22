package com.minardwu.youme.aty;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.minardwu.youme.R;
import com.minardwu.youme.base.App;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

    private CoordinatorLayout coordinatorLayout;
    private MaterialEditText et_phonenumber;
    private MaterialEditText et_password;
    private Button btn_login;
    private TextView tv_noaccount;
    private TextView tv_forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        App.activityList.add(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        et_phonenumber = (MaterialEditText) findViewById(R.id.et_phonenumber);
        et_password = (MaterialEditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_noaccount = (TextView) findViewById(R.id.tv_noaccount);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);

        btn_login.setEnabled(false);
        et_phonenumber.addTextChangedListener(this);
        et_password.addTextChangedListener(this);


        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Object e = msg.obj;
                Log.v("LoginInfo",e.toString());
                if (e.toString().equals("LoginSuccess")) {
                    finish();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                } else if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":211,\"error\":\"Could not find user\"}")) {
                    et_phonenumber.setError("该用户不存在");
                    btn_login.setText("登录");
                    btn_login.setClickable(true);
                } else if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":210,\"error\":\"The username and password mismatch.\"}")) {
                    et_password.setError("密码错误");
                    btn_login.setText("登录");
                    btn_login.setClickable(true);
                } else if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":215,\"error\":\"Mobile phone number isn't verified.\"}")) {
                    btn_login.setText("登录");
                    btn_login.setClickable(true);
                    App.phoneNumber = et_phonenumber.getText().toString();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "该用户手机号尚未未验证", Snackbar.LENGTH_LONG)
                            .setAction("去验证", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AVUser.requestMobilePhoneVerifyInBackground(App.phoneNumber,new RequestMobileCodeCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if(e==null){

                                            }else {
                                                Log.v("requireCode",e.toString());
                                            }
                                        }
                                    });
                                    startActivity(new Intent(LoginActivity.this, VerifyPhonenumberActivity.class));
                                }
                            });
                    App.setSnackBarTextColor(snackbar, Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
                    snackbar.show();
                    btn_login.setEnabled(true);
                } else {
                    btn_login.setText("登录");
                    btn_login.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "网络出问题了╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setText("登录中. . .");
                btn_login.setEnabled(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = handler.obtainMessage();
                        try {
                            AVUser.loginByMobilePhoneNumber(et_phonenumber.getText().toString(), et_password.getText().toString());//本号码随机生成如有雷同纯属巧合
                            message.obj = "LoginSuccess";
                            handler.sendMessage(message);
                        } catch (AVException e) {
                            if (e == null) {

                            } else {
                                message.obj = e;
                                handler.sendMessage(message);
                            }
                        }


                    }
                }).start();

//                AVUser.logInInBackground(et_username.getText().toString(), et_password.getText().toString(), new LogInCallback() {
//                    public void done(AVUser user, AVException e) {
//                        if (e == null) {
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        } else {
//                            Log.v("LoginFail", e.toString());
//                            btn_login.setText("登录");
//                            btn_login.setClickable(true);
//                            if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":211,\"error\":\"Could not find user\"}")) {
//                                et_phonenumber.setError("该用户不存在");
//                            } else if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":210,\"error\":\"The username and password mismatch.\"}")) {
//                                et_password.setError("密码错误");
//                            } else if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":215,\"error\":\"Mobile phone number isn't verified.\"}")) {
//                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "该用户手机号尚未未验证", Snackbar.LENGTH_LONG)
//                                        .setAction("去验证", new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                startActivity(new Intent(LoginActivity.this, VerifyPhonenumberRActivity.class));
//                                            }
//                                        });
//                                App.setSnackBarTextColor(snackbar, Color.parseColor("#4444da"),Color.parseColor("#4444da"));
//                                snackbar.show();
//                            } else {
//                                Toast.makeText(LoginActivity.this, "网络出问题了╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });

            }
        });


        tv_noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        tv_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordEditPhonenumberActivity.class));
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (et_phonenumber.getText().toString().length() != 0 && et_password.getText().toString().length() != 0) {
            btn_login.setEnabled(true);
        } else {
            btn_login.setEnabled(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //双击退出函数
        }
        return false;
    }

    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
