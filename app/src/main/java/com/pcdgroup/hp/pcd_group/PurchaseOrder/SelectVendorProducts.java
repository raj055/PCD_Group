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
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.VendorDealer.DealerList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectVendorProducts extends AppCompatActivity {

    ListView listView;
    String HttpURL = "http://dert.co.in/gFiles/VendorProductList.php";
    String recordName;
    int position;
    String str,str1;
    ArrayList<String> id;
    ProgressDialog progressDialog;
    String emailId;
    String finalResult;
    ArrayList<ProductData> prductlist = new ArrayList<ProductData>();
    VendorProductAdapter productAdapter;
    HttpParse httpParse;
    HashMap<String, String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_products);

        prductlist = new ArrayList<ProductData>();
        recordName = new String("");
        listView = (ListView) findViewById(R.id.list_product);

        httpParse = new HttpParse();

        finalResult = new String();
        id = new ArrayList<String>();
        progressDialog = new ProgressDialog(this);

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

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

        //Retrieve
        GetProductList(id);
    }

    public void GetProductList(final ArrayList<String> productID) {

        class GetProductList extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                try {
                    JSONObject obj = new JSONObject(httpResponseMsg);
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

            @Override
            protected String doInBackground(String... params) {

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        GetProductList getProductList = new GetProductList();

        getProductList.execute(String.valueOf(productID));
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

            SparseBooleanArray selectedRows = productAdapter.getSelectedIds();//Get the selected ids from adapter
            //Check if item is selected or not via size
            if (selectedRows.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                //Loop to all the selected rows array
                for (int i = 0; i < selectedRows.size(); i++) {

                    //Check if selected rows have value i.e. checked item
                    if (selectedRows.valueAt(i)) {

                        //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                        ProductData selectedRowLabel = prductlist.get(selectedRows.keyAt(i));

                        //append the row label text
//                        stringBuilder.append(selectedRowLabel + "\n");

                        Log.v("Selected String ===== ", String.valueOf(selectedRowLabel));
                    }
                }
            }

            Intent intent = new Intent(SelectVendorProducts.this,PO_List.class);

            ProductData productdata = prductlist.get(position);

            intent.putExtra("name",productdata.getTitle());
            intent.putExtra("price", productdata.getPrice());
            intent.putExtra("hsncode", productdata.getHsncode());
            intent.putExtra("gst", productdata.getGst());
            intent.putExtra("description", productdata.getDescription());

            setResult(RESULT_OK, intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
