package com.pcdgroup.hp.pcd_group;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.AdminLogin.AdminLoginActivity;
import com.pcdgroup.hp.pcd_group.AdminLogin.UserDataGet;
import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.ClientRegisterActivity;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.SharedPreferences.MySharedPreferences;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserRegistarActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class MainActivity extends AppCompatActivity {

    EditText Email, Password;
    Button LogIn,Register ;
    String PasswordHolder, EmailHolder;
    String finalResult ;
    String HttpURL = "http://pcddata-001-site1.1tempurl.com/UserLogin.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserEmail = "";

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
                        String data[] = new String[ja.length()];
                        Log.v("length","" + ja.length());
                        for (int i=0; i<ja.length();i++){

                            jo=ja.getJSONObject(i);

                            accessType = jo.getString("Access");
                        }
                        if(accessType != null)
                        {
                            Log.v("accesstype","" + accessType);
                          if(accessType.contains("Admin") )  {
                              Log.v("To be","in Adming mode" );
                              Intent intent = new Intent(MainActivity.this, AdminDashboard.class);

                              intent.putExtra(UserEmail, email);

                              startActivity(intent);

                              finish();

                          }
                          else{

                              Intent intent = new Intent(MainActivity.this, UserDashbord.class);

                              intent.putExtra(UserEmail, email);

                              startActivity(intent);

                              finish();
                          }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();


                    }




                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(email,password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.administrator), getResources().getString(R.string.action_admin)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(getApplicationContext(), AdminLoginActivity.class);

        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
}
