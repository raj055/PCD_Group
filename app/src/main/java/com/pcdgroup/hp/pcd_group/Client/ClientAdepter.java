package com.pcdgroup.hp.pcd_group.Client;

import android.app.Activity;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pcdgroup.hp.pcd_group.Quotation.Pdf;
import com.pcdgroup.hp.pcd_group.Quotation.PdfAdapter;
import com.pcdgroup.hp.pcd_group.R;

import java.util.ArrayList;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ClientAdepter extends ArrayAdapter<DataAdapter> {

    Activity activity;
    int layoutResourceId;
    ArrayList<DataAdapter> data = new ArrayList<DataAdapter>();
    DataAdapter client;

    public ClientAdepter(Activity activity, int layoutResourceId, ArrayList<DataAdapter> data) {
        super(activity, layoutResourceId, data);
        this.activity = activity;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        clienHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new clienHolder();
            holder.textViewName = (TextView) row.findViewById(R.id.tvname);
            holder.textViewType = (TextView) row.findViewById(R.id.tvtype);
            holder.textViewAddress = (TextView) row.findViewById(R.id.tvaddress);
            holder.textViewAddress1 = (TextView) row.findViewById(R.id.tvaddressline1);
            holder.textViewAddress2 = (TextView) row.findViewById(R.id.tvaddressline2);
            holder.textViewMobile = (TextView) row.findViewById(R.id.tv_Mobileno);
            holder.textViewState = (TextView) row.findViewById(R.id.tv_state);
            holder.textViewCounty = (TextView) row.findViewById(R.id.tvCountry);
            holder.textViewCompany = (TextView) row.findViewById(R.id.tv_companyname);
            holder.textViewEmail = (TextView) row.findViewById(R.id.tvemailid);
            holder.textViewPin = (TextView) row.findViewById(R.id.tvpin);
            holder.textViewDesignation = (TextView) row.findViewById(R.id.tvdesignation);
            row.setTag(holder);
        } else {
            holder = (clienHolder) row.getTag();
        }

        client = data.get(position);
        holder.textViewName.setText(client.getName());
        holder.textViewType.setText(client.getType());
        holder.textViewAddress.setText(client.getAddress());
        holder.textViewAddress1.setText(client.getAddressline1());
        holder.textViewAddress2.setText(client.getAddressline2());
        holder.textViewMobile.setText(client.getMobileno());
        holder.textViewState.setText(client.getState());
        holder.textViewCounty.setText(client.getcountry());
        holder.textViewCompany.setText(client.getCompanyname());
        holder.textViewEmail.setText(client.getEmailid());
        holder.textViewPin.setText(client.getPin());
        holder.textViewDesignation.setText(client.getDesignation());
        return row;
    }

    class clienHolder {
        TextView textViewName, textViewType, textViewAddress, textViewAddress1, textViewAddress2,
                textViewMobile, textViewState, textViewCounty, textViewCompany, textViewEmail, textViewPin ,textViewDesignation;
    }
}