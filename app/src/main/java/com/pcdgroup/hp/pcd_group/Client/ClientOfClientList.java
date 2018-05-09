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

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ClientOfClientList extends AppCompatActivity {


    String HttpURL = "http://dert.co.in/gFiles/DataClient.php";

    ListView listView;
    ProgressDialog progressDialog;
    String emailId;
    String finalResult;

    ArrayList<Pdf> ClientList = new ArrayList<Pdf>();

    PdfAdapter pdfAdapter;

    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    HttpParse httpParse;

    HashMap<String, String> hashMap = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientofclient);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClientOfClientList.this, ClientRegisterActivity.class);

                startActivity(intent);
            }
        });

        httpParse = new HttpParse();

        listView = (ListView) findViewById(R.id.listView);

        progressDialog = new ProgressDialog(this);

        // intent
        Intent intent = getIntent();
        emailId = intent.getStringExtra("emailid");

        GetPdfList(emailId);
    }

    // Method to Get the Invoice List
    public void GetPdfList(final String ClientID) {

        class GetPdfList extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                try {
                    JSONObject obj = new JSONObject(httpResponseMsg);
                    Toast.makeText(ClientOfClientList.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

                    JSONArray jsonArray = obj.getJSONArray("g_client_details");

                    for(int i=0;i<jsonArray.length();i++){

                        //Declaring a json object corresponding to every pdf object in our json Array
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        DataAdapter GetData = new DataAdapter();
                        GetData.setId(jsonObject.getString("id"));
                        GetData.setName(jsonObject.getString("name"));
                        GetData.setType(jsonObject.getString("type"));
                        GetData.setAddress(jsonObject.getString("address"));
                        GetData.setaddresline1(jsonObject.getString("addressline1"));
                        GetData.setAddressline2(jsonObject.getString("addressline2"));
                        GetData.setMobileno(jsonObject.getString("mobileno"));
                        GetData.setState(jsonObject.getString("state"));
                        GetData.setCountry(jsonObject.getString("country"));
                        GetData.setCompanyname(jsonObject.getString("company"));
                        GetData.setPin(jsonObject.getString( "pin"));
                        GetData.setEmailid(jsonObject.getString("email_id"));
                        GetData.setDesignation(jsonObject.getString("designation"));


                    }

                    pdfAdapter=new PdfAdapter(ClientOfClientList.this,R.layout.list_layout, ClientList);

                    listView.setAdapter(pdfAdapter);

                    pdfAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                // Sending Client id.
                hashMap.put("emailId", emailId);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        GetPdfList GetPdfList = new GetPdfList();

        GetPdfList.execute(ClientID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;

            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }
}
