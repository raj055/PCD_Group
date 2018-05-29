package com.pcdgroup.hp.pcd_group.VendorDealer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pcdgroup.hp.pcd_group.Quotation.ProdactEntity;
import com.pcdgroup.hp.pcd_group.Quotation.ProductCustomListAdapter;
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

public class Tab1vendorFragment extends Fragment {

    ListView listView;
    VendorDealerAdapter adapter;
    String HttpURL = "http://dert.co.in/gFiles/getvendor.php";
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;
    ArrayList<String> picNames;
    String recordName;
    List<DataVendorDealer> Entities;
    List<String> IdList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vendor,container,false);

        Entities = new ArrayList<DataVendorDealer>();
        recordName = new String("");
        picNames = new ArrayList<String>();
        listView = (ListView) view.findViewById(R.id.list_vendor);

        adapter = new VendorDealerAdapter(this,Entities);
        listView.setAdapter(adapter);

        //Adding ListView Item click Listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        //Retrieve
        getData();

        //Adepter
        adapter.notifyDataSetChanged();

        return view;
    }

    private void getData(){

        try {
            URL url = new URL(HttpURL);
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
                String type = jo.getString("type");
                Integer address = jo.getInt("address");
                Integer area = jo.getInt("area");
                Integer state=jo.getInt("state");
                Integer emailid=jo.getInt("email");
                Integer mobileno=jo.getInt("mobileno");
                String organisation=jo.getString("organisation");
                Integer gst=jo.getInt("gstno");
                Integer designation=jo.getInt("designation");
                Integer id = jo.getInt("id");

                // Adding Student Id TO IdList Array.
                IdList.add(jo.getString("id").toString());

                DataVendorDealer e = new DataVendorDealer(name,type,address, area,state,gst,emailid,mobileno,organisation,id,designation);
                Entities.add(e);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
