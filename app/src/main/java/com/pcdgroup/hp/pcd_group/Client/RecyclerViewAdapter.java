package com.pcdgroup.hp.pcd_group.Client;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pcdgroup.hp.pcd_group.Quotation.ClientDataAdapter;
import com.pcdgroup.hp.pcd_group.Quotation.SelectClient;
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

    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    public RecyclerViewAdapter(ClientDetailsActivity clientDetailsActivity, List<DataAdapter> dataAdapters, ClientDetailsActivity listener) {

        this.context = clientDetailsActivity;
        this.listener = listener;
        this.dataAdapters = dataAdapters;
        this.dataListFiltered = dataAdapters;

        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
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

        // change the row state to activated
        viewHolder.itemView.setActivated(selectedItems.get(position, false));

        // handle icon animation
        applyIconAnimation(viewHolder, position);

        // apply click events
        applyClickEvents(viewHolder, position);
    }

    private void applyClickEvents(ViewHolder holder, final int position) {
        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
            }
        });
    }

    private void applyIconAnimation(ViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(context, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(context, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }

    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    @Override
    public int getItemCount() {

        return dataListFiltered.size();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        dataAdapters.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

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
        public RelativeLayout iconContainer, iconBack, iconFront;

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

            iconBack = (RelativeLayout) itemView.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) itemView.findViewById(R.id.icon_front);
            iconContainer = (RelativeLayout) itemView.findViewById(R.id.icon_container);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }

    public interface DataAdapterListener {
        void onDataSelected(DataAdapter dataAdapter);

        void onIconClicked(int position);

        void onRowLongClicked(int adapterPosition);

        void onIconImportantClicked(int position);

        void onMessageRowClicked(int position);
    }
}