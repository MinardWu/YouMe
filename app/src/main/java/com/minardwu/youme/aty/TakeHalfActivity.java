package com.minardwu.youme.aty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.minardwu.youme.R;
import com.minardwu.youme.base.App;
import com.minardwu.youme.leancloud.UploadHalf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class TakeHalfActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button btn_take;
    private Button btn_top;
    private Button btn_bottom;
    private Button btn_right;
    private Button btn_left;
    private ImageView iv_top;
    private ImageView iv_bottom;
    private ImageView iv_left;
    private ImageView iv_right;
    private RelativeLayout relativeLayout;


    private boolean isPreview;
    private Bitmap bitmap;
    private int degree = 0;
    private String filepath;
    private String position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_photo);

        //旋转摄像头
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }

        relativeLayout = (RelativeLayout) findViewById(R.id.rl);
        btn_take = (Button) findViewById(R.id.btn_take);
        btn_top = (Button) findViewById(R.id.btn_top);
        btn_bottom = (Button) findViewById(R.id.btn_bottom);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        surfaceView = (SurfaceView) findViewById(R.id.surface);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_top = (ImageView) findViewById(R.id.iv_top);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_bottom = (ImageView) findViewById(R.id.iv_bottom);


        btn_take.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_top.setOnClickListener(this);
        btn_bottom.setOnClickListener(this);
        btn_left.setOnClickListener(this);

        //定义surfaceView及其参数，用来预览图片
        RelativeLayout.LayoutParams layoutParamsSur = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
        layoutParamsSur.width = App.ScreenWidth;
        layoutParamsSur.height = App.ScreenWidth;
        surfaceView.setLayoutParams(layoutParamsSur);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);


        LinearLayout.LayoutParams rl_LaLayoutParams = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
        rl_LaLayoutParams.height = App.ScreenWidth;
        rl_LaLayoutParams.width = App.ScreenWidth;
        relativeLayout.setLayoutParams(rl_LaLayoutParams);


        RelativeLayout.LayoutParams iv_top_LayoutParams = (RelativeLayout.LayoutParams) iv_top.getLayoutParams();
        iv_top_LayoutParams.height = App.ScreenWidth/2;
        iv_top_LayoutParams.width = App.ScreenWidth;
        iv_top.setLayoutParams(iv_top_LayoutParams);

        RelativeLayout.LayoutParams iv_left_LayoutParams = (RelativeLayout.LayoutParams) iv_left.getLayoutParams();
        iv_left_LayoutParams.height = App.ScreenWidth;
        iv_left_LayoutParams.width = App.ScreenWidth/2;
        iv_left.setLayoutParams(iv_left_LayoutParams);

        RelativeLayout.LayoutParams iv_bottom_LayoutParams = (RelativeLayout.LayoutParams) iv_bottom.getLayoutParams();
        iv_bottom_LayoutParams.height = App.ScreenWidth/2;
        iv_bottom_LayoutParams.width = App.ScreenWidth;
        iv_bottom.setLayoutParams(iv_bottom_LayoutParams);

        RelativeLayout.LayoutParams iv_right_LayoutParams = (RelativeLayout.LayoutParams) iv_right.getLayoutParams();
        iv_right_LayoutParams.height = App.ScreenWidth;
        iv_right_LayoutParams.width = App.ScreenWidth/2;
        iv_right.setLayoutParams(iv_right_LayoutParams);

        position = "left";
        iv_left.setVisibility(View.GONE);
        iv_top.setVisibility(View.GONE);
        iv_bottom.setVisibility(View.GONE);
        iv_right.setVisibility(View.VISIBLE);


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        //如果有continuous-video这个对焦模式则采用
        List<String> focusModes = parameters.getSupportedFocusModes();
        if(focusModes.contains("continuous-video")){
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        parameters.setPreviewSize(App.ScreenWidth, App.ScreenWidth);
        parameters.setPictureSize(App.ScreenWidth, App.ScreenWidth);
        parameters.setRotation(90);
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setJpegQuality(100);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(degree);//旋转度数
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();

    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            isPreview = false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_take:
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        if (bytes != null) {
                            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);//data是字节数据，将其解析成位图
                            camera.stopPreview();
                            isPreview = false;
                            if (bitmap != null) {
//                                saveJpeg(bitmap);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] bytes1 = baos.toByteArray();
                                finish();
                                Intent intent = new Intent(TakeHalfActivity.this, PublishHalfActivity.class);
                                intent.putExtra("bytes", bytes1);
                                intent.putExtra("position",position);
                                startActivity(intent);
                            }
                        }
                        camera.startPreview();
                        isPreview = true;
                    }
                });
                break;
            case R.id.btn_top:
                position = "top";
                iv_left.setVisibility(View.GONE);
                iv_right.setVisibility(View.GONE);
                iv_top.setVisibility(View.GONE);
                iv_bottom.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_bottom:
                position = "bottom";
                iv_left.setVisibility(View.GONE);
                iv_right.setVisibility(View.GONE);
                iv_bottom.setVisibility(View.GONE);
                iv_top.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_right:
                position = "right";
                iv_right.setVisibility(View.GONE);
                iv_top.setVisibility(View.GONE);
                iv_bottom.setVisibility(View.GONE);
                iv_left.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_left:
                position = "left";
                iv_left.setVisibility(View.GONE);
                iv_top.setVisibility(View.GONE);
                iv_bottom.setVisibility(View.GONE);
                iv_right.setVisibility(View.VISIBLE);
                break;
        }
    }
}
