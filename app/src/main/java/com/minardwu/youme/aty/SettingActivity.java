package com.minardwu.youme.aty;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.youme.R;
import com.minardwu.youme.base.App;
import com.minardwu.youme.base.User;
import com.minardwu.youme.br.GetUserNPReceiver;
import com.minardwu.youme.leancloud.GetUserInfo;
import com.minardwu.youme.leancloud.UpdateGender;
import com.minardwu.youme.leancloud.UpdatePassword;
import com.minardwu.youme.leancloud.UpdateUsername;
import com.minardwu.youme.leancloud.UploadPortrait;
import com.minardwu.youme.adapter.VerifyUserInfo;
import com.minardwu.youme.adapter.VerifyUserInfoAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class SettingActivity extends AppCompatActivity {

    public static int CAMERA_REQUEST_CODE = 1;
    public static int GALLERY_REQUEST_CODE = 2;
    public static int IMAGEZOOM_REQUEST_CODE = 3;
    private ListView listView;
    private ArrayAdapter adapter;
    private List<VerifyUserInfo> list;
    private SimpleDraweeView simpleDraweeView;
    private ImageView iv_toolabr_back;
    private Button btn_logout;
    private GetUserNPReceiver getUserNPReceiver;
    private MaterialDialog dialog_portrait;
    private MaterialDialog dialog_gender;
    private long currentTimeMillis1 = System.currentTimeMillis();
    private String portraitPathForGallery = Environment.getExternalStorageDirectory() + "/youme/" + currentTimeMillis1 + ".jpg";
    private Uri imageUri = Uri.parse("file://" + portraitPathForGallery);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        App.activityList.add(this);


        iv_toolabr_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        iv_toolabr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.logOut();   //清除缓存用户对象
                AVUser currentUser = AVUser.getCurrentUser(); // 现在的currentUser是null了
                finish();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            }
        });

        listView = (ListView) findViewById(R.id.listview_settingactivity);
        list = new ArrayList<VerifyUserInfo>();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1);
                    arrayAdapter.add("拍照");
                    arrayAdapter.add("相册");
                    ListView listView = new ListView(SettingActivity.this);
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

                    dialog_portrait = new MaterialDialog(SettingActivity.this).setTitle("更换头像").setContentView(listView);
                    dialog_portrait.show();

                } else if (position == 1) {
                    View view_change = LayoutInflater.from(SettingActivity.this).inflate(R.layout.settingactivity_dialog_username, null);
                    final MaterialEditText et_newUsername = (MaterialEditText) view_change.findViewById(R.id.et_newUsername);
                    final MaterialDialog dialog_username = new MaterialDialog(SettingActivity.this);
                    dialog_username.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (et_newUsername.getText().toString().length() == 0) {
                                et_newUsername.setError("用户名不能为空");
                            } else {
                                dialog_username.dismiss();
                                UpdateUsername.update(SettingActivity.this, et_newUsername.getText().toString());
                            }
                        }
                    });
                    dialog_username.setView(view_change).show();
                } else if (position == 2) {
                    View view_change = LayoutInflater.from(SettingActivity.this).inflate(R.layout.settingactivity_dialog_password, null);
                    final MaterialEditText et_oldPassword = (MaterialEditText) view_change.findViewById(R.id.et_oldPassword);
                    final MaterialEditText et_newPassword = (MaterialEditText) view_change.findViewById(R.id.et_newPassword);
                    final MaterialDialog dialog_username = new MaterialDialog(SettingActivity.this);
                    dialog_username.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (et_oldPassword.getText().toString().length() == 0) {
                                et_oldPassword.setError("不能为空");
                            } else if (et_newPassword.getText().toString().length() == 0) {
                                et_newPassword.setError("不能为空");
                            } else {
                                dialog_username.dismiss();
                                UpdatePassword.update(SettingActivity.this, et_oldPassword.getText().toString(), et_newPassword.getText().toString());
                            }
                        }
                    });
                    dialog_username.setView(view_change).show();

                } else if (position == 3) {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1);
                    arrayAdapter.add("汉子");
                    arrayAdapter.add("妹子");
                    ListView listView = new ListView(SettingActivity.this);
                    listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    float scale = getResources().getDisplayMetrics().density;
                    int dpAsPixels = (int) (8 * scale + 0.5f);
                    listView.setPadding(0, dpAsPixels, 0, dpAsPixels - 5);
                    listView.setDividerHeight(0);
                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog_gender.dismiss();
                            if (position == 0) {
                                UpdateGender.update(SettingActivity.this, "汉子");
                            } else if (position == 1) {
                                UpdateGender.update(SettingActivity.this, "妹子");
                            }
                        }
                    });

                    dialog_gender = new MaterialDialog(SettingActivity.this).setTitle("选一个吧").setContentView(listView);
                    dialog_gender.show();
                }
            }
        });


        //任何个人数据更新就会执行GetUserInfo.getUsernameAndPortraitUrl(),然后就会接受到广播，从而更新UI
        GetUserInfo.getUsernameAndPortraitUrl(AVUser.getCurrentUser().getObjectId());
        getUserNPReceiver = new GetUserNPReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<String> list_NP = (List<String>) intent.getSerializableExtra("getUsernameAndPortraitUrl");
                list.clear();
                list.add(new VerifyUserInfo("更换头像", "", list_NP.get(1)));
                list.add(new VerifyUserInfo("昵称", list_NP.get(0), ""));
                list.add(new VerifyUserInfo("密码", "不给你看", ""));
                list.add(new VerifyUserInfo("性别", list_NP.get(2), ""));
                list.add(new VerifyUserInfo("生日", "1995-12-2", ""));
                list.add(new VerifyUserInfo("所在地", "中国", ""));
                list.add(new VerifyUserInfo("签名", "绝不随波逐流", ""));
                adapter.notifyDataSetChanged();
            }
        };
        registerReceiver(getUserNPReceiver, new IntentFilter(GetUserNPReceiver.ACTION));

        initList();
        adapter = new VerifyUserInfoAdapter(SettingActivity.this, R.layout.listview_settingactivity, list);
        listView.setAdapter(adapter);
    }

    private void initList() {
        list.clear();
        list.add(new VerifyUserInfo("更换头像", "", User.portraitUrl));
        list.add(new VerifyUserInfo("昵称", User.username, ""));
        list.add(new VerifyUserInfo("密码", "不给你看", ""));
        list.add(new VerifyUserInfo("性别", User.gender, ""));
        list.add(new VerifyUserInfo("生日", "1995-12-2", ""));
        list.add(new VerifyUserInfo("所在地", "中国", ""));
        list.add(new VerifyUserInfo("签名", "绝不随波逐流", ""));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            dialog_portrait.dismiss();
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
            dialog_portrait.dismiss();
            if (data == null) {
                return;
            } else {
                Uri uri = data.getData();
                startImageZoom(uri);
            }
        } else if (requestCode == IMAGEZOOM_REQUEST_CODE) {
            dialog_portrait.dismiss();
            if (imageUri != null) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    UploadPortrait.upload(portraitPathForGallery, SettingActivity.this);
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
            UploadPortrait.upload(portraitPathForCamera, SettingActivity.this);
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
        System.gc();
    }
}
