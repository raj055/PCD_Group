package com.pcdgroup.hp.pcd_group.VendorDealer;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminSetting;
import com.pcdgroup.hp.pcd_group.Client.SingleRecordShow;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Product.Entity;
import com.pcdgroup.hp.pcd_group.PurchaseOrder.Create_New_PO;
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
 * @class_name VendorList
 * @description list of vendor  in list view to vendor name
 */

public class VendorList  extends AppCompatActivity implements CallBackInterface {

    private ListView listView;
    VendorListAdapter adapter;
    List<VendorData> localdata;
    String[] data;

    //Database Components
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    /** Display the vendor list for preparing the PO.
     * @param savedInstanceState object of passing parameters from the previous intent */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_list);

        /*
            - list of vendor name
        */

        listView = (ListView) findViewById(R.id.listViewVendor);

        localdata = new ArrayList<VendorData>();

        adapter = new VendorListAdapter(this, localdata, this);
        listView.setAdapter(adapter);

        urlQry = DataGetUrl.VENDOR_LIST;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                VendorList.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        //setting listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(VendorList.this, Create_New_PO.class);

                VendorData productdata = localdata.get(position);

                intent.putExtra("id",productdata.getId());
                intent.putExtra("name",productdata.getName());
                intent.putExtra("address",productdata.getAddress());
                intent.putExtra("location",productdata.getLocation());
                intent.putExtra("state",productdata.getState());
                intent.putExtra("email",productdata.getEmail());
                intent.putExtra("mobileno",productdata.getMobileno());
                intent.putExtra("organisation",productdata.getOrganisation());
                intent.putExtra("gstno",productdata.getGst());
                intent.putExtra("products",productdata.getProducts());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    /** CallBack Function for processing the Database query result.
     * @param  response - Response string received while database query.
     *         dataGetUrl - Url queried.*/
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
                String address = jo.getString("address");
                String area = jo.getString("location");
                String state = jo.getString("state");
                String email = jo.getString("email");
                String mobile = jo.getString("mobileno");
                String organisation = jo.getString("organisation");
                String gst = jo.getString("gstno");
                String products = jo.getString("products");

                VendorData data= new VendorData(id,name,address,area,state,email,mobile,organisation,gst,products);
                localdata.add(data);
                adapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /** Releases the memory of all the components after intent finishes. */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        listView = null;
    }

}
