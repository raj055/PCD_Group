package com.pcdgroup.hp.pcd_group.Vendor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.ClientOfClientList;
import com.pcdgroup.hp.pcd_group.Client.ClientRegisterActivity;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;

import java.util.HashMap;

public class Add_Vendor extends AppCompatActivity {

    EditText name,address,area,mobileno,state,email,shopname;
    Button submit;
    String Name_Holder, Address_Hoder, Area_Holder, Mobileno_Holder,State_Holder, Email_Holder, ShopName_Holder;
    String finalResult;
    String HttpURL = "http://dert.co.in/gFiles/vendor.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Intent intent;
    GlobalVariable globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        globalVariable = GlobalVariable.getInstance();

        //Assign Id'S
        name = (EditText) findViewById(R.id.name_et);
        state = (EditText) findViewById(R.id.state_et);
        address = (EditText) findViewById(R.id.address_et);
        area = (EditText) findViewById(R.id.area_et);
        state = (EditText) findViewById(R.id.state_et);
        email = (EditText) findViewById(R.id.email_et);
        mobileno=(EditText) findViewById(R.id.mobile_et);
        shopname=(EditText) findViewById(R.id.shopname_et);

        submit = (Button) findViewById(R.id.submit_btn);

        //Adding Click Listener on button.
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not.
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    UserRegisterFunction(Name_Holder, Address_Hoder, Area_Holder,State_Holder,
                                Email_Holder, Mobileno_Holder, ShopName_Holder);
                }
                else {

                    // If EditText is empty then this block will execute.
                    Toast.makeText(Add_Vendor.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public void CheckEditTextIsEmptyOrNot(){

        //Checking all EditText Empty or Not.
        Name_Holder = name.getText().toString();
        Address_Hoder = address.getText().toString();
        Area_Holder = area.getText().toString();
        State_Holder = state.getText().toString();
        Email_Holder = email.getText().toString();
        Mobileno_Holder = mobileno.getText().toString();
        ShopName_Holder = shopname.getText().toString();

        Name_Holder = Name_Holder.replace("'","''");
        Address_Hoder = Address_Hoder.replace("'","''");
        Area_Holder = Area_Holder.replace("'","''");
        State_Holder = State_Holder.replace("'","''");
        Email_Holder = Email_Holder.replace("'","''");
        ShopName_Holder = ShopName_Holder.replace("'","''");

        if(TextUtils.isEmpty(Name_Holder) || TextUtils.isEmpty(Email_Holder) || TextUtils.isEmpty(Mobileno_Holder))
        {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }

    //Register user in database details.
    public void UserRegisterFunction(final String name, final String address,final String area,
                                     final String state,final String email,
                                     final String mobileno, final String shopname){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Add_Vendor.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(Add_Vendor.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            // Creating Packet database string to HashMap.
            @Override
            protected String doInBackground(String... params) {

                hashMap.put("name",params[0]);
                hashMap.put("address",params[1]);
                hashMap.put("area",params[2]);
                hashMap.put("state",params[3]);
                hashMap.put("email",params[4]);
                hashMap.put("mobileno",params[5]);
                hashMap.put("shopname",params[6]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }

        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(name,address,area,state,email,mobileno,shopname);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Add_Vendor.this);
        builder.setMessage("Are You Sure Want To Exit Register ?");
        builder.setCancelable(true);
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (globalVariable.AccessType.contains("Admin")){
                    intent = new Intent(Add_Vendor.this, AdminDashboard.class);

                } else if (globalVariable.AccessType.contains("Manager")){
                    intent = new Intent(Add_Vendor.this, UserDashbord.class);

                } else if (globalVariable.AccessType.contains("Client")){
                    intent = new Intent(Add_Vendor.this, UserDashbord.class);
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
