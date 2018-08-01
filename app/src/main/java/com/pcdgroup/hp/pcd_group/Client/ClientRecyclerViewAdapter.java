package com.pcdgroup.hp.pcd_group.Client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pcdgroup.hp.pcd_group.Quotation.ClientDataAdapter;
import com.pcdgroup.hp.pcd_group.Quotation.SelectClient;
import com.pcdgroup.hp.pcd_group.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ClientRecyclerViewAdapter
 * @description client recycler view to set client details
 */

public class ClientRecyclerViewAdapter extends RecyclerView.Adapter<ClientRecyclerViewAdapter.ViewHolder>
        implements Filterable {

    private Context context;

    private List<ClientDataAdapter> clientDataAdapters;

    private List<ClientDataAdapter> dataListFiltered;

    private DataAdapterListener listener;

    public ClientRecyclerViewAdapter(SelectClient selectClient, List<ClientDataAdapter> getClientDataAdapter, SelectClient listener) {

        this.clientDataAdapters = getClientDataAdapter;
        this.listener = listener;
        this.context = selectClient;
        this.dataListFiltered = getClientDataAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clientcardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        ClientDataAdapter clientDataAdapter =  dataListFiltered.get(position);

        viewHolder.TextViewfName.setText(clientDataAdapter.getfName());

        viewHolder.TextViewlName.setText(clientDataAdapter.getlName());

        viewHolder.TextViewAddress.setText(clientDataAdapter.getAddress());

        viewHolder.TextViewCompanyName.setText(clientDataAdapter.getCompanyname());

    }

    @Override
    public int getItemCount() {

        return dataListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered = clientDataAdapters;
                } else {
                    List<ClientDataAdapter> filteredList = new ArrayList<>();
                    for (ClientDataAdapter row : clientDataAdapters) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getfName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    dataListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFiltered = (ArrayList<ClientDataAdapter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView TextViewfName;
        public TextView TextViewlName;
        public TextView TextViewAddress;
        public TextView TextViewCompanyName;

        public ViewHolder(View itemView) {

            super(itemView);

            TextViewfName = (TextView) itemView.findViewById(R.id.tvfname) ;
            TextViewlName = (TextView) itemView.findViewById(R.id.tvlname) ;
            TextViewAddress = (TextView) itemView.findViewById(R.id.tvaddress) ;
            TextViewCompanyName = (TextView) itemView.findViewById(R.id.tvcompanyname) ;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                // send selected contact in callback
                listener.onDataSelected(dataListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface DataAdapterListener {
        void onDataSelected(ClientDataAdapter dataAdapter);
    }

}