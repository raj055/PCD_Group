package com.pcdgroup.hp.pcd_group.VendorDealer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AccessAdmin;
import com.pcdgroup.hp.pcd_group.AdminLogin.AdminSetting;
import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.SingleRecordShow;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
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
 * @class_name DealerList
 * @description dealer list show in data base
 */

public class DealerList extends AppCompatActivity implements CallBackInterface {

    ListView listView;
    DealerListAdapter adapter;
    List<DealerData> localdata;
    String[] data;
    HashMap<String,String> hashMap = new HashMap<>();
    String emailId;
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealer_list);

        /*
            - shoe list of data to dealer
         */

        listView = (ListView) findViewById(R.id.listViewDealer);

        localdata = new ArrayList<DealerData>();

        adapter = new DealerListAdapter(this, localdata, this);
        listView.setAdapter(adapter);

        urlQry = DataGetUrl.DEALER_LIST;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                DealerList.this
        );
        //Prepare for the database query-
        dataBaseQuery.PrepareForQuery();

        // intent
        Intent intent = getIntent();
        emailId = intent.getStringExtra("emailid");

        //setting listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(DealerList.this, SingleRecordShow.class);

                urlQry = DataGetUrl.ASSIGN_DEALER;
                typeOfQuery = CallType.POST_CALL;

                hashMap.put("email", emailId);

                //Send Database query for inquiring to the database.
                dataBaseQuery = new DataBaseQuery(hashMap,
                        urlQry,
                        typeOfQuery,
                        getApplicationContext(),
                        DealerList.this
                );
                //Prepare for the database query
                dataBaseQuery.PrepareForQuery();

                Toast.makeText(DealerList.this,"Dealor Assign Successfully",Toast.LENGTH_LONG).toString();

                startActivity(intent);
            }
        });
    }
    /** CallBack Function for processing the Database query result.
     * @param  response - Response string received while database query.
     *         dataGetUrl - Url queried.*/
    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {

        if (dataGetUrl.equals(DataGetUrl.DEALER_LIST)) {

            try {

                JSONArray ja = new JSONArray(response);
                JSONObject jo = null;

                data = new String[ja.length()];

                for (int i = 0; i < ja.length(); i++) {

                    jo = ja.getJSONObject(i);
                    String id = jo.getString("id");
                    String name = jo.getString("name");
                    String address = jo.getString("address");
                    String area = jo.getString("location");
                    String state = jo.getString("state");
                    String email = jo.getString("email");
                    String mobile = jo.getString("mobileno");
                    String organisation = jo.getString("organisation");
                    String gst = jo.getString("gstno");

                    DealerData data = new DealerData(id, name, address, area, state, email, mobile, organisation, gst);
                    localdata.add(data);

                }

                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {

            Toast.makeText(DealerList.this, response.toString(), Toast.LENGTH_LONG).show();
        }
    }
    /** Releases the memory of all the components after intent finishes. */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        listView = null;
    }

}
