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
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.Quotation.Invoice;
import com.pcdgroup.hp.pcd_group.Quotation.PDFViewActivity;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;

import java.util.HashMap;

public class Add_Dealer extends Fragment {

    EditText name,address,area,mobileno,state,email,organisation,gstno,designation;
    Button submit;
    String Name_Holder, Address_Hoder, Area_Holder, Mobileno_Holder,State_Holder, Email_Holder,
             Organisation_Holder, Gst_Holder, Designation_Holder;
    String finalResult;
    String HttpURL = "http://dert.co.in/gFiles/dealer.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    GlobalVariable globalVariable;

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
        designation = (EditText) view.findViewById(R.id.designation_et);

        submit = (Button) view.findViewById(R.id.submit_btnDealer);

        //Adding Click Listener on button.
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not.
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    UserRegisterFunction(Name_Holder, Address_Hoder, Area_Holder,State_Holder,
                            Email_Holder, Mobileno_Holder, Organisation_Holder,
                            Gst_Holder,Designation_Holder);
                }
                else {

                    // If EditText is empty then this block will execute.
                    Toast.makeText(getActivity(), "Please fill all form fields.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

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

        Name_Holder = Name_Holder.replace("'","''");
        Address_Hoder = Address_Hoder.replace("'","''");
        Area_Holder = Area_Holder.replace("'","''");
        State_Holder = State_Holder.replace("'","''");
        Email_Holder = Email_Holder.replace("'","''");
        Organisation_Holder = Organisation_Holder.replace("'","''");
        Designation_Holder = Designation_Holder.replace("'","''");

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
                                     final String mobileno, final String organisation,
                                     final String gstno, final String designation){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(getActivity(),"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(getActivity(),httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

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
                hashMap.put("organisation",params[6]);
                hashMap.put("gstno",params[7]);
                hashMap.put("designation",params[8]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }

        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(name,address,area,state,email,mobileno,organisation,gstno,designation);
    }

}
