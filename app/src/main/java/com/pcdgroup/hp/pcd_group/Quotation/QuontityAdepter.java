package com.pcdgroup.hp.pcd_group.Quotation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.pcdgroup.hp.pcd_group.R;

import java.util.List;

public class QuontityAdepter  extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ProdactEntity> prodactEntities;

    public QuontityAdepter(Activity activity, List<ProdactEntity> prodactEntities, Quotation_quantity listener) {
        this.activity = activity;
        this.prodactEntities = prodactEntities;
    }

    @Override
    public int getCount() { return prodactEntities.size(); }

    @Override
    public Object getItem(int location) {
        return prodactEntities.get(location);
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
            convertView = inflater.inflate(R.layout.list_quontaity, null);


        TextView name = (TextView) convertView.findViewById(R.id.textViewName);
        TextView price = (TextView) convertView.findViewById(R.id.textprice);
        EditText qunt = (EditText) convertView.findViewById(R.id.qunt_price);

        // getting movie data for the row
        ProdactEntity m = prodactEntities.get(position);

        // title
        name.setText(m.gettitle());
//        price.setText(m.getPrice());

        return convertView;
    }

}