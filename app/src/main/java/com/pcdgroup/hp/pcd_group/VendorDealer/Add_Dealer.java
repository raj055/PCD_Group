package com.pcdgroup.hp.pcd_group.VendorDealer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.pcdgroup.hp.pcd_group.R;

import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name Add_Dealer
 * @description add dealer in database to all information of dealer
 */

public class Add_Dealer extends Fragment implements CallBackInterface {

    //Widgets for addition of Dealor
    EditText name,address,area,mobileno,state,email,organisation,gstno,designation;
    String Name_Holder, Address_Hoder, Area_Holder, Mobileno_Holder,State_Holder, Email_Holder,
             Organisation_Holder, Gst_Holder, Designation_Holder;
    Boolean CheckEditText ;
    Button addDealor;

    //Globals
    GlobalVariable globalVariable;

    //Database components.
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;
    HashMap<String,String> hashMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dealer,container,false);

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

        //Get the submit button
        addDealor = (Button) view.findViewById(R.id.submit_btnDealer);

        //Assign the click listener
        addDealor.setOnClickListener(dealorAdd);

        return view;
    }

    /** Add Dealor on selection DealorAdd Button */
    View.OnClickListener dealorAdd = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Checking whether EditText is Empty or Not.
            CheckEditTextIsEmptyOrNot();

            if(CheckEditText){

                urlQry = DataGetUrl.ADD_DEALER;
                typeOfQuery = CallType.POST_CALL;

                //Fill the hasmap
                hashMap.put("name",Name_Holder);
                hashMap.put("address",Name_Holder);
                hashMap.put("area",Area_Holder);
                hashMap.put("state",State_Holder);
                hashMap.put("email",Email_Holder);
                hashMap.put("mobileno",Mobileno_Holder);
                hashMap.put("organisation",Organisation_Holder);
                hashMap.put("gstno",Gst_Holder);
                hashMap.put("designation",Designation_Holder);

                //Send Database query for inquiring to the database.
                dataBaseQuery = new DataBaseQuery(hashMap,
                  urlQry,
                  typeOfQuery,
                  getActivity().getApplicationContext(),
                  Add_Dealer.this
                );
                //Prepare for the database query
                dataBaseQuery.PrepareForQuery();

            }
            else {

                // If EditText is empty then this block will execute.
                Toast.makeText(getActivity(), "Please fill all form fields.", Toast.LENGTH_SHORT).show();
            }

        }
    };

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
        Designation_Holder = designation.getText().toString();

        //Allow the Apostrophe in the text so that the text is not rejected.
        Name_Holder = Name_Holder.replace("'","''");
        Address_Hoder = Address_Hoder.replace("'","''");
        Area_Holder = Area_Holder.replace("'","''");
        State_Holder = State_Holder.replace("'","''");
        Email_Holder = Email_Holder.replace("'","''");
        Organisation_Holder = Organisation_Holder.replace("'","''");
        Designation_Holder = Designation_Holder.replace("'","''");

        //Set the boolean variable with the status value.
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
        designation = null;
    }
}
