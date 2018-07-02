package com.pcdgroup.hp.pcd_group.Client;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Quotation.Pdf;
import com.pcdgroup.hp.pcd_group.Quotation.PdfAdapter;
import com.pcdgroup.hp.pcd_group.Quotation.ShowQuotationList;
import com.pcdgroup.hp.pcd_group.Quotation.ViewInvoice;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.http.GET;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ClientOfClientList
 * @description client of client is client add there new  client list
 */

public class ClientOfClientList extends AppCompatActivity implements CallBackInterface {

    private ListView listView;
    private ProgressDialog progressDialog;
    private String emailId;
    String finalResult;

    ArrayList<DataAdapter> ClientList = new ArrayList<DataAdapter>();
    ClientAdepter clientAdepter;

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;
    HashMap<String, String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientofclient);

        /*
            - client add new client show data
        */

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClientOfClientList.this, ClientRegisterActivity.class);

                startActivity(intent);
                finish();
            }
        });


        listView = (ListView) findViewById(R.id.listView);

        finalResult = new String();
        progressDialog = new ProgressDialog(this);

        // intent
        Intent intent = getIntent();
        // Sending Client id.
        Bundle bundle = intent.getExtras();
        if(bundle!= null){
            if(bundle.getString("emailId") != null) {

                emailId = bundle.getString("emailId");
            }
            hashMap.put("emailId", emailId);
        }


        urlQry = DataGetUrl.CLIENT_LIST;

        typeOfQuery = CallType.POST_CALL;


        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
           urlQry,
          typeOfQuery,
          getApplicationContext(),
           ClientOfClientList.this
          );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

    }

    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
        JSONArray jsonArray = null;
        try {
            JSONObject obj = new JSONObject(response);
            jsonArray = obj.getJSONArray("client");

            for(int i=0;i<jsonArray.length();i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                DataAdapter GetData = new DataAdapter();

                GetData.setId(jsonObject.getString("id"));
                GetData.setfName(jsonObject.getString("first_name"));
                GetData.setlName(jsonObject.getString("last_name"));
                GetData.setType(jsonObject.getString("type"));
                GetData.setAddress(jsonObject.getString("address"));
                GetData.setaddresline1(jsonObject.getString("address_line1"));
                GetData.setAddressline2(jsonObject.getString("address_line2"));
                GetData.setMobileno(jsonObject.getString("mobile_num"));
                GetData.setState(jsonObject.getString("state"));
                GetData.setCountry(jsonObject.getString("country"));
                GetData.setCompanyname(jsonObject.getString("company"));
                GetData.setPin(jsonObject.getString("pin"));
                GetData.setEmailid(jsonObject.getString("email_id"));
                GetData.setDesignation(jsonObject.getString("designation"));

                ClientList.add(GetData);

            }

            clientAdepter=new ClientAdepter(ClientOfClientList.this, R.layout.cardview1, ClientList);

            listView.setAdapter(clientAdepter);

            clientAdepter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        clientAdepter=new ClientAdepter(ClientOfClientList.this,R.layout.cardview1, ClientList);

        listView.setAdapter(clientAdepter);

        clientAdepter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listView = null;
        progressDialog = null;
    }
}
