package com.pcdgroup.hp.pcd_group.AdminLogin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.Client.ClientRegisterActivity;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.Quotation.CreateQuotation;
import com.pcdgroup.hp.pcd_group.R;

import java.util.HashMap;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class AdminSetting extends AppCompatActivity {

    Button Brand;
    EditText brandname, address, address1, address2,Pincode,mobile,email,website,pan,gst;
    Spinner state;

    Boolean CheckEditText;
    String Name_Holder, Address_Hoder,Addressline1_Holder,Addressline2_Holder,Mobileno_Holder,State_Holder,Pin_Holder, Emailid_Holder, Website_Holde, Pan_Holde, GST_Holder;
    String finalResult;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    String HttpURL = "http://pcddata-001-site1.1tempurl.com/brandadd.php";

    String HttpURL_get = "http://pcddata-001-site1.1tempurl.com/listbrands.php";

    View promptUserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsetting);

        LayoutInflater layoutinflater = LayoutInflater.from(this);
        promptUserView = layoutinflater.inflate(R.layout.brand_dialog_box, null);

        brandname = (EditText) promptUserView.findViewById(R.id.brnadname);
        address = (EditText) promptUserView.findViewById(R.id.editText);
        address1 = (EditText) promptUserView.findViewById(R.id.editText2);
        address2 = (EditText) promptUserView.findViewById(R.id.editText3);
        Pincode = (EditText) promptUserView.findViewById(R.id.editText4);
        mobile = (EditText) promptUserView.findViewById(R.id.editText5);
        email = (EditText) promptUserView.findViewById(R.id.editText6);
        website = (EditText) promptUserView.findViewById(R.id.editText7);
        pan = (EditText) promptUserView.findViewById(R.id.editText9);
        gst = (EditText) promptUserView.findViewById(R.id.editText8);

        state = (Spinner)  promptUserView.findViewById(R.id.spinner6);

        Brand = (Button) findViewById(R.id.btn_brand);

        // Click Brand button
        Brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckAnyAddress();

                CheckEditTextIsEmptyOrNot();
            }
        });

    }

    public void CheckEditTextIsEmptyOrNot(){

        if(TextUtils.isEmpty(Name_Holder) || TextUtils.isEmpty(Address_Hoder) || TextUtils.isEmpty(Addressline1_Holder) ||
                TextUtils.isEmpty(Addressline2_Holder) || TextUtils.isEmpty(Emailid_Holder) ||
                TextUtils.isEmpty(Mobileno_Holder) || TextUtils.isEmpty(Pin_Holder) ||
                 TextUtils.isEmpty(Pan_Holde) || TextUtils.isEmpty(Website_Holde) || TextUtils.isEmpty(GST_Holder))
        {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }

    }

    private void CheckAnyAddress() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptUserView);

        alertDialogBuilder.setTitle("Enter Brand Details.");

        // prompt for username
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //Checking all EditText Empty or Not.
                Name_Holder = brandname.getText().toString();
                Address_Hoder = address.getText().toString();
                Addressline1_Holder = address1.getText().toString();
                Addressline2_Holder = address2.getText().toString();
                Mobileno_Holder = mobile.getText().toString();
                State_Holder=state.getSelectedItem().toString();
                Pin_Holder=Pincode.getText().toString();
                Pan_Holde = pan.getText().toString();
                Emailid_Holder = email.getText().toString();
                Website_Holde = website.getText().toString();
                GST_Holder = gst.getText().toString();


                    BrandRegisterFunction(Name_Holder, Address_Hoder, Addressline1_Holder,Addressline2_Holder,
                            Mobileno_Holder,State_Holder,Pin_Holder,Emailid_Holder,Website_Holde,Pan_Holde,GST_Holder);

                    Intent intent = new Intent(AdminSetting.this, AdminSetting.class);
                    startActivity(intent);

            }
        });

        alertDialogBuilder .setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });


        // all set and time to build and show up!
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //Register brand in database details.
    public void BrandRegisterFunction(final String name, final String address,final String addressline1,final String addressline2,final String mobileno, final String state, final  String pin,final String email_id, final String website, final String pan, final String gst){

        class BrandRegisterFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(AdminSetting.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(AdminSetting.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            // Creating Packet database string to HashMap.
            @Override
            protected String doInBackground(String... params) {

                hashMap.put("name",params[0]);
                hashMap.put("address",params[1]);
                hashMap.put("address1",params[2]);
                hashMap.put("address2",params[3]);
                hashMap.put("mobileno",params[4]);
                hashMap.put("state",params[5]);
                hashMap.put("website",params[6]);
                hashMap.put("pan",params[7]);
                hashMap.put("pincode",params[8]);
                hashMap.put("email",params[9]);
                hashMap.put("gst",params[10]);


                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
            public void execute(String name, String type, String address, String addressline1, String addressline2, String mobileno, EditText state, String counntry, String company_name, String pin, String email_id, String designation) {
            }
        }

        BrandRegisterFunction userRegisterFunctionClass = new BrandRegisterFunction();

        userRegisterFunctionClass.execute( name,  address, addressline1, addressline2,mobileno,
                state,  pin, email_id, website, pan, gst);
    }
}
