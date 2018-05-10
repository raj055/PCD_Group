package com.pcdgroup.hp.pcd_group.Discount;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AccessAdmin;
import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.AdminLogin.UserAdminAdepter;
import com.pcdgroup.hp.pcd_group.AdminLogin.UserDataGet;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class Client_Discount extends AppCompatActivity {

    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;
    ListView listView;
    Button done;
    UserAdminAdepter adepter;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String currentAccValue;
    List<UserDataGet> userDataGets;
    List<UserDataGet> tempStoreDataValues;
    UserDataGet usrDGet;
    ProgressDialog progressDialog;
    Intent intent;
    GlobalVariable gblv;

    String accessValue = "";
    String finalResult;
    String HttpURL = "http://dert.co.in/gFiles/accessuserdetails.php";
    String HttpURL2 = "http://dert.co.in/gFiles/updateuserdetails.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        userDataGets = new ArrayList<UserDataGet>();
        tempStoreDataValues = new ArrayList<UserDataGet>();
        listView = (ListView) findViewById(R.id.lstv1);
        done = (Button) findViewById(R.id.btn_done);

        gblv = GlobalVariable.getInstance();

        adepter = new UserAdminAdepter(this, userDataGets);
        listView.setAdapter(adepter);
        adepter.notifyDataSetChanged();

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        //Retrieve
        getData();

        //Adepter
        adepter.notifyDataSetChanged();

        //done click listener
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SaveAccess();
            }
        });


        //access listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int clientListViewPos = parent.getPositionForView(view);

                SelectionBox(position);

            }
        });
    }

    private void SelectionBox(int position) {

        usrDGet = userDataGets.get(position);
        currentAccValue = usrDGet.getAccessType();
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Client_Discount.this);
        builder.setTitle("Select Discount Percentage");

        // add a radio button list
        final String[] Percentage = {"5%","10%", "20%", "30%", "40%","50%"};
        boolean[] checkedItems = {false, false, false, false, false, false};

       /* int checkedItem = 0; // cow
        if(currentAccValue.contains("10%")){
            checkedItem = 1;
        }else if (currentAccValue.contains("20%")){
            checkedItem = 2;
        }else if (currentAccValue.contains("30%")){
            checkedItem = 3;
        }else if (currentAccValue.contains("40%")) {
            checkedItem = 4;
        }else if (currentAccValue.contains("50%")) {
            checkedItem = 5;
        }*/

        builder.setMultiChoiceItems(Percentage, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                switch(which){
                    case 0:
                        accessValue = "5%";
                        break;
                    case 1:
                        accessValue = "10%";
                        break;
                    case 2:
                        accessValue = "20%";
                        break;
                    case 3:
                        accessValue = "30%";
                        break;
                    case 4:
                        accessValue = "40%";
                        break;
                    case 5:
                        accessValue = "50%";
                        break;

                }

                usrDGet.setAccessType(accessValue);
            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // user clicked OK

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void SaveAccess() {

        for(int index = 0 ; index < userDataGets.size(); index++){

            UserDataGet tempStoredata = tempStoreDataValues.get(index);
            UserDataGet tmpDataGet = userDataGets.get(index);
            //Get the earlier and current stored strings.
            String accessTypeE = tmpDataGet.getAccessType();
            String accessTypeU = tempStoredata.getAccessType();

            if ( accessTypeE != accessTypeU)
            {
                UserAccessUpdate(tmpDataGet.getEmail(), tmpDataGet.getAccessType());
            }
        }
        finish();
    }

    private void getData(){

        try {
            URL url = new URL(HttpURL);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is = new BufferedInputStream(con.getInputStream());

        }catch (Exception e){
            e.printStackTrace();
        }

        //Read in content into String
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        //Parse json data
        try {

            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            data = new String[ja.length()];

            for (int i=0; i<ja.length();i++){

                jo=ja.getJSONObject(i);
                String email = jo.getString("email");
                String accessType = jo.getString("Access");
                UserDataGet e = new UserDataGet(email);
                UserDataGet tmp = new UserDataGet(email);
                e.setAccessType(accessType);
                tmp.setAccessType(accessType);
                userDataGets.add(e);
                //Store the set values

                tempStoreDataValues.add(tmp);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void UserAccessUpdate( String ClientEmailHolder, String ClientAccessHolder){

        class UserAccessUpdate extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(Client_Discount.this,"Loading Data",null,true,true);

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(Client_Discount.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("access",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL2);

                return finalResult;
            }
        }

        UserAccessUpdate UpdateClass = new UserAccessUpdate();

        UpdateClass.execute( ClientEmailHolder, ClientAccessHolder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        Log.v("Access type ========",gblv.AccessType);

        if(id==R.id.home) {
            if (gblv.AccessType.contains("Admin")) {

                intent = new Intent(this, AdminDashboard.class);

            } else if (gblv.AccessType.contains("Manager")) {

                intent = new Intent(this, UserDashbord.class);

            }


            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
