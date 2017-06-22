package com.minardwu.youme.aty;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.minardwu.youme.R;
import com.minardwu.youme.base.App;

public class ResetPasswordEditPhonenumberActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private ImageView iv_toolbar_back;
    private EditText et_phonenumber;
    private Button btn_surePhonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_edit_phonenumber);
        App.activityList.add(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        iv_toolbar_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        et_phonenumber = (EditText) findViewById(R.id.et_phonenumber);
        btn_surePhonenumber = (Button) findViewById(R.id.btn_surePhonenumber);

        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_surePhonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_surePhonenumber.setEnabled(false);
                btn_surePhonenumber.setText("提交中...");
                AVUser.requestPasswordResetBySmsCodeInBackground(et_phonenumber.getText().toString(), new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            startActivity(new Intent(ResetPasswordEditPhonenumberActivity.this,ResetPasswordEditCodeActivity.class));
                        } else {
                            btn_surePhonenumber.setEnabled(true);
                            btn_surePhonenumber.setText("提交");
                            Log.v("fabv",e.toString());
                            Toast.makeText(ResetPasswordEditPhonenumberActivity.this, "获取验证码失败", Toast.LENGTH_LONG).show();
                            if(e.toString().equals("com.avos.avoscloud.AVException: {\"code\":213,\"error\":\"An user with the specified mobile phone number was not found\"}")){
                                et_phonenumber.setError("该用户不存在");
                            }else if(e.toString().equals("com.avos.avoscloud.AVException: Invalid Phone Number")){
                                et_phonenumber.setError("错误的手机号码");
                            }else if(e.toString().equals("com.avos.avoscloud.AVException: {\"code\":601,\"error\":\"发送短信过快，请稍后重试。\"}")){
                                et_phonenumber.setError("发送短信过快，请稍后重试");
                            }else if(e.toString().equals("com.avos.avoscloud.AVException: {\"code\":601,\"error\":\"发送验证类短信已经超过一天五条的限制。\"}")){
                                et_phonenumber.setError("验证短信已超过五条的限制");
                            }else if(e.toString().equals("com.avos.avoscloud.AVException: {\"code\":215,\"error\":\"Mobile phone number isn't verified.\"}")){
                                et_phonenumber.setError("该手机还未验证");
                                App.phoneNumber = et_phonenumber.getText().toString();
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "该用户手机号尚未未验证", Snackbar.LENGTH_LONG)
                                        .setAction("去验证", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(ResetPasswordEditPhonenumberActivity.this, VerifyPhonenumberActivity.class));
                                            }
                                        });
                                App.setSnackBarTextColor(snackbar, Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
                                snackbar.show();
                            }
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_surePhonenumber.setEnabled(true);
        btn_surePhonenumber.setText("提交");
    }
}
