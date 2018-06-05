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

public class FinishedOrderList_Adepter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<pdf2> pdf2List;

    public FinishedOrderList_Adepter(Activity activity, List<pdf2> pdf2List, FinishedOrder listener) {
        this.activity = activity;
        this.pdf2List = pdf2List;
    }

    @Override
    public int getCount() {
        return pdf2List.size();
    }

    @Override
    public Object getItem(int location) {
        return pdf2List.get(location);
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
        pdf2 m = pdf2List.get(position);


        // title
        name.setText(m.getName());
        email.setText(m.getEmail());

        return convertView;
    }
}