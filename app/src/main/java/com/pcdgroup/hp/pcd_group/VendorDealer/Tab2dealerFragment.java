package com.pcdgroup.hp.pcd_group.VendorDealer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.pcdgroup.hp.pcd_group.R;

public class Tab2dealerFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dealer,container,false);

        return view;
    }

}
