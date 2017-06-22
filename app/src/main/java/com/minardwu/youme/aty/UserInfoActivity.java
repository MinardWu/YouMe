package com.minardwu.youme.aty;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.youme.R;
import com.minardwu.youme.adapter.Half;
import com.minardwu.youme.base.App;
import com.minardwu.youme.base.User;
import com.minardwu.youme.br.GetHalfReceiver;
import com.minardwu.youme.br.GetUserNPReceiver;
import com.minardwu.youme.br.GetUserNumberReceiver;
import com.minardwu.youme.fragment.OneHalfFragment;
import com.minardwu.youme.fragment.OneWholeFragment;
import com.minardwu.youme.leancloud.CheckFollow;
import com.minardwu.youme.leancloud.GetUserInfo;
import com.minardwu.youme.leancloud.GetWholeInfo;
import com.minardwu.youme.leancloud.UploadPortrait;
import com.minardwu.youme.widget.ProgressDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    public static int CAMERA_REQUEST_CODE = 1;
    public static int GALLERY_REQUEST_CODE = 2;
    public static int IMAGEZOOM_REQUEST_CODE = 3;
    private ImageView iv_toolbar_back;
    private ImageView iv_toolbar_setting;
    private TextView tv_toolbar_username;
    private SimpleDraweeView iv_userportrait;
    private TextView tv_username;
    private TextView tv_lovenumber;
    private TextView tv_followernumber;
    private TextView tv_followeenumber;
    private TextView tv_follower;
    private TextView tv_followee;
    private ImageView iv_whole;
    private ImageView iv_half;
    private Button btn_follow;
    private OneWholeFragment oneWholeFragment;
    private OneHalfFragment oneHalfFragment;
    private MaterialDialog dialog;
    private long currentTimeMillis1 = System.currentTimeMillis();
    private String portraitPathForGallery = Environment.getExternalStorageDirectory() + "/youme/" + currentTimeMillis1 + ".jpg";
    private Uri imageUri = Uri.parse("file://" + portraitPathForGallery);
    private GetUserNPReceiver getUserNPReceiver = null;
    private GetUserNumberReceiver getUserNumberReceiver = null;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        App.activityList.add(this);

        //实例化上部分控件
        iv_toolbar_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        tv_toolbar_username = (TextView) findViewById(R.id.tv_toolbar_usernaem);
        iv_toolbar_setting = (ImageView) findViewById(R.id.iv_toolbar_setting);
        tv_username = (TextView) findViewById(R.id.tv_username);
        iv_userportrait = (SimpleDraweeView) findViewById(R.id.iv_userportrait);
        tv_lovenumber = (TextView) findViewById(R.id.tv_lovenumber);
        tv_followernumber = (TextView) findViewById(R.id.tv_followernumber);
        tv_followeenumber = (TextView) findViewById(R.id.tv_followeenumber);
        tv_follower = (TextView) findViewById(R.id.tv_follower);
        tv_followee = (TextView) findViewById(R.id.tv_followee);
        btn_follow = (Button) findViewById(R.id.btn_follow);
        iv_whole = (ImageView) findViewById(R.id.iv_whole);
        iv_half = (ImageView) findViewById(R.id.iv_half);
        //设置控件状态
        iv_whole.setEnabled(false);
        iv_half.setEnabled(true);
        userID = getIntent().getStringExtra("userID");
        if(userID.equals(AVUser.getCurrentUser().getObjectId())){
            btn_follow.setVisibility(View.GONE);
        }else {
            CheckFollow.check(userID, btn_follow,UserInfoActivity.this);
            iv_toolbar_setting.setVisibility(View.GONE);
            iv_userportrait.setEnabled(false);
        }
        //设置数据
        //设置昵称姓名
        GetUserInfo.getUsernameAndPortraitUrl(userID);
        getUserNPReceiver = new GetUserNPReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<String> list_NP = (List<String>) intent.getSerializableExtra("getUsernameAndPortraitUrl");
                tv_toolbar_username.setText(list_NP.get(0));
                tv_username.setText(list_NP.get(0));
                iv_userportrait.setImageURI(Uri.parse(list_NP.get(1)));
                //保存数据，方便进入settingactivity不用再加载一次数据
                if(userID.equals(AVUser.getCurrentUser().getObjectId())){
                    User.username = list_NP.get(0);
                    User.portraitUrl = list_NP.get(1);
                    User.gender = list_NP.get(2);
                }
            }
        };
        registerReceiver(getUserNPReceiver, new IntentFilter(GetUserNPReceiver.ACTION));
        //设置数字
        GetUserInfo.getUserNumber(userID);
        getUserNumberReceiver = new GetUserNumberReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<String> list_Number = (List<String>) intent.getSerializableExtra("getUserNumber");
                tv_lovenumber.setText(list_Number.get(0));
                tv_followernumber.setText(list_Number.get(1));
                tv_followeenumber.setText(list_Number.get(2));
            }
        };
        registerReceiver(getUserNumberReceiver, new IntentFilter(GetUserNumberReceiver.ACTION));
        //点击事件
        iv_toolbar_back.setOnClickListener(this);
        iv_toolbar_setting.setOnClickListener(this);
        tv_followernumber.setOnClickListener(this);
        tv_followeenumber.setOnClickListener(this);
        tv_follower.setOnClickListener(this);
        tv_followee.setOnClickListener(this);
        iv_whole.setOnClickListener(this);
        iv_half.setOnClickListener(this);
        //下面的照片用两个fragment显示
        oneWholeFragment = new OneWholeFragment(userID);
        oneHalfFragment = new OneHalfFragment(userID);
        getSupportFragmentManager().beginTransaction().add(R.id.framelayout_photowall,oneWholeFragment).commit();
        //更换头像
        iv_userportrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UserInfoActivity.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("拍照");
                arrayAdapter.add("相册");
                ListView listView = new ListView(UserInfoActivity.this);
                listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (8 * scale + 0.5f);
                listView.setPadding(0, dpAsPixels, 0, dpAsPixels - 5);
                listView.setDividerHeight(0);
                listView.setAdapter(arrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        } else if (position == 1) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, GALLERY_REQUEST_CODE);
                        }

                    }
                });

                dialog = new MaterialDialog(UserInfoActivity.this).setTitle("更换头像").setContentView(listView);
                dialog.show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.iv_toolbar_setting:
                startActivity(new Intent(UserInfoActivity.this, SettingActivity.class));
                break;
            case R.id.iv_whole:
                iv_whole.setEnabled(false);
                iv_half.setEnabled(true);
                getSupportFragmentManager().beginTransaction().hide(oneHalfFragment).show(oneWholeFragment).commit();
                break;
            case R.id.iv_half:
                if(oneHalfFragment.isAdded()){
                    iv_whole.setEnabled(true);
                    iv_half.setEnabled(false);
                    getSupportFragmentManager().beginTransaction().hide(oneWholeFragment).show(oneHalfFragment).commit();
                }else {
                    iv_whole.setEnabled(true);
                    iv_half.setEnabled(false);
                    getSupportFragmentManager().beginTransaction().hide(oneWholeFragment).add(R.id.framelayout_photowall,oneHalfFragment).commit();
                }
                break;
            case R.id.tv_followernumber:
                Intent intent = new Intent(UserInfoActivity.this,FollowerActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
                break;
            case R.id.tv_follower:
                Intent intent1 = new Intent(UserInfoActivity.this,FollowerActivity.class);
                intent1.putExtra("userID",userID);
                startActivity(intent1);
                break;
            case R.id.tv_followee:
                Intent intent2 = new Intent(UserInfoActivity.this,FolloweeActivity.class);
                intent2.putExtra("userID",userID);
                startActivity(intent2);
                break;
            case R.id.tv_followeenumber:
                Intent intent3 = new Intent(UserInfoActivity.this,FolloweeActivity.class);
                intent3.putExtra("userID",userID);
                startActivity(intent3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            dialog.dismiss();
            if (data == null) {
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras == null) {
                    return;
                } else {
                    Bitmap bitmap = extras.getParcelable("data");
                    saveBitmap(bitmap);
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            dialog.dismiss();
            if (data == null) {
                return;
            } else {
                Uri uri = data.getData();
                startImageZoom(uri);
            }
        } else if (requestCode == IMAGEZOOM_REQUEST_CODE) {
            dialog.dismiss();
            if (imageUri != null) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    UploadPortrait.upload(portraitPathForGallery, UserInfoActivity.this);
                    onResume();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //保存相机拍摄的照片并上传
    private Uri saveBitmap(Bitmap bitmap) {
        File fileDir = new File(Environment.getExternalStorageDirectory() + "/youme");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        long currentTimeMillis1 = System.currentTimeMillis();
        String portraitPathForCamera = Environment.getExternalStorageDirectory() + "/youme/" + currentTimeMillis1 + ".jpg";
        File img = new File(portraitPathForCamera);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            UploadPortrait.upload(portraitPathForCamera, UserInfoActivity.this);
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //打开图片裁剪页面
    private void startImageZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectX", 1);
        //图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        startActivityForResult(intent, IMAGEZOOM_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(getUserNPReceiver);
        unregisterReceiver(getUserNumberReceiver);
    }
}
