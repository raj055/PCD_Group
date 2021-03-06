package com.pcdgroup.hp.pcd_group.PurchaseOrder;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminSetting;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
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
 * @class_name FinishedOrder
 * @description list of finish order
 */

public class FinishedOrder extends AppCompatActivity implements CallBackInterface {

    //PO - list components.
    ListView listView;
    FinishedOrderList_Adepter adapter;
    List<PurchaseData> localPdf;
    String[] data;

    //Database Components
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    /** Populates the screen includes the list of clients added by the logged in user/client.
     * @param savedInstanceState object of passing parameters from the previous intent */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        // list of finish order
        // show to finish order

        listView = (ListView) findViewById(R.id.lV_FinishedOrder);

        localPdf = new ArrayList<PurchaseData>();

        adapter = new FinishedOrderList_Adepter(this, localPdf, this);
        listView.setAdapter(adapter);

        urlQry = DataGetUrl.COMPLETE_ORDER_DETAILS;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                FinishedOrder.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        adapter.notifyDataSetChanged();

        //setting listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                String urlname = jo.getString("url");
                String email = jo.getString("email");
                String purchseOrder = jo.getString("purchaseorder");
                String completeOrder = jo.getString("completeorder");

                PurchaseData pdf = new PurchaseData(id,urlname,email,purchseOrder,completeOrder);
                localPdf.add(pdf);
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
