package com.pcdgroup.hp.pcd_group.Quotation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.AdminLogin.BrandAdepter;
import com.pcdgroup.hp.pcd_group.AdminLogin.Category;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name CreateQuotation
 * @description create new quotation to send client
 */

public class CreateQuotation extends AppCompatActivity implements CallBackInterface {

    private Button add_client, addAddress, next;
    private TextView client,textaddres;
    GlobalVariable globalVariable;
    List<Category> categoriesList;
    String[] data;
    Intent intent;

    HashMap<String,String> hashMap = new HashMap<>();

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        /*
            - select date, brand, add brand, add product
            - create invoice to pdf view
        */

        globalVariable = GlobalVariable.getInstance();

        client = (TextView) findViewById(R.id.tv_client);
        textaddres = (TextView) findViewById(R.id.address);

        add_client = (Button) findViewById(R.id.btn_client);
        addAddress = (Button) findViewById(R.id.btn_address);

        next = (Button) findViewById(R.id.btn_next);

        urlQry = DataGetUrl.LIST_BRAND;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                CreateQuotation.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        // Client add in database
        add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateQuotation.this, SelectClient.class);

                startActivityForResult(intent, 1);
            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateQuotation.this, BrandList.class);

                startActivityForResult(intent, 2);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateQuotation.this, Quotation_product.class);

                //customer
                intent.putExtra("ClientInfo", globalVariable.globalClient);
                //brand
                intent.putExtra("SelectedBrand",globalVariable.globalBarnd);

                startActivity(intent);
                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Client Details
                    if (extras.containsKey("address")) {
                        globalVariable.globalClient[0] = extras.getString("address");
                        globalVariable.globalClient[1] = extras.getString("ad1");
                        globalVariable.globalClient[2] = extras.getString("ad2");
                        globalVariable.globalClient[3] = extras.getString("pin");
                        globalVariable.globalClient[4] = extras.getString("state");
                        globalVariable.globalClient[5] = extras.getString("country");
                        globalVariable.globalClient[6] = extras.getString("company");
                        globalVariable.globalClient[7] = extras.getString("fname");
                    }
                    client.setText(globalVariable.globalClient[7]);
                }
            }
        }
        if (requestCode == 2) {
                if(resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {

                        //Product Details
                        if (extras.containsKey("name")) {
                            globalVariable.globalBarnd[0] = extras.getString("name");
                            globalVariable.globalBarnd[1] = extras.getString("address");
                            globalVariable.globalBarnd[2] = extras.getString("address1");
                            globalVariable.globalBarnd[3] = extras.getString("address2");
                            globalVariable.globalBarnd[4] = extras.getString("pincode");
                            globalVariable.globalBarnd[5] = extras.getString("state");
                            globalVariable.globalBarnd[6] = extras.getString("mobileno");
                            globalVariable.globalBarnd[7] = extras.getString("email");
                            globalVariable.globalBarnd[8] = extras.getString("website");
                            globalVariable.globalBarnd[9] = extras.getString("pan");
                            globalVariable.globalBarnd[10] = extras.getString("gst");
                        }
                        textaddres.setText(globalVariable.globalBarnd[0]);
                    }
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.home) {
            if (globalVariable.AccessType.contains("Admin")) {

                intent = new Intent(this, AdminDashboard.class);

            }
            else if (globalVariable.AccessType.contains("Manager")) {

                intent = new Intent(this, AdminDashboard.class);

            }
            else if (globalVariable.AccessType.contains("Client")) {

                intent = new Intent(this, AdminDashboard.class);

            }
            else {

                intent = new Intent(this, ViewImage.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
        try {

            JSONArray ja = new JSONArray(response);
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
    protected void onDestroy() {
        super.onDestroy();
        add_client = null;
        addAddress = null;
        client = null;
        textaddres = null;
    }
}
