package com.pcdgroup.hp.pcd_group.Product;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pcdgroup.hp.pcd_group.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ImageAdapter
 * @description image adepter to set image size and his name to link in database
 */

public class ImageAdapter extends BaseAdapter {

    private Activity activity;
    private List<Entity> entityItems;
    private ImageLoader imageLoader;

    public ImageAdapter(Activity activity, ViewImage listener){
        this.activity = activity;
        this.entityItems = entityItems;
        imageLoader = CustomVolleyRequest.getInstance(activity.getApplicationContext())
                .getImageLoader();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (imageLoader == null)
            imageLoader = CustomVolleyRequest.getInstance(activity.getApplicationContext())
                    .getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

        Entity m = entityItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        return convertView;
    }
}
