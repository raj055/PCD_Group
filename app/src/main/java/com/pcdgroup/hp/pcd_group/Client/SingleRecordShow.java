package com.pcdgroup.hp.pcd_group.Client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.VendorDealer.DealerList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name SingleRecordShow
 * @description show single record of client
 */

public class SingleRecordShow extends AppCompatActivity implements CallBackInterface {

    HashMap<String, String> hashMap = new HashMap<>();
    HashMap<String, String> ResultHash = new HashMap<>();
    String FinalJSonObject;
    String IdHolder, fNameHolder, lNameHolder, AddressHolder, Address1Holder, Address2Holder, MobileHolder, StateHolder, CountryHolder,
            PinHolder, CompnyHolder, EmailHolder, DesignationHolder;
    Button UpdateButton, DeleteButton, DealerAssign;

    Intent intent;
    GlobalVariable gblVar;

    TextView TextViewName, TextViewNameL, TextViewAddress, TextviewAddressline1, TextviewAddressline2,
                    TextviewMobileno, TextviewState, TextviewCountry, TextViewCompanyName, TextviewPin,
                    TextViewEmailID, TextViewDesignation;

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record);

        /*
            - update button to update record and delete record
            - selected record details show
        */

        gblVar = GlobalVariable.getInstance();

        TextViewName = (TextView) findViewById(R.id.tvname);
        TextViewNameL = (TextView) findViewById(R.id.tvnamel);
        TextViewAddress = (TextView) findViewById(R.id.tvaddress);
        TextviewAddressline1 = (TextView) findViewById(R.id.tvaddressline1);
        TextviewAddressline2 = (TextView) findViewById(R.id.tvaddressline2);
        TextviewMobileno = (TextView) findViewById(R.id.tv_Mobileno);
        TextviewState = (TextView) findViewById(R.id.tv_state);
        TextviewCountry = (TextView) findViewById(R.id.tvCountry);
        TextViewCompanyName = (TextView) findViewById(R.id.tv_companyname);
        TextviewPin = (TextView) findViewById(R.id.tvpin);
        TextViewEmailID = (TextView) findViewById(R.id.tvemailid);
        TextViewDesignation = (TextView) findViewById(R.id.tvdesignation);

        UpdateButton = (Button) findViewById(R.id.buttonUpdate);
        DeleteButton = (Button) findViewById(R.id.buttonDelete);
        DealerAssign = (Button) findViewById(R.id.buttonDealerAssign);

        //Receiving the ListView Clicked item value send by previous activity.
        IdHolder = getIntent().getStringExtra("id");
        fNameHolder = getIntent().getStringExtra("first_name");
        lNameHolder = getIntent().getStringExtra("last_name");
        AddressHolder = getIntent().getStringExtra("address");
        Address1Holder = getIntent().getStringExtra("address_line1");
        Address2Holder = getIntent().getStringExtra("address_line2");
        MobileHolder = getIntent().getStringExtra("mobile_num");
        StateHolder = getIntent().getStringExtra("state");
        CountryHolder = getIntent().getStringExtra("country");
        PinHolder = getIntent().getStringExtra("pin");
        CompnyHolder = getIntent().getStringExtra("company_name");
        EmailHolder = getIntent().getStringExtra("email_id");
        DesignationHolder = getIntent().getStringExtra("designation");

        TextViewName.setText(fNameHolder);
        TextViewNameL.setText(lNameHolder);
        TextViewAddress.setText(AddressHolder);
        TextviewAddressline1.setText(Address1Holder);
        TextviewAddressline2.setText(Address2Holder);
        TextviewMobileno.setText(MobileHolder);
        TextviewState.setText(StateHolder);
        TextviewCountry.setText(CountryHolder);
        TextviewPin.setText(PinHolder);
        TextViewCompanyName.setText(CompnyHolder);
        TextViewEmailID.setText(EmailHolder);
        TextViewDesignation.setText(DesignationHolder);

        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SingleRecordShow.this, UpdateActivity.class);

                // Sending Client Id, Name, Number and Class to next UpdateActivity.
                intent.putExtra("id", IdHolder);
                intent.putExtra("first_name", fNameHolder);
                intent.putExtra("last_name", lNameHolder);
                intent.putExtra("address", AddressHolder);
                intent.putExtra("address_line1", Address1Holder);
                intent.putExtra("address_line2", Address2Holder);
                intent.putExtra("mobile_num", MobileHolder);
                intent.putExtra("state", StateHolder);
                intent.putExtra("country", CountryHolder);
                intent.putExtra("pin", PinHolder);
                intent.putExtra("company_name", CompnyHolder);
                intent.putExtra("email_id", EmailHolder);
                intent.putExtra("designation", DesignationHolder);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);

                // Finishing current activity after opening next activity.
                finish();
            }
        });

        // Add Click listener on Delete button.
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                urlQry = DataGetUrl.SINGLE_DELETE;
                typeOfQuery = CallType.POST_CALL;

                hashMap.put("id", IdHolder);

                //Send Database query for inquiring to the database.
                dataBaseQuery = new DataBaseQuery(hashMap,
                        urlQry,
                        typeOfQuery,
                        getApplicationContext(),
                        SingleRecordShow.this
                );
                //Prepare for the database query
                dataBaseQuery.PrepareForQuery();

                intent = new Intent(SingleRecordShow.this, ClientDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Add Click listener on DealerAssign button.
        DealerAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SingleRecordShow.this, DealerList.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home) {
            if (gblVar.AccessType.contains("Admin")) {

                intent = new Intent(this, AdminDashboard.class);

            } else if (gblVar.AccessType.contains("Manager")) {

                intent = new Intent(this, AdminDashboard.class);

            } else if (gblVar.AccessType.contains("Client")) {

                intent = new Intent(this, AdminDashboard.class);

            } else {

                intent = new Intent(this, ViewImage.class);
            }

            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ExecuteQueryResult(String response, DataGetUrl dataGetUrl) {

        Toast.makeText(SingleRecordShow.this, response.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TextViewName = null;
        TextViewNameL = null;
        TextViewAddress = null;
        TextviewAddressline1 = null;
        TextviewAddressline2 = null;
        TextviewMobileno = null;
        TextviewState = null;
        TextviewCountry = null;
        TextViewCompanyName = null;
        TextviewPin = null;
        TextViewEmailID = null;
        TextViewDesignation = null;
        UpdateButton = null;
        DeleteButton = null;
        DealerAssign = null;
    }
}