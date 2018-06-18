package com.pcdgroup.hp.pcd_group.VendorDealer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcdgroup.hp.pcd_group.R;

import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 18-06-2018.
 */

public class VendorListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<VendorData> vendorData;

    public VendorListAdapter(Activity activity, List<VendorData> vendorData, VendorList listener) {
        this.activity = activity;
        this.vendorData = vendorData;
    }

    @Override
    public int getCount() {
        return vendorData.size();
    }

    @Override
    public Object getItem(int location) {
        return vendorData.get(location);
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
            convertView = inflater.inflate(R.layout.list_layout, null);


        TextView name = (TextView) convertView.findViewById(R.id.textViewName);
        TextView email = (TextView) convertView.findViewById(R.id.textViewEmail);

        // getting movie data for the row
        VendorData m = vendorData.get(position);


        // title
        name.setText(m.getName());
        email.setText(m.getEmail());

        return convertView;
    }
}
