package com.pcdgroup.hp.pcd_group.VendorDealer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminSetting;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Product.Entity;
import com.pcdgroup.hp.pcd_group.PurchaseOrder.PO_List;
import com.pcdgroup.hp.pcd_group.PurchaseOrder.ProductData;
import com.pcdgroup.hp.pcd_group.PurchaseOrder.SelectVendorProducts;
import com.pcdgroup.hp.pcd_group.PurchaseOrder.VendorProductAdapter;
import com.pcdgroup.hp.pcd_group.Quotation.ProdactEntity;
import com.pcdgroup.hp.pcd_group.Quotation.ProductCustomListAdapter;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserRegistarActivity;

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
 * @class_name VendorProductAdd
 * @description  vendor add new product in database
 */

public class VendorProductAdd extends AppCompatActivity implements CallBackInterface {

    private ListView listView;
    ProductList_VendorAdepter adapter;
    String[] data;
    ArrayList<String> picNames;
    String recordName,EmailHolders;
    List<ProductdataVendor> localEntity;
    GlobalVariable gblVar;
    String id;
    ProductdataVendor selectedRowLabel;
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        /*
            - add vendor product to show in details
            - show product to product id
        */

        Intent intent = getIntent();
        EmailHolders = intent.getStringExtra("email");

        gblVar = GlobalVariable.getInstance();

        localEntity = new ArrayList<ProductdataVendor>();
        recordName = new String("");
        picNames = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.lstv);

        adapter = new ProductList_VendorAdepter(this, localEntity,this);
        listView.setAdapter(adapter);

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        urlQry = DataGetUrl.VIEW_PRODUCT;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                VendorProductAdd.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        //Adepter
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.action_done) {

            SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
            //Check if item is selected or not via size
            if (selectedRows.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                //Loop to all the selected rows array

                for (int i = 0; i < selectedRows.size(); i++) {

                    //Check if selected rows have value i.e. checked item
                    if (selectedRows.valueAt(i)) {

                        //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                         selectedRowLabel = localEntity.get(selectedRows.keyAt(i));

                        //append the row label text
                        stringBuilder.append(selectedRowLabel.getId() + ",");

                    }
                }

                String productString = new String ();

                productString = String.valueOf((stringBuilder));

                Intent intent = new Intent();

                intent.putExtra("ProductID", productString);

                setResult(RESULT_OK, intent);

                finish();
            }
        }

        return super.onOptionsItemSelected(item);
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
                id = jo.getString("id");
                String picname = jo.getString("name");
                String urlname = jo.getString("photo");
                String price = jo.getString("price");
                String minimum = jo.getString("minimum");
                String hsncode=jo.getString("hsncode");
                String gst = jo.getString("gst");
                String brand=jo.getString("brand");
                String description=jo.getString("description");
                String stock=jo.getString("stock");
                String reorderlevel=jo.getString("reorderlevel");
                adapter.notifyDataSetChanged();
                picNames.add(picname);
                ProductdataVendor e = new ProductdataVendor(id,picname,urlname,price,gst, minimum,hsncode,description,stock,reorderlevel);
                localEntity.add(e);
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