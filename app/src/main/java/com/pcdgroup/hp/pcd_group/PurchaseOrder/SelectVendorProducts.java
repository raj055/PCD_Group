package com.pcdgroup.hp.pcd_group.PurchaseOrder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Quotation.ProdactEntity;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.VendorDealer.DealerList;
import com.pcdgroup.hp.pcd_group.VendorDealer.VendorProductAdd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name SelectVendorProducts
 * @description select selected vendor product
 */

public class SelectVendorProducts extends AppCompatActivity implements CallBackInterface {

    ListView listView;
    String recordName;
    int position;
    String str1;
    ArrayList<String> id;
    ProgressDialog progressDialog;
    String emailId;
    String finalResult;
    ArrayList<ProductData> prductlist = new ArrayList<ProductData>();
    VendorProductAdapter productAdapter;
    HttpParse httpParse;
    HashMap<String, String> hashMap = new HashMap<>();

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_products);

        /*
            - list of product to selected vendor
        */

        prductlist = new ArrayList<ProductData>();
        recordName = new String("");
        listView = (ListView) findViewById(R.id.list_product);

        httpParse = new HttpParse();

        str1 = new String();
        finalResult = new String();
        id = new ArrayList<String>();
        progressDialog = new ProgressDialog(this);

        //Adding ListView Item click Listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                Intent intent = new Intent();

                // Sending ListView clicked value using intent.
                ProductData pcdata = prductlist.get((int) id);
                intent.putExtra("name", pcdata.getTitle());
                intent.putExtra("hsncode", pcdata.getHsncode());
                intent.putExtra("gst", pcdata.getGst());
                intent.putExtra("price", pcdata.getPrice());
                setResult(RESULT_OK, intent);

                finish();

            }
        });

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {

                String[] vendoremail =  extras.getStringArray("vendor_email");

                emailId = vendoremail[5];
                str1 = vendoremail[9];

                String items[] = str1.split(",");

                for(int i = 0; i < items.length; i++) {

                    id.add(items[i].trim());

                    hashMap.put("id["+i+"]", id.get(i));
                }
            }
        }

        urlQry = DataGetUrl.VENDOR_PRODUCT_LIST;
        typeOfQuery = CallType.POST_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                SelectVendorProducts.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();
    }

    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
        try {
            JSONObject obj = new JSONObject(response);
            Toast.makeText(SelectVendorProducts.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

            JSONArray jsonArray = obj.getJSONArray("products");

            for(int i=0;i<jsonArray.length();i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ProductData GetData = new ProductData();

                GetData.setId(jsonObject.getString("id"));
                GetData.setTitle(jsonObject.getString("name"));
                GetData.setPrice(jsonObject.getString("price"));
                GetData.setHsncode(jsonObject.getString("hsncode"));
                GetData.setGst(jsonObject.getString("gst"));
                GetData.setDescription(jsonObject.getString("description"));

                prductlist.add(GetData);

            }

            productAdapter = new VendorProductAdapter(SelectVendorProducts.this,R.layout.list_product, prductlist);

            listView.setAdapter(productAdapter);

            productAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
