package com.pcdgroup.hp.pcd_group.Client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;

import java.util.HashMap;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ClientRegisterActivity extends AppCompatActivity {

    EditText fname,lname,address,addressline1,addressline2,mobileno,country,company_name,pin,email_id,designation;
    Spinner type,state;
    Button submit;
    String FName_Holder, LName_Holder, type_Holder, Address_Hoder,Addressline1_Holder,Addressline2_Holder,Mobileno_Holder,
            State_Holder,Country_Holder, CompanyName_Holder,Pin_Holder, Emailid_Holder, Designation_Holder,
            UserClient_Holder;
    String finalResult;
    String HttpURL = "http://dert.co.in/gFiles/ClientRegister.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Intent intent;
    GlobalVariable globalVariable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientregister);

        globalVariable = GlobalVariable.getInstance();

        //Assign Id'S
        fname = (EditText) findViewById(R.id.et_fname);
        lname = (EditText) findViewById(R.id.et_lname);
        type = (Spinner) findViewById(R.id.spinner);
        state = (Spinner) findViewById(R.id.spinner2);
        address = (EditText) findViewById(R.id.et_address);
        addressline1 = (EditText) findViewById(R.id.et_addressline1);
        addressline2 = (EditText) findViewById(R.id.et_addressline2);
        mobileno=(EditText) findViewById(R.id.et_Mobileno);
        country=(EditText) findViewById(R.id.et_Country);
        company_name = (EditText) findViewById(R.id.et_companyname);
        pin=(EditText) findViewById(R.id.et_Pin);
        email_id = (EditText) findViewById(R.id.et_email);
        designation = (EditText) findViewById(R.id.et_designation);
        submit = (Button) findViewById(R.id.btn_submit);

        //Adding Click Listener on button.
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not.
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    UserRegisterFunction(FName_Holder,LName_Holder, type_Holder, Address_Hoder, Addressline1_Holder,
                            Addressline2_Holder,Mobileno_Holder,State_Holder,Country_Holder,
                            CompanyName_Holder,Pin_Holder, Emailid_Holder,Designation_Holder,UserClient_Holder);
                }
                else {

                    // If EditText is empty then this block will execute.
                    Toast.makeText(ClientRegisterActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot(){

        //Checking all EditText Empty or Not.
        FName_Holder = fname.getText().toString();
        LName_Holder = lname.getText().toString();
        type_Holder = type.getSelectedItem().toString();
        Address_Hoder = address.getText().toString();
        Addressline1_Holder = addressline1.getText().toString();
        Addressline2_Holder = addressline2.getText().toString();
        Mobileno_Holder = mobileno.getText().toString();
        State_Holder=state.getSelectedItem().toString();
        Country_Holder=country.getText().toString();
        CompanyName_Holder = company_name.getText().toString();
        Emailid_Holder = email_id.getText().toString();
        Pin_Holder = pin.getText().toString();
        Designation_Holder = designation.getText().toString();

        FName_Holder = FName_Holder.replace("'","''");
        LName_Holder = LName_Holder.replace("'","''");
        Address_Hoder = Address_Hoder.replace("'","''");
        Addressline1_Holder = Addressline1_Holder.replace("'","''");
        Addressline2_Holder = Addressline2_Holder.replace("'","''");
        CompanyName_Holder = CompanyName_Holder.replace("'","''");
        Designation_Holder = Designation_Holder.replace("'","''");

        UserClient_Holder = globalVariable.currentUserEmail;

        if(TextUtils.isEmpty(FName_Holder) || TextUtils.isEmpty(LName_Holder) || TextUtils.isEmpty(type_Holder) || TextUtils.isEmpty(Address_Hoder) || TextUtils.isEmpty(CompanyName_Holder) || TextUtils.isEmpty(Emailid_Holder) || TextUtils.isEmpty(Designation_Holder))
        {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }

    //Register user in database details.
    public void UserRegisterFunction(final String fname, final String lname, final String type,
                                     final String address, final String addressline1,final String addressline2,
                                     final String mobileno,final String state,
                                     final String counntry, final String company_name,
                                     final  String pin,final String email_id, final String designation,
                                     final String userclient){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ClientRegisterActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(ClientRegisterActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            // Creating Packet database string to HashMap.
            @Override
            protected String doInBackground(String... params) {

                hashMap.put("first_name",params[0]);
                hashMap.put("last_name",params[1]);
                hashMap.put("type",params[2]);
                hashMap.put("address",params[3]);
                hashMap.put("address_line1",params[4]);
                hashMap.put("address_line2",params[5]);
                hashMap.put("mobile_num",params[6]);
                hashMap.put("state",params[7]);
                hashMap.put("country",params[8]);
                hashMap.put("company_name",params[9]);
                hashMap.put("pin",params[10]);
                hashMap.put("email_id",params[11]);
                hashMap.put("designation",params[12]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
            public void execute(String fname,String lname, String type, String address, String addressline1, String addressline2, String mobileno, EditText state, String counntry, String company_name, String pin, String email_id, String designation, String userclient) {
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(fname,lname,type,address,addressline1,addressline2,mobileno,state,counntry,company_name,pin,email_id,designation,userclient);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ClientRegisterActivity.this);
        builder.setMessage("Are You Sure Want To Exit Register ?");
        builder.setCancelable(true);
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (globalVariable.AccessType.contains("Admin")){
                    intent = new Intent(ClientRegisterActivity.this, ClientDetailsActivity.class);

                } else if (globalVariable.AccessType.contains("Manager")){
                    intent = new Intent(ClientRegisterActivity.this, ClientDetailsActivity.class);

                } else if (globalVariable.AccessType.contains("Client")){
                    intent = new Intent(ClientRegisterActivity.this, ClientOfClientList.class);
                    intent.putExtra("emailid", globalVariable.currentUserEmail);

                }

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
}
