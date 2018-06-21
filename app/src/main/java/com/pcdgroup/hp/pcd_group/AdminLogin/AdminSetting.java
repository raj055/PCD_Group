package com.pcdgroup.hp.pcd_group.AdminLogin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */
public class AdminSetting extends AppCompatActivity implements CallBackInterface {

    Button Brand;
    EditText brandname, address, address1, address2,pincode,mobileno,email,website,pan,gst;
    Spinner state, SpinerBrand;
    TextView details;

    Boolean CheckEditText;
    String Name_Holder, Address_Hoder,Addressline1_Holder,Addressline2_Holder,Mobileno_Holder,
      State_Holder,Pin_Holder, Emailid_Holder, Website_Holde, Pan_Holde, GST_Holder;

    HashMap<String,String> hashMap = new HashMap<>();

    View promptUserView;

    List<Category> categoriesList;
    BrandAdepter adepter;
    String[] data;

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsetting);

        LayoutInflater layoutinflater = LayoutInflater.from(this);
        promptUserView = layoutinflater.inflate(R.layout.brand_dialog_box,null);

        brandname = (EditText) promptUserView.findViewById(R.id.brnadname);
        address = (EditText) promptUserView.findViewById(R.id.editText);
        address1 = (EditText) promptUserView.findViewById(R.id.editText2);
        address2 = (EditText) promptUserView.findViewById(R.id.editText3);
        pincode = (EditText) promptUserView.findViewById(R.id.editText4);
        mobileno = (EditText) promptUserView.findViewById(R.id.editText5);
        email = (EditText) promptUserView.findViewById(R.id.editText6);
        website = (EditText) promptUserView.findViewById(R.id.editText7);
        pan = (EditText) promptUserView.findViewById(R.id.editText9);
        gst = (EditText) promptUserView.findViewById(R.id.editText8);
        state = (Spinner)  promptUserView.findViewById(R.id.spinner6);

        Brand = (Button) findViewById(R.id.btn_brand);
        details = (TextView) findViewById(R.id.tv_details);
        SpinerBrand = (Spinner) findViewById(R.id.spbrnad);

        categoriesList = new ArrayList<Category>();

        adepter = new BrandAdepter(this, categoriesList);
        SpinerBrand.setAdapter(adepter);
        adepter.notifyDataSetChanged();

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        urlQry = DataGetUrl.LIST_BRAND;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                AdminSetting.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        //Adepter
        adepter.notifyDataSetChanged();

        // Click Brand button
        Brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();

                CheckAnyAddress();

            }
        });

        SpinerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
//                String selectedItem = parent.getItemAtPosition(position).toString();

                details.setText("Name : " + categoriesList.get(position).getName() + "\n" + "Address : " + categoriesList.get(position).getAddress()
                        + "\n"  + "Address line_1 : " + categoriesList.get(position).getAddress1() + "\n" + "Address line 2 : " + categoriesList.get(position).getAddress2()
                        + "\n" + "Pincode : " + categoriesList.get(position).getPincode() + "\n" + "State : " + categoriesList.get(position).getState()
                        + "\n" + "Mobile No : " + categoriesList.get(position).getMobileno() + "\n" + "Email Id : " + categoriesList.get(position).getEmail()
                        + "\n" + "WebSite : " + categoriesList.get(position).getWebsite() + "\n" + "PAN NO : " + categoriesList.get(position).getPan()
                        + "\n" + "GST  No : " + categoriesList.get(position).getGst());

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

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

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
                Pin_Holder=pincode.getText().toString();
                State_Holder=state.getSelectedItem().toString();
                Mobileno_Holder = mobileno.getText().toString();
                Pan_Holde = pan.getText().toString();
                Emailid_Holder = email.getText().toString();
                Website_Holde = website.getText().toString();
                GST_Holder = gst.getText().toString();

                urlQry = DataGetUrl.ADD_BRAND;
                typeOfQuery = CallType.POST_CALL;

                hashMap.put("name",Name_Holder);
                hashMap.put("address",Address_Hoder);
                hashMap.put("address1",Addressline1_Holder);
                hashMap.put("address2",Addressline2_Holder);
                hashMap.put("pincode",Pin_Holder);
                hashMap.put("state",State_Holder);
                hashMap.put("mobileno",Mobileno_Holder);
                hashMap.put("email",Emailid_Holder);
                hashMap.put("website",Website_Holde);
                hashMap.put("pan",Pan_Holde);
                hashMap.put("gst",GST_Holder);

                //Send Database query for inquiring to the database.
                dataBaseQuery = new DataBaseQuery(hashMap,
                        urlQry,
                        typeOfQuery,
                        getApplicationContext(),
                        AdminSetting.this
                );
                //Prepare for the database query
                dataBaseQuery.PrepareForQuery();

                finish();

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

    @Override
    public void onBackPressed() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminSetting.this);
        builder.setMessage("Are You Sure Want To Exit Brand Setting?");
        builder.setCancelable(true);
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ExecuteQueryResult(String response, DataGetUrl dataGetUrl) {

        if (dataGetUrl.equals(DataGetUrl.LIST_BRAND)) {
            try {

                JSONArray ja = new JSONArray(response);
                JSONObject jo = null;

                data = new String[ja.length()];

                for (int i = 0; i < ja.length(); i++) {

                    jo = ja.getJSONObject(i);
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
                    adepter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(AdminSetting.this, response.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
