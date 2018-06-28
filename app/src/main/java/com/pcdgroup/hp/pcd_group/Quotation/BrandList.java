package com.pcdgroup.hp.pcd_group.Quotation;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminSetting;
import com.pcdgroup.hp.pcd_group.AdminLogin.BrandAdepter;
import com.pcdgroup.hp.pcd_group.AdminLogin.Category;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
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
 * @class_name BrandList
 * @description brand list show in listview
 */

public class BrandList  extends AppCompatActivity implements CallBackInterface {

    ListView listView;
    List<Category> categoriesList;
    BrandAdepter adepter;
    String[] data;
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brandlist);

        /*
            - show brand list
            - add brand list in invoice
        */

        listView = (ListView) findViewById(R.id.list_address);

        categoriesList = new ArrayList<Category>();

        adepter = new BrandAdepter(this, categoriesList);
        listView.setAdapter(adepter);

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();

                // Sending ListView clicked value using intent.
                Category pcdata = categoriesList.get((int) id);
                intent.putExtra("name", pcdata.getName());
                intent.putExtra("address", pcdata.getAddress());
                intent.putExtra("address1", pcdata.getAddress1());
                intent.putExtra("address2", pcdata.getAddress2());
                intent.putExtra("pincode", pcdata.getPincode());
                intent.putExtra("state", pcdata.getState());
                intent.putExtra("mobileno", pcdata.getMobileno());
                intent.putExtra("email", pcdata.getEmail());
                intent.putExtra("website", pcdata.getWebsite());
                intent.putExtra("pan", pcdata.getPan());
                intent.putExtra("gst", pcdata.getGst());

                setResult(RESULT_OK, intent);

                finish();

             }
         });

        urlQry = DataGetUrl.LIST_BRAND;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                BrandList.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

    }

    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
        try {

            JSONArray ja = new JSONArray(response);
            JSONObject jo = null;

            data = new String[ja.length()];

            for (int i=0; i<ja.length();i++){

                jo=ja.getJSONObject(i);
                String name = jo.getString("name");
                String address = jo.getString("address");
                String address1 = jo.getString("address1");
                String address2 = jo.getString("address2");
                String pincode = jo.getString("pincode");
                String state = jo.getString("state");
                String mobileno = jo.getString("mobileno");
                String email = jo.getString("email");
                String website = jo.getString("website");
                String pan = jo.getString("pan");
                String gst = jo.getString("gst");
                Category e = new Category(name, address, address1, address2, pincode,
                        state, mobileno, email, website, pan, gst);
                categoriesList.add(e);
                adepter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
