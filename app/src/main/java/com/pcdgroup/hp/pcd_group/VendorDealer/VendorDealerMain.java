package com.pcdgroup.hp.pcd_group.VendorDealer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcdgroup.hp.pcd_group.R;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name VendorDealerMain
 * @description main class of vendor and dealer to fragment
 */

public class VendorDealerMain extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    /** Register the details of Vendor / Dealer. Add the details to the database.
     * @param savedInstanceState object of passing parameters from the previous intent */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vendor_dealer);

        //    - add vendor fragment
        //    - add dealer fragment
        Log.v("Vendor Dealer-----","Selected");
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        Log.v("Vendor Dealer-----","Selected");
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        Log.v("Vendor Dealer-----","Selected");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        Log.v("Vendor Dealer-----","Finished");
        tabLayout.setupWithViewPager(mViewPager);
        Log.v("Vendor Dealer-----","Finished");
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.v("Vendor Dealer-----","setupViewPager");
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Add_Vendor(), "VENDOR");
        adapter.addFragment(new Add_Dealer(), "DEALER");
        viewPager.setAdapter(adapter);
    }
}
