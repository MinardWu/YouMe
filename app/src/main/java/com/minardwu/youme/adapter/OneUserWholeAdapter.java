package com.minardwu.youme.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.youme.R;
import com.minardwu.youme.base.App;

import java.util.List;

/**
 * Created by MinardWu on 2016/3/11.
 */
public class OneUserWholeAdapter extends ArrayAdapter<Whole> {

    private int resourceID;

    public OneUserWholeAdapter(Context context, int resource, List<Whole> objects) {
        super(context, resource, objects);
        this.resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Whole whole = getItem(position);
        View view;
        if(convertView == null){
            view =  LayoutInflater.from(App.getContext()).inflate(resourceID, null);
        }else {
            view = convertView;
        }
        SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.iv_gridview);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.width = App.ScreenWidth/3;
        layoutParams.height = App.ScreenWidth/3;
        imageView.setLayoutParams(layoutParams);
        imageView.setImageURI(Uri.parse(whole.getPohotUrl()));

        return view;
    }
}
