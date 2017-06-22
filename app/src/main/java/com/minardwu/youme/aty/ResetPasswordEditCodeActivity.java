package com.minardwu.youme.aty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.minardwu.youme.R;
import com.minardwu.youme.base.App;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ResetPasswordEditCodeActivity extends AppCompatActivity implements TextWatcher {

    private ImageView iv_toolbar_back;
    private MaterialEditText et_verifyCode;
    private MaterialEditText et_newPassword;
    private Button btn_resetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_edit_code);
        App.activityList.add(this);

        iv_toolbar_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        et_verifyCode = (MaterialEditText) findViewById(R.id.et_VerifyCode);
        et_newPassword = (MaterialEditText) findViewById(R.id.et_newPassword);
        btn_resetPassword = (Button) findViewById(R.id.btn_resetPassword);

        et_verifyCode.addTextChangedListener(this);
        et_newPassword.addTextChangedListener(this);
        btn_resetPassword.setEnabled(false);

        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_resetPassword.setEnabled(false);
                btn_resetPassword.setText("提交中...");
                AVUser.resetPasswordBySmsCodeInBackground(et_verifyCode.getText().toString(), et_newPassword.getText().toString(), new UpdatePasswordCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            btn_resetPassword.setEnabled(true);
                            btn_resetPassword.setClickable(false);
                            btn_resetPassword.setText("提交成功");
                            Toast.makeText(ResetPasswordEditCodeActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ResetPasswordEditCodeActivity.this,LoginActivity.class));
                        } else {
                            if (e.toString().equals("com.avos.avoscloud.AVException: Invalid Verify Code")) {
                                et_verifyCode.setError("验证码错误");
                            }else if(e.toString().equals("com.avos.avoscloud.AVException: {\"code\":603,\"error\":\"无效的短信验证码\"}")){
                                et_verifyCode.setError("无效的短信验证码");
                            }
                            btn_resetPassword.setEnabled(true);
                            btn_resetPassword.setText("提交");
                            Toast.makeText(ResetPasswordEditCodeActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
        if (et_verifyCode.getText().toString().length() > 0 && et_newPassword.getText().toString().length() > 0) {
            btn_resetPassword.setEnabled(true);
        }
    }
}
