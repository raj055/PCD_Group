package com.pcdgroup.hp.pcd_group.PurchaseOrder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcdgroup.hp.pcd_group.Quotation.pdf2;
import com.pcdgroup.hp.pcd_group.R;

import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name
 * @description
 */

public class Purchaselist_Adepter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<PurchaseData> purchaseData;

    public Purchaselist_Adepter(Activity activity, List<PurchaseData> purchaseData, PO_List listener) {
        this.activity = activity;
        this.purchaseData = purchaseData;
    }

    @Override
    public int getCount() { return purchaseData.size(); }

    @Override
    public Object getItem(int location) {
        return purchaseData.get(location);
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
        PurchaseData m = purchaseData.get(position);


        // title
        name.setText(m.getPurchaseorder());
        email.setText(m.getEmail());

        return convertView;
    }
}