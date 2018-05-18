package com.pcdgroup.hp.pcd_group.Client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.Product.ProductSingleRecord;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class SingleRecordShow extends AppCompatActivity {

    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    // Http Url For Filter Client Data from Id Sent from previous activity.
    String HttpURL = "http://dert.co.in/gFiles/filterclientdata.php";

    // Http URL for delete Already Open Client Record.
    String HttpUrlDeleteRecord = "http://dert.co.in/gFiles/deleteclient.php";

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    String ParseResult ;
    HashMap<String,String> ResultHash = new HashMap<>();
    String FinalJSonObject ;
    String IdHolder,NameHolder, AddressHolder, Address1Holder,Address2Holder,MobileHolder,StateHolder,CountryHolder,
            PinHolder,CompnyHolder,EmailHolder,DesignationHolder;
    Button UpdateButton, DeleteButton;
    ProgressDialog progressDialog2;

    Intent intent;
    GlobalVariable gblVar;

    public TextView TextViewName;
    public TextView TextViewAddress;
    public TextView TextviewAddressline1;
    public TextView TextviewAddressline2;
    public TextView TextviewMobileno;
    public TextView TextviewState;
    public TextView TextviewCountry;
    public TextView TextViewCompanyName;
    public TextView TextviewPin;
    public TextView TextViewEmailID;
    public TextView TextViewDesignation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record);

        gblVar = GlobalVariable.getInstance();

        TextViewName = (TextView) findViewById(R.id.tvname) ;
        TextViewAddress = (TextView) findViewById(R.id.tvaddress) ;
        TextviewAddressline1 = (TextView) findViewById(R.id.tvaddressline1) ;
        TextviewAddressline2 = (TextView) findViewById(R.id.tvaddressline2) ;
        TextviewMobileno = (TextView) findViewById(R.id.tv_Mobileno) ;
        TextviewState = (TextView) findViewById(R.id.tv_state) ;
        TextviewCountry = (TextView) findViewById(R.id.tvCountry) ;
        TextViewCompanyName = (TextView) findViewById(R.id.tv_companyname) ;
        TextviewPin = (TextView) findViewById(R.id.tvpin) ;
        TextViewEmailID = (TextView) findViewById(R.id.tvemailid) ;
        TextViewDesignation = (TextView) findViewById(R.id.tvdesignation) ;

        UpdateButton = (Button)findViewById(R.id.buttonUpdate);
        DeleteButton = (Button)findViewById(R.id.buttonDelete);

        //Receiving the ListView Clicked item value send by previous activity.
        IdHolder = getIntent().getStringExtra("id");
        NameHolder = getIntent().getStringExtra("name");
        AddressHolder = getIntent().getStringExtra("address");
        Address1Holder = getIntent().getStringExtra("addressline1");
        Address2Holder = getIntent().getStringExtra("addressline2");
        MobileHolder = getIntent().getStringExtra("mobileno");
        StateHolder = getIntent().getStringExtra("state");
        CountryHolder = getIntent().getStringExtra("country");
        PinHolder = getIntent().getStringExtra("pin");
        CompnyHolder = getIntent().getStringExtra("company");
        EmailHolder = getIntent().getStringExtra("email_id");
        DesignationHolder = getIntent().getStringExtra("designation");

        //Calling method to filter Client Record and open selected record.
        HttpWebCall(IdHolder);


        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SingleRecordShow.this,UpdateActivity.class);

                // Sending Client Id, Name, Number and Class to next UpdateActivity.
                intent.putExtra("id",IdHolder);
                intent.putExtra("name", NameHolder);
                intent.putExtra("address", AddressHolder);
                intent.putExtra("addressline1", Address1Holder);
                intent.putExtra("addressline2", Address2Holder);
                intent.putExtra("mobileno", MobileHolder);
                intent.putExtra("state", StateHolder);
                intent.putExtra("country", CountryHolder);
                intent.putExtra("pin", PinHolder);
                intent.putExtra("company", CompnyHolder);
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

                // Calling Client delete method to delete current record using Client ID.
                ClientDelete(IdHolder);

            }
        });

    }

    // Method to Delete Client Record
    public void ClientDelete(final String ClientID) {

        class ClientDeleteClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog2 = ProgressDialog.show(SingleRecordShow.this, "Loading Data",
                  null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog2.dismiss();

                Toast.makeText(SingleRecordShow.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                // Sending Client id.
                hashMap.put("id",IdHolder);
                Log.v("id ====== ", IdHolder);

                finalResult = httpParse.postRequest(hashMap, HttpUrlDeleteRecord);

                return finalResult;
            }
        }

        ClientDeleteClass ClientDeleteClass = new ClientDeleteClass();

        ClientDeleteClass.execute(ClientID);

        intent = new Intent(this,ClientDetailsActivity.class);
        startActivity(intent);
        finish();
    }

    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem){

        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(SingleRecordShow.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg ;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(SingleRecordShow.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("id",IdHolder);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }


    // Parsing Complete JSON Object.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                if(FinalJSonObject != null)
                {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(FinalJSonObject);

                        JSONObject jsonObject;

                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            // Storing Client Name, Phone Number, Class into Variables.
                            IdHolder =  jsonObject.getString("id").toString() ;
                            NameHolder = jsonObject.getString("name").toString() ;
                            AddressHolder = jsonObject.getString("address").toString() ;
                            Address1Holder = jsonObject.getString("addressline1").toString() ;
                            Address2Holder = jsonObject.getString("addressline2").toString() ;
                            MobileHolder = jsonObject.getString("mobileno").toString() ;
                            StateHolder = jsonObject.getString("state").toString() ;
                            CountryHolder = jsonObject.getString("country").toString() ;
                            PinHolder = jsonObject.getString("pin").toString() ;
                            CompnyHolder = jsonObject.getString("company").toString() ;
                            EmailHolder = jsonObject.getString("email_id").toString() ;
                            DesignationHolder = jsonObject.getString("designation").toString() ;

                        }
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            // Setting Client Name, Phone Number, Class into TextView after done all process .
            TextViewName.setText(NameHolder);
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

                intent = new Intent(this, UserDashbord.class);

            }
            else if (gblVar.AccessType.contains("Client")) {

                intent = new Intent(this, UserDashbord.class);

            }
            else {

                intent = new Intent(this, ViewImage.class);
            }

            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
