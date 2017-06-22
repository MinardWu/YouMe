package com.minardwu.youme.aty;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.minardwu.youme.R;
import com.minardwu.youme.br.UploadWholeReceiver;
import com.minardwu.youme.leancloud.UploadWhole;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PublishWholeActivity extends AppCompatActivity {

    private String author1ID;

    private ImageView iv_show;
    private Button btn_publish;
    private Button btn_load;
    private UploadWholeReceiver uploadWholeReceiver;
    private RotateAnimation rotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_whole);

        iv_show = (ImageView) findViewById(R.id.iv_show);
        btn_publish = (Button) findViewById(R.id.btn_publish);
        btn_load = (Button) findViewById(R.id.btn_forload);

        byte[] bytes = getIntent().getByteArrayExtra("bytes");
        author1ID = getIntent().getStringExtra("author1ID");

        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);//data是字节数据，将其解析成位图


        btn_publish.setEnabled(true);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(100);
        btn_load.startAnimation(rotateAnimation);
        iv_show.setImageBitmap(bitmap);
        btn_load.setVisibility(View.GONE);
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJpeg(bitmap);
                btn_publish.setEnabled(false);
                btn_load.setEnabled(false);
                btn_load.setVisibility(View.VISIBLE);
                btn_load.startAnimation(rotateAnimation);
            }
        });

        uploadWholeReceiver = new UploadWholeReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("UploadWholeString")!=null){
                    if(intent.getStringExtra("UploadWholeString").equals("success")){
                        btn_publish.setEnabled(false);
                        btn_publish.setText("已发送");
                        btn_load.clearAnimation();
                        btn_load.setVisibility(View.GONE);
                        Toast.makeText(PublishWholeActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(PublishWholeActivity.this,MainActivity.class));
                        finish();
                    }else if(intent.getStringExtra("UploadWholeString").equals("fail")){
                        btn_publish.setEnabled(true);
                        btn_load.clearAnimation();
                        btn_load.setVisibility(View.GONE);
                        Toast.makeText(PublishWholeActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        registerReceiver(uploadWholeReceiver,new IntentFilter(UploadWholeReceiver.ACTION));

    }

    public void saveJpeg(Bitmap bm) {

        //创建文件夹
        File myDir = new File(Environment.getExternalStorageDirectory() + "/YouMe");
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        //创建文件
        long currentTimeMillis = System.currentTimeMillis();
        String filepath = myDir.getAbsolutePath() + "/" + currentTimeMillis + ".jpeg";
        File myPhoto = new File(filepath);
        //往文件写入数据
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(myPhoto);
            bm.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            UploadWhole.upload(filepath, author1ID);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
