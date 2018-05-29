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

public class VendorDealerAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataVendorDealer> Entities;

    public VendorDealerAdapter(Tab1vendorFragment tab1vendorFragment, List<DataVendorDealer> entities) {
        this.activity = activity;
        this.Entities = entities;
    }

    @Override
    public int getCount() {
        return Entities.size();
    }

    @Override
    public Object getItem(int i) {
        return Entities.get(i);
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
            convertView = inflater.inflate(R.layout.vendor_dealer_listrow, null);

        //Assign Id'S
        TextView name = (TextView) convertView.findViewById(R.id.vd_name);
        TextView type = (TextView) convertView.findViewById(R.id.vd_type);
        TextView address = (TextView) convertView.findViewById(R.id.vd_address);
        TextView area = (TextView) convertView.findViewById(R.id.vd_area);
        TextView mobileno = (TextView) convertView.findViewById(R.id.vd_mobile);
        TextView state = (TextView) convertView.findViewById(R.id.vd_state);
        TextView organisation = (TextView) convertView.findViewById(R.id.vd_organisation);
        TextView gst = (TextView) convertView.findViewById(R.id.vd_gst);
        TextView email = (TextView) convertView.findViewById(R.id.vd_emailid);
        TextView designation = (TextView) convertView.findViewById(R.id.vd_designation);

        DataVendorDealer m = Entities.get(position);

        // title
        name.setText(m.getName());
        type.setText(m.getType());
        address.setText(m.getAddress());
        area.setText(m.getArea());
        mobileno.setText(m.getMobileno());
        state.setText(m.getState());
        organisation.setText(m.getOrganisation());
        gst.setText(m.getGst());
        email.setText(m.getEmailid());
        designation.setText(m.getDesignation());

        return convertView;
    }
}
