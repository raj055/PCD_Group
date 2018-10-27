package com.pcdgroup.hp.pcd_group.UserLoginRegister;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.Client.ClientRegisterActivity;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.Quotation.CreateQuotation;
import com.pcdgroup.hp.pcd_group.R;

import java.util.HashMap;
import java.util.Random;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name UserRegistarActivity
 * @description register new user to use this application
 */

public class UserRegistarActivity extends AppCompatActivity implements CallBackInterface {

    EditText First_Name, Last_Name,MobileNo, Email, Password ;
    String F_Name_Holder, L_Name_Holder, MobileNo_Holder, EmailHolder, PasswordHolder;
    String emailPattern;
    Boolean CheckEditText ;
    HashMap<String,String> hashMap = new HashMap<>();
    EditText VerifayCode;
    TextView RegenrateCode;
    String strVerify;
    String senderHolder,numberHolde,message_Holder,code_Holder;
    Random Number;
    int Rnumber;
    Button Verify;

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    /** Registers new user. Adds users to database.
     * Confirms mobile number by sending OTP.
     * @param savedInstanceState object of passing parameters from the previous intent */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        //Assign Id'S
        First_Name = (EditText)findViewById(R.id.editTextF_Name);
        Last_Name = (EditText)findViewById(R.id.editTextL_Name);
        Email = (EditText)findViewById(R.id.editTextEmail);
        Password = (EditText)findViewById(R.id.editTextPassword);
        MobileNo = (EditText)findViewById(R.id.editTextL_mobile);
    }

    public void onClickUserRegister(View view) {

        // Checking whether EditText is Empty or Not
        CheckEditTextIsEmptyOrNot();

        if(EmailHolder.matches(emailPattern) && CheckEditText){

            VerificationCode();
        }
        else {

            // If EditText is empty then this block will execute .
            Toast.makeText(UserRegistarActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();

        }
    }

    private void VerificationCode() {

        Number = new Random();
        Rnumber = Number.nextInt(9999-1000)+1000;
        message_Holder = Integer.toString(Rnumber);

        String email = Email.getText().toString().trim();
        String subject = getResources().getString(R.string.emailSend);
        String message = message_Holder + getResources().getString(R.string.otp_msg);

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();

        LayoutInflater layoutinflater = LayoutInflater.from(this);
        View promptUserView = layoutinflater.inflate(R.layout.code_verfication_dialogbox, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptUserView);

        VerifayCode = (EditText) promptUserView.findViewById(R.id.username);
        RegenrateCode = (TextView) promptUserView.findViewById(R.id.regenratecode);
        Verify = (Button) promptUserView.findViewById(R.id.verified);


        urlQry = DataGetUrl.OTP_REGISTER;

        typeOfQuery = CallType.POST_CALL;

        hashMap.put("sender",senderHolder);

        hashMap.put("numbers",numberHolde);

        hashMap.put("message",message_Holder);

        hashMap.put("code",code_Holder);

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                UserRegistarActivity.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        Log.v("message_Holder------",message_Holder);

        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strVerify = VerifayCode.getText().toString();
                Log.v("strVerify----",strVerify);

                if (strVerify.equals(message_Holder)) {

                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    urlQry = DataGetUrl.USER_REGISTER;

                    typeOfQuery = CallType.POST_CALL;

                    hashMap.put("first_name",F_Name_Holder);

                    hashMap.put("last_name",L_Name_Holder);

                    hashMap.put("email_id",EmailHolder);

                    hashMap.put("password",PasswordHolder);

                    hashMap.put("mobile_num",MobileNo_Holder);


                    //Send Database query for inquiring to the database.
                    dataBaseQuery = new DataBaseQuery(hashMap,
                            urlQry,
                            typeOfQuery,
                            getApplicationContext(),
                            UserRegistarActivity.this
                    );
                    //Prepare for the database query
                    dataBaseQuery.PrepareForQuery();

                    Intent intent = new Intent(UserRegistarActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(UserRegistarActivity.this,"You Enter Wrong Code.",Toast.LENGTH_SHORT).toString();
                }

            }
        });

        RegenrateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificationCode();
            }
        });

        // all set and time to build and show up!
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void CheckEditTextIsEmptyOrNot(){

        F_Name_Holder = First_Name.getText().toString();
        L_Name_Holder = Last_Name.getText().toString();
        EmailHolder = Email.getText().toString().trim();
        PasswordHolder = Password.getText().toString();
        MobileNo_Holder = MobileNo.getText().toString();
        
        F_Name_Holder = F_Name_Holder.replace("'","''");
        L_Name_Holder = L_Name_Holder.replace("'","''");

        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(TextUtils.isEmpty(F_Name_Holder) || TextUtils.isEmpty(L_Name_Holder)  || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder) ||  TextUtils.isEmpty(MobileNo_Holder))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }

    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
        Toast.makeText(UserRegistarActivity.this,response.toString(), Toast.LENGTH_LONG).show();
    }
    /** Releases the memory of all the components after intent finishes. */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        First_Name = null;
        Last_Name = null;
        MobileNo = null;
        Email = null;
        Password = null;
        RegenrateCode = null;
        VerifayCode = null;
        Verify = null;
    }
}
