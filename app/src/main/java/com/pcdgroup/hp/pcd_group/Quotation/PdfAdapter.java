package com.pcdgroup.hp.pcd_group.Quotation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pcdgroup.hp.pcd_group.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class PdfAdapter extends ArrayAdapter<Pdf> {

    Activity activity;
    int layoutResourceId;
    ArrayList<Pdf> data=new ArrayList<Pdf>();
    Pdf pdf;

    public PdfAdapter(Activity activity, int layoutResourceId, ArrayList<Pdf> data) {
        super(activity, layoutResourceId, data);
        this.activity=activity;
        this.layoutResourceId=layoutResourceId;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        PdfHolder holder=null;
        if(row==null)
        {
            LayoutInflater inflater=LayoutInflater.from(activity);
            row=inflater.inflate(layoutResourceId,parent,false);
            holder=new PdfHolder();
            holder.textViewName= (TextView) row.findViewById(R.id.textViewName);
            holder.textViewEmail= (TextView) row.findViewById(R.id.textViewEmail);
            row.setTag(holder);
        }
        else
        {
            holder= (PdfHolder) row.getTag();
        }

        pdf = data.get(position);
        holder.textViewName.setText(pdf.getName());
        holder.textViewEmail.setText(pdf.getEmail());
        return row;
    }

    class PdfHolder
    {
        TextView textViewName,textViewEmail;
    }

}