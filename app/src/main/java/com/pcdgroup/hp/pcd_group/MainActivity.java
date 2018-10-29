package com.pcdgroup.hp.pcd_group;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.SharedPreferences.MySharedPreferences;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserRegistarActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name MainActivity
 * @description login page to user add email and password to login activity
 */

public class MainActivity extends AppCompatActivity implements CallBackInterface {

    //
    RelativeLayout rellay1,rellay2;
    Handler handler = new Handler();

    //Login details widgets
    EditText Email, Password;
    String PasswordHolder, EmailHolder, UserType, DiscountValue;
    Boolean CheckEditText ;
    public static final String UserEmail = "";

    //Globals
    GlobalVariable gblVar;

    //Shutdown status
    boolean shutdown = false;

    //Database Components
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;
    HashMap<String,String> hashMap = new HashMap<>();
    Intent intent;

    Runnable runnable;

    /** Populates the Login screen including the splash screen.
     * Queries the Database for the verification of the user details.
     * Stores the details in SharedPreferences.
     * @param savedInstanceState object of passing parameters from the previous intent */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        android.app.ActionBar actionBar = getActionBar();
//        actionBar.setBackgroundDrawable( new ColorDrawable(getResources().getColor(R.color.AntiqueWhite)));

        //    login page to login activity
        //    check database to access type after this type user login
        if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else {
            Toast.makeText(MainActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
        }
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        runnable= new Runnable() {

            @Override
            public void run() {
//                while (!shutdown){
            if(rellay1 != null)
                rellay1.setVisibility(View.VISIBLE);
            if(rellay2 != null)
                rellay2.setVisibility(View.VISIBLE);
//                }
            }
        };

        gblVar = GlobalVariable.getInstance();

        Email = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.password);


        // check to see if the user is already logged in
        String username      = MySharedPreferences.getUsername(this);
        String password      = MySharedPreferences.getPassword(this);
        String usertype      = MySharedPreferences.getUsertype(this);
        String discountvalue = MySharedPreferences.getDiscountValue(this);



        if ((username != null) && (password != null) && (usertype != null) && (discountvalue != null)) {
            Log.v("Username----",username);
            Log.v("Password----", password);

        //    launchMainActivity();
            EmailHolder = username;
            PasswordHolder = password;
            UserType = usertype;
            DiscountValue = discountvalue;
            checkAccessType(usertype, discountvalue);
//            QueryTheDataBase();
        }
        else
            QueryTheDataBase();
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash




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

    public void onClickLogin(View view) {

//                saveUsername();

        CheckEditTextIsEmptyOrNot();

        if(CheckEditText){
            QueryTheDataBase();
        }
        else {
            Toast.makeText(MainActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
        }
    }
    /** Go to the register activity on clicking the register button */
    public void onClickRegister(View view) {

        Intent intent = new Intent(MainActivity.this, UserRegistarActivity.class);
        startActivity(intent);
    }
    /** Prepare the variables for querying the Database. */
    private void QueryTheDataBase(){

        urlQry = DataGetUrl.USER_LOGIN;
        typeOfQuery = CallType.POST_CALL;

        hashMap.put("email_id",EmailHolder);
        hashMap.put("password",PasswordHolder);

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
          urlQry,
          typeOfQuery,
          getApplicationContext(),
          MainActivity.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

    }
    /** Save the required parameters in Shared Preferences. */
    private void saveUsername(String username,
                              String password,
                              String usertype,
                              String discountvalue) {

        MySharedPreferences.storeUsername(this, username, password, usertype, discountvalue );

        launchMainActivity();
    }
    /** Save the required parameters in Shared Preferences. */
    private void launchMainActivity() {
        Intent intent = new Intent(MainActivity.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }
    /** Check if the entered values are empty. */
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

    /** CallBack Function for processing the Database query result.
     * @param  response - Response string received while database query.
     *         dataGetUrl - Url queried.*/
    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {

        if(response.equalsIgnoreCase("Invalid Username or Password")){

            Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
        }
        else{
            try {

                JSONArray ja = new JSONArray(response);
                JSONObject jo = null;
                String accessType = "";
                String discountType = "";
                String data[] = new String[ja.length()];
                Log.v("length","" + ja.length());
                for (int i=0; i<ja.length();i++){

                    jo=ja.getJSONObject(i);

                    accessType = jo.getString("Access");
                    discountType = jo.getString("Discount");
                    checkAccessType(accessType, discountType);
                    saveUsername(EmailHolder, PasswordHolder,accessType,discountType);
                }

            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"The Username or password you entered is incorrect.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }

        }

    }

    public void checkAccessType(String accessType, String discountType){
        if(accessType != null && discountType != null)
        {
            Log.v("accesstype","" + accessType);
            if(accessType.contains("Admin")) {
                Log.v("To be","in Admin mode" );
                intent = new Intent(MainActivity.this, AdminDashboard.class);

                gblVar.AccessType = "Admin";
                gblVar.DiscountType = "100";

            }
            else if(accessType.contains("Manager")) {
                Log.v("To be","in Manager mode" );
                intent = new Intent(MainActivity.this, AdminDashboard.class);

                gblVar.AccessType = "Manager";
                gblVar.DiscountType = "100";

            }
            else if(accessType.contains("Client")) {
                Log.v("To be","in Client mode" );
                intent = new Intent(MainActivity.this, AdminDashboard.class);

                gblVar.AccessType = "Client";
                gblVar.DiscountType = discountType;

            }
            else{
                intent = new Intent(MainActivity.this, ViewImage.class);
                gblVar.AccessType = "User";

            }

            intent.putExtra(UserEmail , EmailHolder);

            startActivity(intent);
            finish();
        }

    }
    /** See if app has access to internet .
     * @param  context */
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }
    /** Dialog Box to check if the internet is working.
     * @param  c Context*/
    public android.support.v7.app.AlertDialog.Builder buildDialog(Context c) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(c);
        builder.setTitle("INTERNET REQUIRED");
        builder.setMessage("You must connect the internet to use this application. Please connect and try again");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }
    /** Releases the memory of all the components after intent finishes. */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        shutdown = true;
        runnable = null;
        rellay1 = null;
        rellay2 = null;
        Email = null;
        Password = null;
    }
}
