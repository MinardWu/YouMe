package com.minardwu.youme.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.youme.R;
import com.minardwu.youme.aty.UserInfoActivity;
import com.minardwu.youme.base.App;

import java.util.List;

/**
 * Created by MinardWu on 2016/3/15.
 */
public class NewsAdapter extends ArrayAdapter<ForNews> {

    private int resourceID;


    public NewsAdapter(Context context, int resource, List<ForNews> objects) {
        super(context, resource, objects);
        this.resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ForNews forNews = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID, null);
        SimpleDraweeView iv_news_userPortrait = (SimpleDraweeView) view.findViewById(R.id.iv_news_userPortrait);
        TextView tv_news_username = (TextView) view.findViewById(R.id.tv_news_username);
        TextView tv_news_type = (TextView) view.findViewById(R.id.tv_news_type);
        SimpleDraweeView iv_news_photo = (SimpleDraweeView) view.findViewById(R.id.iv_news_photo);
        iv_news_userPortrait.setImageURI(Uri.parse(forNews.getPortraitUrl()));
        tv_news_username.setText(forNews.getUsername());
        if(forNews.getType().equals("lover")){
            tv_news_type.setText("赞了你");
        }else {
            tv_news_type.setText("关注了你");
        }
        if(forNews.getType().equals("lover")){
            iv_news_photo.setImageURI(Uri.parse(forNews.getPhotoUrl()));
        }else {
            iv_news_photo.setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), UserInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userID",forNews.getUserID());
                App.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
