package com.pcdgroup.hp.pcd_group.Client;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.R;

import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name UpdateActivity
 * @description update client information
 */

public class UpdateActivity extends AppCompatActivity implements CallBackInterface {

    Boolean CheckEditText;
    HashMap<String,String> hashMap = new HashMap<>();
    EditText ClientName, ClientNameL, ClientAddress,ClientAddressline1,ClientAddressline2,ClientMobileno,ClientState,
            ClientCountry, ClientEmail, ClientCompany,ClientPin, ClientDesignation;
    Button UpdateStudent;
    String ClientIdHolder,ClientFNameHolder,ClientLNameHolder,ClientAddressHolder,ClientAddressline1Holder,ClientAddressline2Holder,
            ClientMobilenoHolder,ClientStateHolder,ClientCountryHolder, ClientEmailHolder,ClientPinoHolder,
            ClientComapnyHolder, ClientDesignationHolder;
    Intent intent;
    GlobalVariable gblVar;

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        /*
            - update all client field
        */

        gblVar = GlobalVariable.getInstance();

        ClientName = (EditText)findViewById(R.id.editName);
        ClientNameL = (EditText)findViewById(R.id.editNamel);
        ClientAddress = (EditText)findViewById(R.id.editAddress);
        ClientAddressline1 = (EditText)findViewById(R.id.editAddressline1);
        ClientAddressline2 = (EditText)findViewById(R.id.editAddressline2);
        ClientMobileno = (EditText)findViewById(R.id.editMobileno);
        ClientState = (EditText)findViewById(R.id.editState);
        ClientCountry = (EditText)findViewById(R.id.editCountry);
        ClientEmail = (EditText)findViewById(R.id.editEmail);
        ClientPin= (EditText)findViewById(R.id.editPin);
        ClientCompany = (EditText)findViewById(R.id.editCompany);
        ClientDesignation = (EditText)findViewById(R.id.editDesignation);

        UpdateStudent = (Button)findViewById(R.id.UpdateButton);

        // Receive Client ID, Name , Address , Email, etc.. Send by previous ShowSingleRecordActivity.
        ClientIdHolder = getIntent().getStringExtra("id");
        ClientFNameHolder = getIntent().getStringExtra("first_name");
        ClientLNameHolder = getIntent().getStringExtra("last_name");
        ClientAddressHolder = getIntent().getStringExtra("address");
        ClientAddressline1Holder = getIntent().getStringExtra("address_line1");
        ClientAddressline2Holder = getIntent().getStringExtra("address_line2");
        ClientMobilenoHolder = getIntent().getStringExtra("mobile_num");
        ClientStateHolder = getIntent().getStringExtra("state");
        ClientCountryHolder = getIntent().getStringExtra("country");
        ClientPinoHolder = getIntent().getStringExtra("pin");
        ClientComapnyHolder = getIntent().getStringExtra("company");
        ClientEmailHolder = getIntent().getStringExtra("email_id");
        ClientDesignationHolder = getIntent().getStringExtra("designation");

        // Setting Received Student Name, Phone Number, Class into EditText.
        ClientName.setText(ClientFNameHolder);
        ClientNameL.setText(ClientLNameHolder);
        ClientAddress.setText(ClientAddressHolder);
        ClientAddressline1.setText(ClientAddressline1Holder);
        ClientAddressline2.setText(ClientAddressline2Holder);
        ClientMobileno.setText(ClientMobilenoHolder);
        ClientState.setText(ClientStateHolder);
        ClientCountry.setText(ClientCountryHolder);
        ClientEmail.setText(ClientEmailHolder);
        ClientPin.setText(ClientPinoHolder);
        ClientCompany.setText(ClientComapnyHolder);
        ClientDesignation.setText(ClientDesignationHolder);

        // Adding click listener to update button .
        UpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting data from EditText after button click.
                GetDataFromEditText();

                if(CheckEditText){

                    urlQry = DataGetUrl.EDIT_CLIENT;
                    typeOfQuery = CallType.POST_CALL;

                    hashMap.put("id",ClientIdHolder);

                    hashMap.put("first_name",ClientFNameHolder);

                    hashMap.put("last_name",ClientLNameHolder);

                    hashMap.put("address",ClientAddressHolder);

                    hashMap.put("address_line1",ClientAddressline1Holder);

                    hashMap.put("address_line2",ClientAddressline2Holder);

                    hashMap.put("mobile_num",ClientMobilenoHolder);

                    hashMap.put("state",ClientStateHolder);

                    hashMap.put("country",ClientCountryHolder);

                    hashMap.put("email_id",ClientEmailHolder);

                    hashMap.put("pin",ClientPinoHolder);

                    hashMap.put("company",ClientComapnyHolder);

                    hashMap.put("designation",ClientDesignationHolder);

                    //Send Database query for inquiring to the database.
                    dataBaseQuery = new DataBaseQuery(hashMap,
                            urlQry,
                            typeOfQuery,
                            getApplicationContext(),
                            UpdateActivity.this
                    );
                    //Prepare for the database query
                    dataBaseQuery.PrepareForQuery();

                }else {

                    // If EditText is empty then this block will execute.
                    Toast.makeText(UpdateActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    // Method to get existing data from EditText.
    public void GetDataFromEditText(){

        ClientFNameHolder = ClientName.getText().toString();
        ClientLNameHolder = ClientNameL.getText().toString();
        ClientAddressHolder = ClientAddress.getText().toString();
        ClientAddressline1Holder = ClientAddressline1.getText().toString();
        ClientAddressline2Holder = ClientAddressline2.getText().toString();
        ClientMobilenoHolder = ClientMobileno.getText().toString();
        ClientStateHolder = ClientState.getText().toString();
        ClientCountryHolder = ClientCountry.getText().toString();
        ClientEmailHolder = ClientEmail.getText().toString();
        ClientPinoHolder = ClientPin.getText().toString();
        ClientComapnyHolder = ClientCompany.getText().toString();
        ClientDesignationHolder = ClientDesignation.getText().toString();

        ClientFNameHolder = ClientFNameHolder.replace("'","''");
        ClientLNameHolder = ClientLNameHolder.replace("'","''");
        ClientAddressHolder = ClientAddressHolder.replace("'","''");
        ClientAddressline1Holder = ClientAddressline1Holder.replace("'","''");
        ClientAddressline2Holder = ClientAddressline2Holder.replace("'","''");
        ClientComapnyHolder = ClientComapnyHolder.replace("'","''");
        ClientDesignationHolder = ClientDesignationHolder.replace("'","''");

        if(TextUtils.isEmpty(ClientFNameHolder) || TextUtils.isEmpty(ClientLNameHolder) || TextUtils.isEmpty(ClientAddressHolder) || TextUtils.isEmpty(ClientAddressline1Holder)
                || TextUtils.isEmpty(ClientAddressline2Holder) || TextUtils.isEmpty(ClientMobilenoHolder)
                || TextUtils.isEmpty(ClientStateHolder) || TextUtils.isEmpty(ClientCountryHolder)
                || TextUtils.isEmpty(ClientEmailHolder)|| TextUtils.isEmpty(ClientPinoHolder)
                || TextUtils.isEmpty(ClientComapnyHolder) || TextUtils.isEmpty(ClientDesignationHolder))
        {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
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
            if (gblVar.AccessType.contains("Admin")) {

                intent = new Intent(this, AdminDashboard.class);

            }
            else if (gblVar.AccessType.contains("Manager")) {

                intent = new Intent(this, AdminDashboard.class);

            }
            else if (gblVar.AccessType.contains("Client")) {

                intent = new Intent(this, AdminDashboard.class);

            }
            else {

                intent = new Intent(this, ViewImage.class);
            }

            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setMessage("Are You Sure Want To Exit ?");
        builder.setCancelable(true);
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UpdateActivity.this, ClientDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
        Toast.makeText(UpdateActivity.this,response.toString(), Toast.LENGTH_LONG).show();
    }
}
