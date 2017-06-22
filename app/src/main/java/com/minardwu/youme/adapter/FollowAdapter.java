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

import java.util.List;

/**
 * Created by MinardWu on 2016/3/14.
 */
public class FollowAdapter extends ArrayAdapter<ForFollow> {

    private int resourceID;

    public FollowAdapter(Context context, int resource, List<ForFollow> objects) {
        super(context, resource, objects);
        this.resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ForFollow forFollow = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID,null);
        SimpleDraweeView iv_followerPortrait = (SimpleDraweeView) view.findViewById(R.id.iv_forfollowPortrait);
        TextView tv_followerName = (TextView) view.findViewById(R.id.tv_forfollowName);
        iv_followerPortrait.setImageURI(Uri.parse(forFollow.getPortraitUrl()));
        tv_followerName.setText(forFollow.getUsername());
        return view;
    }
}
