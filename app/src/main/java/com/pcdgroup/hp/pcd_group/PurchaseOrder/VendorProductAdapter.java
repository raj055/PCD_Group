package com.pcdgroup.hp.pcd_group.PurchaseOrder;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pcdgroup.hp.pcd_group.Client.ClientAdepter;
import com.pcdgroup.hp.pcd_group.Client.DataAdapter;
import com.pcdgroup.hp.pcd_group.Quotation.ProdactEntity;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.VendorDealer.VendorData;
import com.pcdgroup.hp.pcd_group.VendorDealer.VendorList;

import java.util.ArrayList;
import java.util.List;

public class VendorProductAdapter extends ArrayAdapter<ProductData> {

    private Activity activity;
    private LayoutInflater inflater;
    private boolean isListView;
    private SparseBooleanArray mSelectedItemsIds;

    int layoutResourceId;
    ArrayList<ProductData> productData = new ArrayList<ProductData>();
    ProductData product;

    public VendorProductAdapter(Activity activity,int layoutResourceId, ArrayList<ProductData> productData) {
        super(activity,layoutResourceId,productData);
        this.activity = activity;
        this.productData = productData;
        this.isListView = isListView;
        this.layoutResourceId = layoutResourceId;

        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        VendorProductAdapter.productHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new VendorProductAdapter.productHolder();
            holder.textViewName = (TextView) row.findViewById(R.id.textViewName);
            holder.checkBox = (CheckBox) row.findViewById(R.id.checkbox);
            row.setTag(holder);
        } else {
            holder = (VendorProductAdapter.productHolder) row.getTag();
        }

        product = productData.get(position);
        holder.textViewName.setText(product.getTitle());
        holder.checkBox.setChecked(mSelectedItemsIds.get(position));

        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(position, !mSelectedItemsIds.get(position));
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(position, !mSelectedItemsIds.get(position));
            }
        });

        return row;
    }

    class productHolder {
        TextView textViewName;
        CheckBox checkBox;
    }

    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}