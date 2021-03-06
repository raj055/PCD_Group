package com.pcdgroup.hp.pcd_group.Client;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.R;

import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ClientRegisterActivity
 * @description add new client register in database
 */

public class ClientRegisterActivity extends AppCompatActivity implements CallBackInterface {

    //EditText for Client details
    EditText fname, lname, address, addressline1, addressline2, mobileno, country, company_name,
            pin, email_id, password, designation;
    Spinner type, state;

    //String to store the text for
    String FName_Holder, LName_Holder, type_Holder, Address_Hoder, Addressline1_Holder, Addressline2_Holder, Mobileno_Holder,
            State_Holder, Country_Holder, CompanyName_Holder, Pin_Holder, Emailid_Holder, Password_Holder, Designation_Holder,
            UserClient_Holder;

    //Check edit text
    Boolean CheckEditText;

    //Database query components.
    HashMap<String, String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    Intent intent;
    GlobalVariable globalVariable;

    /** Populates the screen for client registration. Add user/client on registration.
     * @param savedInstanceState object of passing parameters from the previous intent */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientregister);

        globalVariable = GlobalVariable.getInstance();

        //All the client registration components.
        fname = (EditText) findViewById(R.id.et_fname);
        lname = (EditText) findViewById(R.id.et_lname);
        type = (Spinner) findViewById(R.id.spinner);
        state = (Spinner) findViewById(R.id.spinner2);
        address = (EditText) findViewById(R.id.et_address);
        addressline1 = (EditText) findViewById(R.id.et_addressline1);
        addressline2 = (EditText) findViewById(R.id.et_addressline2);
        mobileno = (EditText) findViewById(R.id.et_Mobileno);
        country = (EditText) findViewById(R.id.et_Country);
        company_name = (EditText) findViewById(R.id.et_companyname);
        pin = (EditText) findViewById(R.id.et_Pin);
        email_id = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        designation = (EditText) findViewById(R.id.et_designation);
    }

    /** On clicking the button register, send registration details.
     * Send database query for adding the registration details.
     * @param view View*/
    public void onClickRegister(View view) {

        // Checking whether EditText is Empty or Not.
        CheckEditTextIsEmptyOrNot();

        if (CheckEditText) {

            // If EditText is not empty and CheckEditText = True then this block will execute.
            urlQry = DataGetUrl.CLIENT_REGISTER;

            typeOfQuery = CallType.POST_CALL;

            //Add the parameters to hashmap for sending to database.
            hashMap.put("first_name", FName_Holder);
            hashMap.put("last_name", LName_Holder);
            hashMap.put("type", type_Holder);
            hashMap.put("address", Address_Hoder);
            hashMap.put("address_line1", Addressline1_Holder);
            hashMap.put("address_line2", Addressline2_Holder);
            hashMap.put("mobile_num", Mobileno_Holder);
            hashMap.put("state", State_Holder);
            hashMap.put("country", Country_Holder);
            hashMap.put("company_name", CompanyName_Holder);
            hashMap.put("pin", Pin_Holder);
            hashMap.put("email_id", Emailid_Holder);
            hashMap.put("password", Password_Holder);
            hashMap.put("designation", Designation_Holder);
            hashMap.put("user", UserClient_Holder);

            //Send Database query for inquiring to the database.
            dataBaseQuery = new DataBaseQuery(hashMap,
                    urlQry,
                    typeOfQuery,
                    getApplicationContext(),
                    ClientRegisterActivity.this
            );
            //Prepare for the database query
            dataBaseQuery.PrepareForQuery();
        } else {
            // If EditText is empty then this block will execute.
            Toast.makeText(ClientRegisterActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
        }
    }
    /** Check if all the fields of registration are filled. */
    public void CheckEditTextIsEmptyOrNot() {

        //Checking all EditText Empty or Not.
        FName_Holder = fname.getText().toString();
        LName_Holder = lname.getText().toString();
        type_Holder = type.getSelectedItem().toString();
        Address_Hoder = address.getText().toString();
        Addressline1_Holder = addressline1.getText().toString();
        Addressline2_Holder = addressline2.getText().toString();
        Mobileno_Holder = mobileno.getText().toString();
        State_Holder = state.getSelectedItem().toString();
        Country_Holder = country.getText().toString();
        CompanyName_Holder = company_name.getText().toString();
        Emailid_Holder = email_id.getText().toString();
        Password_Holder = password.getText().toString();
        Pin_Holder = pin.getText().toString();
        Designation_Holder = designation.getText().toString();

        FName_Holder = FName_Holder.replace("'", "''");
        LName_Holder = LName_Holder.replace("'", "''");
        Address_Hoder = Address_Hoder.replace("'", "''");
        Addressline1_Holder = Addressline1_Holder.replace("'", "''");
        Addressline2_Holder = Addressline2_Holder.replace("'", "''");
        CompanyName_Holder = CompanyName_Holder.replace("'", "''");
        Designation_Holder = Designation_Holder.replace("'", "''");

        UserClient_Holder = globalVariable.currentUserEmail;

        if (TextUtils.isEmpty(FName_Holder) || TextUtils.isEmpty(LName_Holder) ||
                TextUtils.isEmpty(type_Holder) || TextUtils.isEmpty(Address_Hoder) ||
                TextUtils.isEmpty(CompanyName_Holder) || TextUtils.isEmpty(Emailid_Holder) ||
                TextUtils.isEmpty(Password_Holder) || TextUtils.isEmpty(Designation_Holder)) {

            CheckEditText = false;
        } else {
            CheckEditText = true;
        }
    }

    /** Go back to the client details activity on back press. */
    @Override
    public void onBackPressed() {

        //Show the dialog and recheck if user wants to exit.
        final AlertDialog.Builder builder = new AlertDialog.Builder(ClientRegisterActivity.this);
        builder.setMessage("Are You Sure Want To Exit Register ?");
        builder.setCancelable(true);

        //Check the type of user logged in and go to the relevant activity.
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (globalVariable.AccessType.contains("Admin")) {
                    intent = new Intent(ClientRegisterActivity.this, ClientDetailsActivity.class);

                } else if (globalVariable.AccessType.contains("Manager")) {
                    intent = new Intent(ClientRegisterActivity.this, ClientDetailsActivity.class);

                } else if (globalVariable.AccessType.contains("Client")) {
                    intent = new Intent(ClientRegisterActivity.this, ClientOfClientList.class);
                    intent.putExtra("emailid", globalVariable.currentUserEmail);

                }

                //Start the next activity.
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //Build the dialog.
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    /** CallBack Function for processing the Database query result.
     * @param  response - Response string received while database query.
     *         dataGetUrl - Url queried.*/
    @Override
    public void ExecuteQueryResult(String response, DataGetUrl dataGetUrl) {
        Toast.makeText(ClientRegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
    }
    /** Releases the memory of all the components after intent finishes. */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        fname = null;
        lname = null;
        address = null;
        addressline1 = null;
        addressline2 = null;
        mobileno = null;
        country = null;
        company_name = null;
        pin = null;
        email_id = null;
        password = null;
        designation = null;
        state = null;
        type = null;
    }
}