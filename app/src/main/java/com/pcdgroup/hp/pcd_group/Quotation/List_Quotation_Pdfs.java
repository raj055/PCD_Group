package com.pcdgroup.hp.pcd_group.Quotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.R;


import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name List_Quotation_Pdfs
 * @description list of all quotation list
 */

public class List_Quotation_Pdfs extends AppCompatActivity implements CallBackInterface {

    private ListView listView;
    QuotationAdepter adapter;
    List<pdf2> localPdf;
    String[] data;
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listpdfs_activity);

        /*
            - list of all quotation
            - click quotation to show and create bill
        */

        listView = (ListView) findViewById(R.id.listView);

        localPdf = new ArrayList<pdf2>();

        adapter = new QuotationAdepter(this, localPdf, this);
        listView.setAdapter(adapter);

        urlQry = DataGetUrl.ALL_QUOTATION_LIST;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                List_Quotation_Pdfs.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        adapter.notifyDataSetChanged();

        //setting listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String fileUrl = localPdf.get(position).getUrl();

                Intent intent = new Intent(List_Quotation_Pdfs.this, ViewInvoice.class);
                intent.putExtra("FileUrl", fileUrl);
                startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listView = null;
    }
}
