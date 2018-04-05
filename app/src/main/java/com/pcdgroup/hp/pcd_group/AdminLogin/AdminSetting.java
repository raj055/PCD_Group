package com.pcdgroup.hp.pcd_group.AdminLogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */
public class AdminSetting extends AppCompatActivity {

    Button Brand;
    EditText brandname, address, address1, address2,Pincode,mobile,email,website,pan,gst;
    Spinner state, SpinerBrand;

    Boolean CheckEditText;
    String Name_Holder, Address_Hoder,Addressline1_Holder,Addressline2_Holder,Mobileno_Holder,State_Holder,Pin_Holder, Emailid_Holder, Website_Holde, Pan_Holde, GST_Holder;
    String finalResult;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    String HttpURL = "http://pcddata-001-site1.1tempurl.com/brandadd.php";

    String HttpURL_get = "http://pcddata-001-site1.1tempurl.com/listbrands.php";

    View promptUserView;
    ProgressDialog pDialog;

    List<Category> categoriesList;
    BrandAdepter adepter;
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;

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

        SpinerBrand = (Spinner) findViewById(R.id.spbrnad);

        categoriesList = new ArrayList<Category>();

        adepter = new BrandAdepter(this, categoriesList);
        SpinerBrand.setAdapter(adepter);
        adepter.notifyDataSetChanged();

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        //Retrieve
        getData();

        //Adepter
        adepter.notifyDataSetChanged();

        // Click Brand button
        Brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckAnyAddress();

                CheckEditTextIsEmptyOrNot();
            }
        });

        SpinerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Add new category"))
                {
                    // do your stuff
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

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

//                if(CheckEditText) {
                    BrandRegisterFunction(Name_Holder, Address_Hoder, Addressline1_Holder, Addressline2_Holder,
                            Mobileno_Holder, State_Holder, Pin_Holder, Emailid_Holder, Website_Holde, Pan_Holde, GST_Holder);
//                }
//                else {
//
//                    // If EditText is empty then this block will execute.
//                    Toast.makeText(ClientRegisterActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(AdminSetting.this, AdminSetting.class);
                    startActivity(intent);

//                    }

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

    private void getData(){

        try {
            URL url = new URL(HttpURL_get);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is = new BufferedInputStream(con.getInputStream());

        }catch (Exception e){
            e.printStackTrace();
        }

        //Read in content into String
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        //Parse json data
        try {

            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            data = new String[ja.length()];

            for (int i=0; i<ja.length();i++){

                jo=ja.getJSONObject(i);
                String name = jo.getString("name");
                Category e = new Category(name);
                categoriesList.add(e);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
