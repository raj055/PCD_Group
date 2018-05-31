package com.pcdgroup.hp.pcd_group.VendorDealer;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.SingleRecordShow;
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

public class DealerList extends AppCompatActivity {

    String FETCH_URL = "http://dert.co.in/gFiles/dealerlist.php";
    ListView listView;
    DealerListAdapter adapter;
    List<DealerData> localdata;
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealer_list);

        listView = (ListView) findViewById(R.id.listViewDealer);

        localdata = new ArrayList<DealerData>();

        adapter = new DealerListAdapter(this, localdata, this);
        listView.setAdapter(adapter);

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        getPdfs();

        adapter.notifyDataSetChanged();

        //setting listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(DealerList.this, ClientDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getPdfs() {

        try {
            URL url = new URL(FETCH_URL);
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
                String name = jo.getString("name");
                String address = jo.getString("address");
                String area = jo.getString("location");
                String state = jo.getString("state");
                String email = jo.getString("email");
                String mobile = jo.getString("mobileno");
                String organisation = jo.getString("organisation");
                String gst = jo.getString("gstno");

                DealerData data= new DealerData(id,name,address,area,state,email,mobile,organisation,gst);
                localdata.add(data);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
