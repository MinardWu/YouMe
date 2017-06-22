package com.minardwu.youme.aty;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.minardwu.youme.R;
import com.minardwu.youme.base.App;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Timer;
import java.util.TimerTask;

public class VerifyPhonenumberActivity extends AppCompatActivity implements TextWatcher {

    private ImageView iv_toolbar_back;
    private MaterialEditText et_verifyCode;
    private Button btn_sendVerifyCode;
    private Button btn_requireVerifyCode;
    private int time = 30;
    private Timer timer;
    private TimerTask timerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyphonenumber);
        App.activityList.add(this);

        iv_toolbar_back= (ImageView) findViewById(R.id.iv_toolbar_back);
        et_verifyCode = (MaterialEditText) findViewById(R.id.et_VerifyCode);
        btn_sendVerifyCode = (Button) findViewById(R.id.btn_sendVerifyCode);
        btn_requireVerifyCode = (Button) findViewById(R.id.btn_requireVerifyCode);


        et_verifyCode.addTextChangedListener(this);
        btn_sendVerifyCode.setEnabled(false);
        btn_requireVerifyCode.setEnabled(false);

        startTime();

        btn_sendVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_sendVerifyCode.setEnabled(false);
                btn_sendVerifyCode.setText("提交中...");
                AVUser.verifyMobilePhoneInBackground(et_verifyCode.getText().toString(), new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(VerifyPhonenumberActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(VerifyPhonenumberActivity.this, LoginActivity.class));
                        } else {
                            btn_sendVerifyCode.setEnabled(true);
                            btn_sendVerifyCode.setText("提交");
                            if (e.toString().equals("com.avos.avoscloud.AVException: {\"code\":603,\"error\":\"无效的短信验证码\"}")) {
                                Toast.makeText(VerifyPhonenumberActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                            }
                            Log.v("VerifyFail", e.toString());
                        }
                    }
                });
            }
        });

        btn_requireVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 30;
                startTime();
                AVUser.requestMobilePhoneVerifyInBackground(App.phoneNumber,new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){

                        }else {
                            Log.v("requireCode",e.toString());
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
        if(et_verifyCode.getText().toString().length()>0){
            btn_sendVerifyCode.setEnabled(true);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1 == 0){
                stopTime();
                btn_requireVerifyCode.setEnabled(true);
                btn_requireVerifyCode.setText("重新获取");
            }else {
                btn_requireVerifyCode.setEnabled(false);
                btn_requireVerifyCode.setText("已发送："+msg.arg1+"秒");
                startTime();
            }
        }
    };

    private void startTime(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                time--;
                Message message = handler.obtainMessage();
                message.arg1 = time;
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask,1000);
    }

    private void stopTime(){
        timer.cancel();
    }
}
