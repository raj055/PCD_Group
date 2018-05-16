package com.pcdgroup.hp.pcd_group;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.AdminLogin.AdminLoginActivity;
import com.pcdgroup.hp.pcd_group.AdminLogin.UserDataGet;
import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.ClientRegisterActivity;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.OrderList.Order_List;
import com.pcdgroup.hp.pcd_group.Product.CustomListAdapter;
import com.pcdgroup.hp.pcd_group.Product.Entity;
import com.pcdgroup.hp.pcd_group.Product.ProductSingleRecord;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.SharedPreferences.MySharedPreferences;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserRegistarActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class MainActivity extends AppCompatActivity {


    EditText Email, Password;
    Button LogIn,Register ;
    String PasswordHolder, EmailHolder, DiscountHolder;
    String finalResult;
    String HttpURL = "http://dert.co.in/gFiles/UserLogin.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserEmail = "";
    public static final String ClientDiscount = "";
    GlobalVariable gblVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* // check to see if the user is already logged in
        String username = MySharedPreferences.getUsername(this);
        if (username != null) {
            launchMainActivity();
        }*/

        Email = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.password);
        LogIn = (Button)findViewById(R.id.Login);
        Register = (Button)findViewById(R.id.register);

        gblVar = GlobalVariable.getInstance();

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                saveUsername();

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    UserLoginFunction(EmailHolder, PasswordHolder);

                }
                else {

                    Toast.makeText(MainActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,UserRegistarActivity.class);
                startActivity(intent);
            }
        });
/*
        Email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                // detect if the user presses [enter]
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    saveUsername();
                    return true;
                }
                return false;
            }
        });

        Password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                // detect if the user presses [enter]
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    saveUsername();
                    return true;
                }
                return false;
            }
        });*/
    }

    /*private void saveUsername() {
        String username = Email.getText().toString();
        String userpassword =  Password.getText().toString();
        MySharedPreferences.storeUsername(this, username, userpassword);

        launchMainActivity();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(MainActivity.this, UserDashbord.class);
        startActivity(intent);
    }*/

    public void CheckEditTextIsEmptyOrNot(){

        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }

    public void UserLoginFunction(final String email, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity.this,"Loading Data",
                        null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

//                String str = httpResponseMsg.toString();

                if(httpResponseMsg.equalsIgnoreCase("Invalid Username or Password")){

                    Toast.makeText(MainActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }
                else{
                    try {

                        JSONArray ja = new JSONArray(httpResponseMsg);
                        JSONObject jo = null;
                        String accessType = "";
                        String discountType = "";
                        String data[] = new String[ja.length()];
                        Log.v("length","" + ja.length());
                        for (int i=0; i<ja.length();i++){

                            jo=ja.getJSONObject(i);

                            accessType = jo.getString("Access");
                            discountType = jo.getString("Discount");
                        }
                        if(accessType != null && discountType != null)
                        {
                            Log.v("accesstype","" + accessType);
                            if(accessType.contains("Admin")) {
                                Log.v("To be","in Adming mode" );
                                Intent intent = new Intent(MainActivity.this, AdminDashboard.class);

                                intent.putExtra(UserEmail , email);
                                gblVar.AccessType = "Admin";
                                gblVar.DiscountType = "100%";
                                startActivity(intent);

                                finish();
                            }
                            else if(accessType.contains("Manager")) {
                                Log.v("To be","in Manager mode" );
                                Intent intent = new Intent(MainActivity.this, UserDashbord.class);

                                intent.putExtra(UserEmail , email);
                                gblVar.AccessType = "Manager";
                                gblVar.DiscountType = "100%";
                                startActivity(intent);

                                finish();
                            }
                            else if(accessType.contains("Client")) {
                                Log.v("To be","in Client mode" );
                                Intent intent = new Intent(MainActivity.this, UserDashbord.class);

                                intent.putExtra(UserEmail , email);
                                gblVar.AccessType = "Client";
                                gblVar.DiscountType = discountType;
                                startActivity(intent);

                                finish();
                            }
                            else{
                                Intent intent = new Intent(MainActivity.this, ViewImage.class);

                                intent.putExtra(UserEmail, email);
                                gblVar.AccessType = "User";
                                startActivity(intent);

                                finish();
                            }
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),"The Username or password you entered is incorrect.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }

                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                gblVar.AccessType = Email.getText().toString();

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(email,password);
    }
}
