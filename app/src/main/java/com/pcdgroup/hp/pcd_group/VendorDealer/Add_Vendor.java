package com.pcdgroup.hp.pcd_group.VendorDealer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.R;

import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name Add_Vendor
 * @description add new Vendor in database and add related product
 */

public class Add_Vendor extends Fragment implements CallBackInterface {

    //Components for Vendor
    EditText name, address, area, mobileno, state, email, organisation, gstno, products;
    String Name_Holder, Address_Hoder, Area_Holder, Mobileno_Holder,State_Holder, Email_Holder,
             Organisation_Holder, Gst_Holder, Products_Holder;
    Boolean CheckEditText ;
    Button addVendorSubmit, addRelevantProducts;

    //Globals
    GlobalVariable globalVariable;

    //Database Components
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vendor,container,false);

        globalVariable = GlobalVariable.getInstance();

        //Assign Id'S
        name = (EditText) view.findViewById(R.id.name_et);
        state = (EditText) view.findViewById(R.id.state_et);
        address = (EditText) view.findViewById(R.id.address_et);
        area = (EditText) view.findViewById(R.id.area_et);
        state = (EditText) view.findViewById(R.id.state_et);
        email = (EditText) view.findViewById(R.id.email_et);
        mobileno = (EditText) view.findViewById(R.id.mobile_et);
        organisation = (EditText) view.findViewById(R.id.organisation_et);
        gstno = (EditText) view.findViewById(R.id.gstno_et);
        products = (EditText) view.findViewById(R.id.product_et);

        //Assign Buttons
        addVendorSubmit = (Button) view.findViewById(R.id.submit_btnVendor);
        addRelevantProducts = (Button) view.findViewById(R.id.btn_vendor_product);

        //Assign the click listeners
        addVendorSubmit.setOnClickListener(addVendorListener);
        addRelevantProducts.setOnClickListener(addProducts);

        return view;
    }

    /** Add the Products related to the Vendors. */
    View.OnClickListener addProducts = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(),VendorProductAdd.class);
            startActivityForResult(intent, 1);

        }
    };
    /** Add the Vendors by querying database. */
    View.OnClickListener addVendorListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Checking whether EditText is Empty or Not.
            CheckEditTextIsEmptyOrNot();

            if(CheckEditText){

                urlQry = DataGetUrl.ADD_VENDOR;
                typeOfQuery = CallType.POST_CALL;

                hashMap.put("name",Name_Holder);
                hashMap.put("address",Address_Hoder);
                hashMap.put("area",Area_Holder);
                hashMap.put("state",State_Holder);
                hashMap.put("email",Email_Holder);
                hashMap.put("mobileno",Mobileno_Holder);
                hashMap.put("organisation",Organisation_Holder);
                hashMap.put("gstno",Gst_Holder);
                hashMap.put("products",Products_Holder);

                //Send Database query for inquiring to the database.
                dataBaseQuery = new DataBaseQuery(hashMap,
                  urlQry,
                  typeOfQuery,
                  getActivity().getApplicationContext(),
                  Add_Vendor.this
                );
                //Prepare for the database query
                dataBaseQuery.PrepareForQuery();
            }
            else {
                // If EditText is empty then this block will execute.
                Toast.makeText(getActivity(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
            }
        }
    };

    /** On returning from select product activity. */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {

                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Client Details
                    if (extras.containsKey("ProductID")) {
                        globalVariable.globalAddVendorProduct[0] = extras.getString("ProductID");
                        globalVariable.globalAddVendorProduct[1] = extras.getString("name");
                        globalVariable.globalAddVendorProduct[2] = extras.getString("photo");
                        globalVariable.globalAddVendorProduct[3] = extras.getString("price");
                        globalVariable.globalAddVendorProduct[4] = extras.getString("hsncode");
                        globalVariable.globalAddVendorProduct[5] = extras.getString("gst");
                        globalVariable.globalAddVendorProduct[6] = extras.getString("stock");
                        globalVariable.globalAddVendorProduct[7] = extras.getString("description");
                    }
                    products.setText(globalVariable.globalAddVendorProduct[0]);
                }
            }
        }
    }
    /** Check whether Edit Text is empty or not. */
    public void CheckEditTextIsEmptyOrNot(){

        //Checking all EditText Empty or Not.
        Name_Holder = name.getText().toString();
        Address_Hoder = address.getText().toString();
        Area_Holder = area.getText().toString();
        State_Holder = state.getText().toString();
        Email_Holder = email.getText().toString();
        Mobileno_Holder = mobileno.getText().toString();
        Organisation_Holder = organisation.getText().toString();
        Gst_Holder = gstno.getText().toString();
        Products_Holder = products.getText().toString();

        //Allow the Apostrophe in the text so that the text is not rejected.
        Name_Holder = Name_Holder.replace("'","''");
        Address_Hoder = Address_Hoder.replace("'","''");
        Area_Holder = Area_Holder.replace("'","''");
        State_Holder = State_Holder.replace("'","''");
        Email_Holder = Email_Holder.replace("'","''");
        Organisation_Holder = Organisation_Holder.replace("'","''");
        Products_Holder = Products_Holder.replace("'","''");

        if(TextUtils.isEmpty(Name_Holder) || TextUtils.isEmpty(Email_Holder) || TextUtils.isEmpty(Mobileno_Holder))
        {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }

    /** CallBack Function for processing the Database query result.
     * @param  response - Response string received while database query.
     *         dataGetUrl - Url queried.*/
    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
        Toast.makeText(getActivity(),response.toString(), Toast.LENGTH_LONG).show();
    }

    /** Releases the memory of all the components after intent finishes. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        name = null;
        address = null;
        area = null;
        mobileno = null;
        state = null;
        email = null;
        organisation = null;
        gstno = null;
        products = null;
    }
}
