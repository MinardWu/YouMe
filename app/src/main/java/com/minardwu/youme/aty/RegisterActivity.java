package com.minardwu.youme.aty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.youme.R;
import com.minardwu.youme.base.App;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {

    private MaterialEditText et_username;
    private MaterialEditText et_password;
    private MaterialEditText et_phonenumber;
    private Button btn_register;
    private String objietId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        App.activityList.add(this);

        et_username = (MaterialEditText) findViewById(R.id.et_username);
        et_password = (MaterialEditText) findViewById(R.id.et_password);
        et_phonenumber = (MaterialEditText) findViewById(R.id.et_phonenumber);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_register.setEnabled(false);
        et_username.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_phonenumber.addTextChangedListener(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_password.getText().toString().length() > 18) {
                    et_password.setError("密码不能大于18位");
                } else if (et_password.getText().toString().length() < 6) {
                    et_password.setError("密码不能小于6位");
                } else {
                    btn_register.setText("注册中. . .");
                    btn_register.setEnabled(false);
                    AVUser user = new AVUser();
                    user.setUsername(et_username.getText().toString());
                    user.setPassword(et_password.getText().toString());
                    user.setMobilePhoneNumber(et_phonenumber.getText().toString());
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                btn_register.setText("注册");
                                btn_register.setEnabled(true);
                                AVObject post = new AVObject("Number");
                                post.put("userID",AVUser.getCurrentUser().getObjectId());
                                post.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if(e==null){
//                                            Toast.makeText(RegisterActivity.this,"success",Toast.LENGTH_SHORT).show();
                                        }else {
//                                            Toast.makeText(RegisterActivity.this,"fail",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                AVUser.logOut();
                                AVUser currentUser = AVUser.getCurrentUser();
                                App.phoneNumber = et_phonenumber.getText().toString();
                                finish();
                                startActivity(new Intent(RegisterActivity.this, VerifyPhonenumberActivity.class));
                            } else {
                                btn_register.setText("注册");
                                btn_register.setEnabled(true);
                                Log.v("RegisterFail", e.toString());
                                if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":202,\"error\":\"Username has already been taken\"}")) {
                                    et_username.setError("该用户名已存在");
                                } else if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":214,\"error\":\"Mobile phone number has already been taken\"}")) {
                                    et_phonenumber.setError("该手机已被注册");
                                } else if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":601,\"error\":\"发送验证类短信已经超过一天五条的限制。\"}")) {
                                    Toast.makeText(RegisterActivity.this, "发送验证短信已经超过一天五条的限制", Toast.LENGTH_SHORT).show();
                                } else if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":127,\"error\":\"无效的手机号码。\"}")) {
                                    et_phonenumber.setError("无效的手机号码");
                                } else {
                                    Toast.makeText(RegisterActivity.this, "网络出问题了╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
                }
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
        if (et_username.getText().toString().length() != 0
                && et_password.getText().toString().length() != 0
                && et_phonenumber.getText().toString().length() != 0) {
            btn_register.setEnabled(true);
        } else {
            btn_register.setEnabled(false);
        }
    }
}
