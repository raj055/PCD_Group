package com.pcdgroup.hp.pcd_group.PurchaseOrder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.AdminLogin.AdminSetting;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.Quotation.ProdactEntity;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SelectVendorProducts extends AppCompatActivity {

    ListView listView;
    VendorProductAdapter adapter;
    String HttpURL1 = "http://dert.co.in/gFiles/fimage.php";
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;
    ArrayList<String> picNames;
    String recordName,EmailHolders;
    List<ProductData> localEntity;
    GlobalVariable gblVar;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_products);

        Intent intent = getIntent();
        EmailHolders = intent.getStringExtra("email");

        gblVar = GlobalVariable.getInstance();

        localEntity = new ArrayList<ProductData>();
        recordName = new String("");
        picNames = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.list_product);

        adapter = new VendorProductAdapter(this, localEntity,this);
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
                String id = jo.getString("id");
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
                ProductData e = new ProductData(id,picname,urlname,price,gst, minimum,hsncode,description,stock,reorderlevel);
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
                Toast.makeText(SelectVendorProducts.this, "Selected Rows\n" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(SelectVendorProducts.this,PO_List.class);

            ProductData productdata = localEntity.get(position);

            intent.putExtra("name",productdata.getTitle());
            intent.putExtra("price", productdata.getPrice());
            intent.putExtra("hsncode", productdata.getHsncode());
            intent.putExtra("gst", productdata.getGst());
            intent.putExtra("stock", productdata.getstock());
            intent.putExtra("description", productdata.getDescription());
            intent.putExtra("reorderlevel", productdata.getReorderlevel());

            setResult(RESULT_OK, intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
