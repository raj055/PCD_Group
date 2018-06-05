package com.pcdgroup.hp.pcd_group.VendorDealer;

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
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

public class VendorProductAdd extends AppCompatActivity {

    ListView listView;
    ProductList_VendorAdepter adapter;
    String HttpURL1 = "http://dert.co.in/gFiles/fimage.php";
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;
    ArrayList<String> picNames;
    String recordName,EmailHolders;
    List<ProductData> localEntity;
    GlobalVariable gblVar;

    String HttpProductAdd = "http://dert.co.in/gFiles/MultipleProductsAdd.php";
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String Products_Holder;
    String finalResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        Intent intent = getIntent();
        EmailHolders = intent.getStringExtra("email");

        gblVar = GlobalVariable.getInstance();

        localEntity = new ArrayList<ProductData>();
        recordName = new String("");
        picNames = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.lstv);

        adapter = new ProductList_VendorAdepter(this, localEntity,this);
        listView.setAdapter(adapter);

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        //Retrieve
        getData();

        //Adepter
        adapter.notifyDataSetChanged();

    }

    private void getData(){

        try {
            URL url = new URL(HttpURL1);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is = new BufferedInputStream(con.getInputStream());

        }catch (Exception e){
            e.printStackTrace();
        }

        //Read in content into String
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        //Parse json data
        try {

            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            data = new String[ja.length()];

            for (int i=0; i<ja.length();i++){

                jo=ja.getJSONObject(i);
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
                ProductData e = new ProductData(picname,urlname,price,gst, minimum,hsncode,description,stock,reorderlevel);
                localEntity.add(e);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done,menu);
        return super.onCreateOptionsMenu(menu);
    }

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
                        ProductData selectedRowLabel = localEntity.get(selectedRows.keyAt(i));

                        //append the row label text
                        stringBuilder.append(selectedRowLabel + "\n");
                    }
                }
                AddProducts(Products_Holder);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void AddProducts(final String productadd){

        class AddProducts extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(VendorProductAdd.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(VendorProductAdd.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("submit",params[0]);

                finalResult = httpParse.postRequest(hashMap, HttpProductAdd);

                return finalResult;
            }
        }

        AddProducts addProducts = new AddProducts();

        addProducts.execute(productadd);
    }
}