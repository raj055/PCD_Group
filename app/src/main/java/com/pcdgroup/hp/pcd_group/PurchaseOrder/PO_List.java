package com.pcdgroup.hp.pcd_group.PurchaseOrder;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminSetting;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Product.UploadImage;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.Quotation.List_Quotation_Pdfs;
import com.pcdgroup.hp.pcd_group.Quotation.QuotationAdepter;
import com.pcdgroup.hp.pcd_group.Quotation.ViewInvoice;
import com.pcdgroup.hp.pcd_group.Quotation.pdf2;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name PO_List
 * @description list of purchase order
 */

public class PO_List extends AppCompatActivity implements CallBackInterface {

    ListView listView;
    Purchaselist_Adepter adapter;
    List<pdf2> localPdf;
    String[] data;
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po_list);

        /*
            - list of purchase order
            - create new purchase order
        */

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PO_List.this,Create_New_PO.class);
                startActivityForResult(intent, 1);

            }
        });

        listView = (ListView) findViewById(R.id.lstv);

        localPdf = new ArrayList<pdf2>();

        adapter = new Purchaselist_Adepter(this, localPdf, this);
        listView.setAdapter(adapter);

        urlQry = DataGetUrl.PURCHASE_ORDER_DETAILS;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                PO_List.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        //setting listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
        try {

            JSONArray ja = new JSONArray(response);
            JSONObject jo = null;

            data = new String[ja.length()];

            for (int i=0; i<ja.length();i++){

                jo=ja.getJSONObject(i);
                String id = jo.getString("id");
                String name = jo.getString("name");
                String urlname = jo.getString("url");
                String email = jo.getString("email");
                String bill = jo.getString("Billing");
                String purchseOrder = jo.getString("purchaseorder");
                String completeOrder = jo.getString("completeorder");

                pdf2 pdf= new pdf2(id,name,urlname,email,bill,purchseOrder,completeOrder);
                localPdf.add(pdf);
                adapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
