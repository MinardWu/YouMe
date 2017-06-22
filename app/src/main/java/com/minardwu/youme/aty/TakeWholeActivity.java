package com.minardwu.youme.aty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class TakeWholeActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private Camera camera;
    private RelativeLayout relativeLayout;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageView iv_top;
    private ImageView iv_bottom;
    private ImageView iv_left;
    private ImageView iv_right;
    private Button btn_take;
    private Button btn_delete;
    private Button btn_save;

    private Bitmap bitmap;
    private Bitmap wholebitmap;
    private Bitmap halfbitmap;
    private String filepath;
    private String author1ID;
    private boolean isPreview;
    private int degree = 0;
    private String position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addanotherphoto);


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


        byte[] bytes = getIntent().getByteArrayExtra("bytes");
        position = getIntent().getStringExtra("position");
        wholebitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        author1ID = getIntent().getStringExtra("author1ID");


        relativeLayout = (RelativeLayout) findViewById(R.id.rl);
        btn_take = (Button) findViewById(R.id.btn_take);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_save = (Button) findViewById(R.id.btn_save);
        surfaceView = (SurfaceView) findViewById(R.id.surface);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_top = (ImageView) findViewById(R.id.iv_top);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_bottom = (ImageView) findViewById(R.id.iv_bottom);


        //照片携带信息即为它在那一边，所以如果position为top则加载iv_top且可见
        if (position.equals("top")) {
            halfbitmap = Bitmap.createBitmap(wholebitmap, 0, 0, wholebitmap.getWidth(), wholebitmap.getHeight() / 2);
            iv_top.setImageBitmap(halfbitmap);
            iv_left.setVisibility(View.GONE);
            iv_right.setVisibility(View.GONE);
            iv_bottom.setVisibility(View.GONE);
            iv_top.setVisibility(View.VISIBLE);
        } else if (position.equals("bottom")) {
            halfbitmap = Bitmap.createBitmap(wholebitmap, 0, wholebitmap.getHeight() / 2, wholebitmap.getWidth(), wholebitmap.getHeight() / 2);
            iv_bottom.setImageBitmap(halfbitmap);
            iv_left.setVisibility(View.GONE);
            iv_right.setVisibility(View.GONE);
            iv_top.setVisibility(View.GONE);
            iv_bottom.setVisibility(View.VISIBLE);
        } else if (position.equals("left")) {
            halfbitmap = Bitmap.createBitmap(wholebitmap, 0, 0, wholebitmap.getWidth() / 2, wholebitmap.getHeight());
            iv_left.setImageBitmap(halfbitmap);
            iv_top.setVisibility(View.GONE);
            iv_bottom.setVisibility(View.GONE);
            iv_right.setVisibility(View.GONE);
            iv_left.setVisibility(View.VISIBLE);
        } else if (position.equals("right")) {
            halfbitmap = Bitmap.createBitmap(wholebitmap, wholebitmap.getWidth() / 2, 0, wholebitmap.getWidth() / 2, wholebitmap.getHeight());
            iv_right.setImageBitmap(halfbitmap);
            iv_top.setVisibility(View.GONE);
            iv_bottom.setVisibility(View.GONE);
            iv_left.setVisibility(View.GONE);
            iv_right.setVisibility(View.VISIBLE);
        }


        btn_delete.setVisibility(View.GONE);
        btn_save.setVisibility(View.GONE);
        btn_take.setOnClickListener(this);

        //定义surfaceView及其参数，用来预览图片
        RelativeLayout.LayoutParams layoutParamsleft = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
        layoutParamsleft.height = App.ScreenWidth;
        layoutParamsleft.width = App.ScreenWidth;
        surfaceView.setLayoutParams(layoutParamsleft);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);


        LinearLayout.LayoutParams rl_LaLayoutParams = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
        rl_LaLayoutParams.height = App.ScreenWidth;
        rl_LaLayoutParams.width = App.ScreenWidth;
        relativeLayout.setLayoutParams(rl_LaLayoutParams);


        RelativeLayout.LayoutParams iv_top_LayoutParams = (RelativeLayout.LayoutParams) iv_top.getLayoutParams();
        iv_top_LayoutParams.height = App.ScreenWidth / 2;
        iv_top_LayoutParams.width = App.ScreenWidth;
        iv_top.setLayoutParams(iv_top_LayoutParams);

        RelativeLayout.LayoutParams iv_left_LayoutParams = (RelativeLayout.LayoutParams) iv_left.getLayoutParams();
        iv_left_LayoutParams.height = App.ScreenWidth;
        iv_left_LayoutParams.width = App.ScreenWidth / 2;
        iv_left.setLayoutParams(iv_left_LayoutParams);

        RelativeLayout.LayoutParams iv_bottom_LayoutParams = (RelativeLayout.LayoutParams) iv_bottom.getLayoutParams();
        iv_bottom_LayoutParams.height = App.ScreenWidth / 2;
        iv_bottom_LayoutParams.width = App.ScreenWidth;
        iv_bottom.setLayoutParams(iv_bottom_LayoutParams);

        RelativeLayout.LayoutParams iv_right_LayoutParams = (RelativeLayout.LayoutParams) iv_right.getLayoutParams();
        iv_right_LayoutParams.height = App.ScreenWidth;
        iv_right_LayoutParams.width = App.ScreenWidth / 2;
        iv_right.setLayoutParams(iv_right_LayoutParams);


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        //如果有continuous-video这个对焦模式则采用
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
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
        switch (v.getId()) {
            case R.id.btn_take:
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(final byte[] bytes, final Camera camera) {
                        if (bytes != null) {
                            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);//data是字节数据，将其解析成位图
                            camera.stopPreview();
                            isPreview = false;
                            btn_delete.setVisibility(View.VISIBLE);
                            btn_save.setVisibility(View.VISIBLE);
                            btn_save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (bitmap != null) {
                                        btn_delete.setVisibility(View.GONE);
                                        btn_save.setVisibility(View.GONE);
                                        Bitmap finalBitmap = toConformBitmap(bitmap, halfbitmap);
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                        byte[] bytes1 = baos.toByteArray();
                                        finish();
                                        Intent intent = new Intent(TakeWholeActivity.this, PublishWholeActivity.class);
                                        intent.putExtra("bytes", bytes1);
                                        intent.putExtra("author1ID",author1ID);
                                        startActivity(intent);
                                    }
                                }
                            });
                            btn_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    camera.startPreview();
                                    isPreview = true;
                                    btn_delete.setVisibility(View.GONE);
                                    btn_save.setVisibility(View.GONE);
                                }
                            });

                        }

                    }
                });
                break;
        }
    }


    private Bitmap toConformBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();


        Matrix matrix = new Matrix();
        Rect mSrcRect = null;
        Rect mDestRect = null;
        //这里的position即为图片需要绘制的地方
        if (position.equals("left")) {
            matrix.setScale((float) bgWidth / 2 / fgWidth, (float) bgHeight / fgHeight);
            mSrcRect = new Rect(0, 0, bgWidth / 2, bgHeight);//设置foreground的大小
            mDestRect = new Rect(0, 0, bgWidth / 2, bgHeight);//设置foreground的位置,即左上角坐标为前两个（0,0），右下角为后两个（bgWidth/2, bgHeight）
        } else if (position.equals("right")) {
            matrix.setScale((float) bgWidth / 2 / fgWidth, (float) bgHeight / fgHeight);
            mSrcRect = new Rect(0, 0, bgWidth / 2, bgHeight);//设置foreground的大小
            mDestRect = new Rect(bgWidth / 2, 0, bgWidth, bgHeight);//设置foreground的位置,即左上角坐标为前两个（0,0），右下角为后两个（bgWidth/2, bgHeight）
        } else if (position.equals("top")) {
            matrix.setScale((float) bgWidth / fgWidth, (float) bgHeight / 2 / fgHeight);
            mSrcRect = new Rect(0, 0, bgWidth, bgHeight / 2);
            mDestRect = new Rect(0, 0, bgWidth, bgHeight / 2);
        } else if (position.equals("bottom")) {
            matrix.setScale((float) bgWidth / fgWidth, (float) bgHeight / 2 / fgHeight);
            mSrcRect = new Rect(0, 0, bgWidth, bgHeight / 2);
            mDestRect = new Rect(0, bgHeight / 2, bgWidth, bgHeight);
        }
        Bitmap foregroundNO2 = Bitmap.createBitmap(foreground, 0, 0, fgWidth, fgHeight, matrix, true);

        Bitmap newBitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(background, 0, 0, null);//在0，0坐标开始画入bg
        cv.drawBitmap(foregroundNO2, mSrcRect, mDestRect, null);
        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        cv.restore();//存储
        return newBitmap;



    }

}
