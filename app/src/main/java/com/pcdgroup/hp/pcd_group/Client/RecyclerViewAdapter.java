package com.pcdgroup.hp.pcd_group.Client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pcdgroup.hp.pcd_group.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
        implements Filterable {

    private Context context;

    private List<DataAdapter> dataAdapters;

    private List<DataAdapter> dataListFiltered;

    private DataAdapterListener listener;

    public RecyclerViewAdapter(ClientDetailsActivity clientDetailsActivity, List<DataAdapter> dataAdapters, ClientDetailsActivity listener) {

        this.context = clientDetailsActivity;
        this.listener = listener;
        this.dataAdapters = dataAdapters;
        this.dataListFiltered = dataAdapters;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        DataAdapter dataAdapter =  dataListFiltered.get(position);

        viewHolder.TextViewName.setText(dataAdapter.getName());
        viewHolder.TextViewType.setText(dataAdapter.getType());
        viewHolder.TextViewAddress.setText(dataAdapter.getAddress());
        viewHolder.TextviewAddressline1.setText(dataAdapter.getAddressline1());
        viewHolder.TextviewAddressline2.setText(dataAdapter.getAddressline2());
        viewHolder.TextviewMobileno.setText(dataAdapter.getMobileno());
        viewHolder.TextviewState.setText(dataAdapter.getState());
        viewHolder.TextviewCountry.setText(dataAdapter.getcountry());
        viewHolder.TextViewCompanyName.setText(dataAdapter.getCompanyname());
        viewHolder.TextviewPin.setText(dataAdapter.getPin());
        viewHolder.TextViewEmailID.setText(dataAdapter.getEmailid());
        viewHolder.TextViewDesignation.setText(dataAdapter.getDesignation());

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
                    dataListFiltered = dataAdapters;
                } else {
                    List<DataAdapter> filteredList = new ArrayList<>();
                    for (DataAdapter row : dataAdapters) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                dataListFiltered = (ArrayList<DataAdapter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView TextViewName;
        public TextView TextViewType;
        public TextView TextViewAddress;
        public TextView TextviewAddressline1;
        public TextView TextviewAddressline2;
        public TextView TextviewMobileno;
        public TextView TextviewState;
        public TextView TextviewCountry;
        public TextView TextViewCompanyName;
        public TextView TextviewPin;
        public TextView TextViewEmailID;
        public TextView TextViewDesignation;


        public ViewHolder(View itemView) {

            super(itemView);

            TextViewName = (TextView) itemView.findViewById(R.id.tvname) ;
            TextViewType = (TextView) itemView.findViewById(R.id.tvtype) ;
            TextViewAddress = (TextView) itemView.findViewById(R.id.tvaddress) ;
            TextviewAddressline1 = (TextView) itemView.findViewById(R.id.tvaddressline1) ;
            TextviewAddressline2 = (TextView) itemView.findViewById(R.id.tvaddressline2) ;
            TextviewMobileno = (TextView) itemView.findViewById(R.id.tv_Mobileno) ;
            TextviewState = (TextView) itemView.findViewById(R.id.tv_state) ;
            TextviewCountry = (TextView) itemView.findViewById(R.id.tvCountry) ;
            TextViewCompanyName = (TextView) itemView.findViewById(R.id.tv_companyname) ;
            TextviewPin = (TextView) itemView.findViewById(R.id.tvpin) ;
            TextViewEmailID = (TextView) itemView.findViewById(R.id.tvemailid) ;
            TextViewDesignation = (TextView) itemView.findViewById(R.id.tvdesignation) ;

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
        void onDataSelected(DataAdapter dataAdapter);
    }
}