package com.pcdgroup.hp.pcd_group.AdminLogin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pcdgroup.hp.pcd_group.Product.CustomVolleyRequest;
import com.pcdgroup.hp.pcd_group.Product.Entity;
import com.pcdgroup.hp.pcd_group.R;

import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name UserAdminAdepter
 * @description Admin Adepter to load image and get email id.
 */

public class UserAdminAdepter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<UserDataGet> userDataGets;
    private ImageLoader imageLoader;

    public UserAdminAdepter(Activity activity, List<UserDataGet> userDataGets) {

        this.activity = activity;
        this.userDataGets = userDataGets;
        imageLoader = CustomVolleyRequest.getInstance(activity.getApplicationContext())
                .getImageLoader();
    }

    @Override
    public int getCount() {
        return userDataGets.size();
    }

    @Override
    public Object getItem(int location) {
        return userDataGets.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_user, null);


        //Assign Id'S
        TextView email = (TextView) convertView.findViewById(R.id.title);

        // getting movie data for the row
        UserDataGet m = userDataGets.get(position);

        // title
        email.setText(m.getEmail());

        return convertView;
    }
}