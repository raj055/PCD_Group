package com.pcdgroup.hp.pcd_group.AdminLogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.pcdgroup.hp.pcd_group.Product.CustomListAdapter;
import com.pcdgroup.hp.pcd_group.Product.Entity;
import com.pcdgroup.hp.pcd_group.Quotation.Pdf;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class AccessAdmin extends AppCompatActivity {

    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;
    ListView listView;
    Button done;
    UserAdminAdepter adepter;
    List<UserDataGet> userDataGets;
    String HttpURL = "http://pcddata-001-site1.1tempurl.com/accessuserdetails.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminaccess);

        userDataGets = new ArrayList<UserDataGet>();

        listView = (ListView) findViewById(R.id.lstv);
        done = (Button) findViewById(R.id.btn_done);

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
                SaveAccess();
            }
        });

        //access listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            SelectionBox();

            }
        });
    }

    private void SelectionBox() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(AccessAdmin.this);
        builder.setTitle("Select Client");

        // add a radio button list
        String[] Client = {"Default", "Full Access"};
        int checkedItem = 0; // cow
        builder.setSingleChoiceItems(Client, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
                UserDataGet e = new UserDataGet(email);
                userDataGets.add(e);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
