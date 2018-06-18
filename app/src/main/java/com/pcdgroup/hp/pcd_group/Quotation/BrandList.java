package com.pcdgroup.hp.pcd_group.Quotation;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class BrandList  extends AppCompatActivity implements CallBackInterface {

    ListView listView;
    String HttpURL_get = "http://dert.co.in/gFiles/listbrands.php";

    List<Category> categoriesList;
    BrandAdepter adepter;
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brandlist);

        listView = (ListView) findViewById(R.id.list_address);

        categoriesList = new ArrayList<Category>();

        adepter = new BrandAdepter(this, categoriesList);
        listView.setAdapter(adepter);
        adepter.notifyDataSetChanged();

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

        //Retrieve
        getData();

    }

    private void getData(){

        try {
            URL url = new URL(HttpURL_get);
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
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void ExecuteQueryResult(String response) {

    }
}
