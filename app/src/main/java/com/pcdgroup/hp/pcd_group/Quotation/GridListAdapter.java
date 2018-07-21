package com.pcdgroup.hp.pcd_group.Quotation;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pcdgroup.hp.pcd_group.Product.CustomVolleyRequest;
import com.pcdgroup.hp.pcd_group.Product.Entity;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name
 * @description
 */

public class GridListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ProdactEntity> entityItems;
    private ImageLoader imageLoader;

    int numberOfCheckboxesChecked = 0;

    private SparseBooleanArray mSelectedItemsIds;

    public GridListAdapter(Activity activity, List<ProdactEntity> entityItems, Quotation_product listener) {
        this.activity = activity;
        this.entityItems = entityItems;
        mSelectedItemsIds = new SparseBooleanArray();
        imageLoader = CustomVolleyRequest.getInstance(activity.getApplicationContext())
                .getImageLoader();
    }

    @Override
    public int getCount() { return entityItems.size(); }

    @Override
    public Object getItem(int location) {
        return entityItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.grid_custom_row_layout, null);

        if (imageLoader == null)
            imageLoader = CustomVolleyRequest.getInstance(activity.getApplicationContext())
                    .getImageLoader();

        //Assign Id'S
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.label);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        // getting movie data for the row
        ProdactEntity m = entityItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());

        checkBox.setChecked(mSelectedItemsIds.get(position));

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkCheckBox(position, !mSelectedItemsIds.get(position));
            }
        });

        final View finalConvertView = convertView;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && numberOfCheckboxesChecked >= 10) {

                    Toast.makeText(finalConvertView.getContext(),"You Check Only 10 Items.",Toast.LENGTH_SHORT).show();

                    checkBox.setChecked(false);
                } else {

                    if (isChecked) {
                        numberOfCheckboxesChecked++;
                    } else {
                        numberOfCheckboxesChecked--;
                    }
                }

            }
        });

        return convertView;
    }

    /**
     * Remove all checkbox Selection
     **/
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
