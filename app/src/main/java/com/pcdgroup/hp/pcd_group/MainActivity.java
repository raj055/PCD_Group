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
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.AdminLogin.UserDataGet;
import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.ClientRegisterActivity;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.OrderList.Order_List;
import com.pcdgroup.hp.pcd_group.Product.CustomListAdapter;
import com.pcdgroup.hp.pcd_group.Product.Entity;
import com.pcdgroup.hp.pcd_group.Product.ProductSingleRecord;
import com.pcdgroup.hp.pcd_group.Product.ProductUpdate;
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

public class MainActivity extends AppCompatActivity implements CallBackInterface {

    RelativeLayout rellay1,rellay2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    EditText Email, Password;
    Button LogIn,Register;
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

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* // check to see if the user is already logged in
        String username = MySharedPreferences.getUsername(this);
        if (username != null) {
            launchMainActivity();
        }*/

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

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

//                    gblVar.AccessType = Email.getText().toString();

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

    @Override
    public void ExecuteQueryResult(String response) {

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
                }
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
                         intent = new Intent(MainActivity.this, UserDashbord.class);

                        gblVar.AccessType = "Manager";
                        gblVar.DiscountType = "100";

                    }
                    else if(accessType.contains("Client")) {
                        Log.v("To be","in Client mode" );
                         intent = new Intent(MainActivity.this, UserDashbord.class);

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
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"The Username or password you entered is incorrect.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }

        }

    }
}
