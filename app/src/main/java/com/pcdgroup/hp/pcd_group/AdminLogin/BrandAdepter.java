package com.pcdgroup.hp.pcd_group.AdminLogin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.pcdgroup.hp.pcd_group.Product.CustomVolleyRequest;
import com.pcdgroup.hp.pcd_group.R;

import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class BrandAdepter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Category> categories;
    private ImageLoader imageLoader;

    public BrandAdepter(Activity activity, List<Category> categories) {
        this.activity = activity;
        this.categories = categories;
        imageLoader = CustomVolleyRequest.getInstance(activity.getApplicationContext())
                .getImageLoader();
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int location) {
        return categories.get(location);
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
        TextView name = (TextView) convertView.findViewById(R.id.title);

        // getting movie data for the row
        Category m = categories.get(position);

        // title
        name.setText(m.getName());

        return convertView;
    }
}