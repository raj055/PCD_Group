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

import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.Quotation.CreateQuotation;
import com.pcdgroup.hp.pcd_group.R;

import java.util.HashMap;
import java.util.Random;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class UserRegistarActivity extends AppCompatActivity {

    Button register;
    EditText First_Name, Last_Name,MobileNo, Email, Password ;
    String F_Name_Holder, L_Name_Holder, MobileNo_Holder, EmailHolder, PasswordHolder;
    String emailPattern;
    String finalResult;
    String HttpURL = "http://dert.co.in/gFiles/UserRegistration.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    String HttpURLVERIFYCODE = "http://dert.co.in/gFiles/otp_registration_user.php";
    EditText VerifayCode;
    TextView RegenrateCode;
    Button Verify;
    String strVerify,genrateCode;
    String senderHolder,numberHolde,message_Holder,code_Holder;
    Random Number;
    int Rnumber;
    String finalResultcode;

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

        register = (Button)findViewById(R.id.Submit);

        //Adding Click Listener on button.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        });

    }

    private void VerificationCode() {

        Number = new Random();
        Rnumber = Number.nextInt(9999-1000)+1000;
        message_Holder = Integer.toString(Rnumber);

        String email = Email.getText().toString().trim();
        String subject = getResources().getString(R.string.emailsend);
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


//        CodeGenrate(senderHolder,numberHolde,message_Holder,code_Holder);

        Log.v("message_Holder------",message_Holder);

        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strVerify = VerifayCode.getText().toString();
                Log.v("strVerify----",strVerify);

                if (strVerify.equals(message_Holder)) {
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    UserRegisterFunctionClass(F_Name_Holder, L_Name_Holder, EmailHolder, MobileNo_Holder,
                            PasswordHolder);

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

    public void UserRegisterFunctionClass(final String F_Name, final String L_Name, final String email,
                                          final String password, final String mobile){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UserRegistarActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UserRegistarActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("first_name",params[0]);

                hashMap.put("last_name",params[1]);

                hashMap.put("email_id",params[2]);

                hashMap.put("password",params[3]);

                hashMap.put("mobile_num",params[4]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(F_Name,L_Name,email,mobile,password);
    }

    private void CodeGenrate(final String sender, final String number, final String message, final String code){

        class CodeGenrate extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UserRegistarActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UserRegistarActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("sender",params[0]);

                hashMap.put("numbers",params[1]);

                hashMap.put("message",params[2]);

                hashMap.put("code",params[3]);

                finalResultcode = httpParse.postRequest(hashMap, HttpURLVERIFYCODE);

                return finalResultcode;
            }
        }

        CodeGenrate codeGenrate = new CodeGenrate();

        codeGenrate.execute(sender,number,message,code);
    }
}
