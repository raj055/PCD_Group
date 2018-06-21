package com.pcdgroup.hp.pcd_group.Quotation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.pcdgroup.hp.pcd_group.Product.CustomListAdapter;
import com.pcdgroup.hp.pcd_group.Product.Entity;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ProductCustomListAdapter extends BaseAdapter implements Filterable {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ProdactEntity> prodactEntities;
    private ArrayList<ProdactEntity> entityItemsfilter;
    private DataAdapterListener listener;

    public ProductCustomListAdapter(Activity activity, List<ProdactEntity> entityItems, SelectProduct listener) {
        this.activity = activity;
        this.prodactEntities = entityItems;
        this.listener = listener;
        this.entityItemsfilter = (ArrayList<ProdactEntity>) entityItems;
    }

    @Override
    public int getCount() { return entityItemsfilter.size(); }

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
            convertView = inflater.inflate(R.layout.product_listrow, null);


        //Assign Id'S
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView qunt = (TextView) convertView.findViewById(R.id.qunt);
        TextView hsncode = (TextView) convertView.findViewById(R.id.HSNCode);
        TextView gst = (TextView) convertView.findViewById(R.id.GST);
        TextView description = (TextView) convertView.findViewById(R.id.Descriprion);
        TextView stock = (TextView) convertView.findViewById(R.id.Stock);
        TextView reorderlevel = (TextView) convertView.findViewById(R.id.Reorderlevel);


        // getting movie data for the row
        ProdactEntity m = entityItemsfilter.get(position);

        // title
        title.setText(m.getTitle());
        // price & Quantity
        price.setText("Price: \u20B9" + String.valueOf(m.getPrice()));
        qunt.setText("minimum: " + String.valueOf(m.getQuantity()));
        hsncode.setText("Hsncode:" +String.valueOf(m.getHsncode()));
        gst.setText("GST:" +String.valueOf(m.getGst()));
        description.setText("Description:" +String.valueOf(m.getDescription()));
        stock.setText("Stock:" +String.valueOf(m.getstock()));
        reorderlevel.setText("Reorderlevel:" +String.valueOf(m.getReorderlevel()));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    entityItemsfilter = (ArrayList<ProdactEntity>) prodactEntities;
                } else {
                    List<ProdactEntity> filteredList = new ArrayList<>();
                    for (ProdactEntity row : prodactEntities) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    entityItemsfilter = (ArrayList<ProdactEntity>) filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = entityItemsfilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                entityItemsfilter = (ArrayList<ProdactEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface DataAdapterListener {
        void onDataSelected(Entity dataAdapter);
    }
}
