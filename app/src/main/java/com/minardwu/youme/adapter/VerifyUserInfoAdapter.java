package com.minardwu.youme.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.youme.R;
import com.minardwu.youme.base.User;

import java.util.List;

/**
 * Created by MinardWu on 2016/3/5.
 */
public class VerifyUserInfoAdapter extends ArrayAdapter<VerifyUserInfo> {

    private int ReasureID;

    public VerifyUserInfoAdapter(Context context, int resource, List<VerifyUserInfo> objects) {
        super(context, resource, objects);
        this.ReasureID = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VerifyUserInfo verifyUserInfo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(ReasureID, null);
        TextView userinfo_title = (TextView) view.findViewById(R.id.userinfo_title);
        TextView userinfo_value = (TextView) view.findViewById(R.id.userinfo_value);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.userinfo_value_portrait);
        if(position == 0){
            userinfo_value.setVisibility(View.GONE);
            simpleDraweeView.setImageURI(Uri.parse(User.portraitUrl));
        }else {
            simpleDraweeView.setVisibility(View.GONE);
        }
        userinfo_title.setText(verifyUserInfo.getTitle());
        userinfo_value.setText(verifyUserInfo.getValue());
        return view;
    }
}
