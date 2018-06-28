package com.pcdgroup.hp.pcd_group.OrderList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Quotation.Pdf;
import com.pcdgroup.hp.pcd_group.Quotation.PdfAdapter;
import com.pcdgroup.hp.pcd_group.Quotation.ViewInvoice;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name Order_List
 * @description order list is a quotation going in process
 */

public class Order_List extends AppCompatActivity implements CallBackInterface {

    ArrayList<Pdf> pdfList = new ArrayList<Pdf>();
    PdfAdapter billAdepter;
    HttpParse httpParse;
    ListView lstVeiw;
    Intent intent;
    String emailId;

    HashMap<String, String> hashMap = new HashMap<>();

    GlobalVariable globalVariable;

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);

        /*
            - display all order list
            - create order list to bill
        */

        httpParse = new HttpParse();

        globalVariable = GlobalVariable.getInstance();

        Intent intent = this.getIntent();
        emailId = intent.getStringExtra("emailid");

        lstVeiw = (ListView) findViewById(R.id.orderList);

        urlQry = DataGetUrl.ORDERLIST_DETAILS;
        typeOfQuery = CallType.POST_CALL;

        hashMap.put("billed","true");

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                Order_List.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        lstVeiw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String fileUrl = pdfList.get(position).getUrl();
            Log.v("File Url",fileUrl);

            Intent intent = new Intent(Order_List.this, ViewInvoice.class);
            intent.putExtra("FileUrl", fileUrl);
            intent.putExtra("Activity", "OrderList");
            startActivity(intent);

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_home,menu);
        super.onCreateOptionsMenu(menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.home) {
            if (globalVariable.AccessType.contains("Admin")) {

                intent = new Intent(Order_List.this, AdminDashboard.class);

            }
            else if (globalVariable.AccessType.contains("Manager")) {

                intent = new Intent(Order_List.this, AdminDashboard.class);

            }

            startActivity(intent);
           finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
        try {
            JSONObject obj = new JSONObject(response);
            Toast.makeText(Order_List.this,obj.getString("message"),
                    Toast.LENGTH_SHORT).show();

            JSONArray jsonArray = obj.getJSONArray("pdfs");

            for(int i=0; i < jsonArray.length() ; i++){

                //Declaring a json object corresponding to every pdf object in our json Array
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Declaring a Pdf object to add it to the ArrayList  pdfList
                Pdf pdf  = new Pdf();
                String pdfBill = jsonObject.getString("name");
                String url = jsonObject.getString("url");
//                        String pdfEmail = jsonObject.getString("email");
                pdf.setName(pdfBill);
                pdf.setEmail(emailId);
                pdf.setUrl(url);
                pdfList.add(pdf);
            }

            billAdepter = new PdfAdapter(Order_List.this,R.layout.list_layout, pdfList);
            lstVeiw.setAdapter(billAdepter);

            billAdepter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
