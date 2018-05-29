package com.pcdgroup.hp.pcd_group.VendorDealer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.ClientRegisterActivity;
import com.pcdgroup.hp.pcd_group.R;

public class List_VendorDealer extends AppCompatActivity{

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vendor_dealer);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(List_VendorDealer.this,Vendor_Dealer.class);
                startActivity(intent);
            }
        });

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1vendorFragment(), "VENDOR");
        adapter.addFragment(new Tab2dealerFragment(), "DEALER");
        viewPager.setAdapter(adapter);
    }
}
