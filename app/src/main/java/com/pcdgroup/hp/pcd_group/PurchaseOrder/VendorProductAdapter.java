package com.pcdgroup.hp.pcd_group.PurchaseOrder;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.pcdgroup.hp.pcd_group.Client.ClientAdepter;
import com.pcdgroup.hp.pcd_group.Client.DataAdapter;
import com.pcdgroup.hp.pcd_group.Quotation.ProdactEntity;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.VendorDealer.VendorData;
import com.pcdgroup.hp.pcd_group.VendorDealer.VendorList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 18-06-2018.
 */

public class VendorProductAdapter extends ArrayAdapter<ProductData> {

    private Activity activity;
    private LayoutInflater inflater;
    private boolean isListView;
    private SparseBooleanArray mSelectedItemsIds;

    int layoutResourceId;
    public static ArrayList<ProductData> productData = new ArrayList<ProductData>();
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
        productHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new VendorProductAdapter.productHolder();
            holder.textViewName = (TextView) row.findViewById(R.id.textViewName);
            row.setTag(holder);
        } else {
            holder = (VendorProductAdapter.productHolder) row.getTag();
        }

        product = productData.get(position);
        holder.textViewName.setText(product.getTitle());


        Log.v("quantity",product.toString());
        return row;
    }

    class productHolder {
        TextView textViewName;
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