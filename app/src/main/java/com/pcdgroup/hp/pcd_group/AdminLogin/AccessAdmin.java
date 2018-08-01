package com.pcdgroup.hp.pcd_group.AdminLogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 .
 * @class_name AccessAdmin
 * @description Admin set & change the access type of user.
 */

public class AccessAdmin extends AppCompatActivity implements CallBackInterface {

    //Users List Components
    String[] data;
    List<UserDataGet> userDataGets;
    List<UserDataGet> tempStoreDataValues;
    UserDataGet usrDGet;
    String currentAccValue;
    UserAdminAdepter adepter;

    //Widgets
    ListView listView;

    //Access Value Parameters
    String accessValue = "";

    //Database Query Parameters
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    HashMap<String,String> hashMap = new HashMap<>();
    CallType typeOfQuery;

    /** Populates the screen including the list of users.
     * Queries the Database for the change of access type.
     * @param savedInstanceState object of passing parameters from the previous intent */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminaccess);

        //Display List of users
        userDataGets = new ArrayList<UserDataGet>();
        tempStoreDataValues = new ArrayList<UserDataGet>();
        listView = (ListView) findViewById(R.id.lstv1);

        //Define adapter and set the list view for the users.
        adepter = new UserAdminAdepter(this, userDataGets);
        listView.setAdapter(adepter);
        adepter.notifyDataSetChanged();

        //Assign the query variables for the call type and getting the php file.
        urlQry = DataGetUrl.ACCESS_DETAILS;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                AccessAdmin.this
        );

        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        //access listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int clientListViewPos = parent.getPositionForView(view);

                SelectionBox(position);

            }
        });
    }

    /** After clicking done, save the access type */
    public void onClickDone(View v) {
        SaveAccess();
    }

    /** Adds the access type of the users to the Dialog
     * @param  position -  index of the selected user on the list view */
    private void SelectionBox(int position) {

        //user data for the position
        usrDGet = userDataGets.get(position);
        currentAccValue = usrDGet.getAccessType();

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(AccessAdmin.this);
        builder.setTitle("Select Access Level");

        // add a radio button list
        String[] Client = {"User", "Admin", "Manager", "Client"};
        int checkedItem = 0; // cow
        if(currentAccValue.contains("Admin")){
            checkedItem = 1;
        }else if (currentAccValue.contains("Manager")){
            checkedItem = 2;
        }else if (currentAccValue.contains("Client")){
            checkedItem = 3;
        }

        //Assign access type on selection of the radio button
        builder.setSingleChoiceItems(Client, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        accessValue = "User";
                        break;
                    case 1:
                        accessValue = "Admin";
                        break;
                    case 2:
                        accessValue = "Manager";
                        break;
                    case 3:
                        accessValue = "Client";
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

    /**Queries the database after addition of the type of access of the users
     *              updated by Admin.*/
    private void SaveAccess() {

        //Check the access type for all the users of the list
        for(int index = 0 ; index < userDataGets.size(); index++){

            //Get the previous and current value of UserDataGet - users' data from list.
            UserDataGet tempStoredata = tempStoreDataValues.get(index);
            UserDataGet tmpDataGet = userDataGets.get(index);

            //Get the earlier and current stored strings.
            String accessTypeE = tmpDataGet.getAccessType();
            String accessTypeU = tempStoredata.getAccessType();

            //If the previous and current value do not match, update the access type.
            if (accessTypeE != accessTypeU)
            {
                //Assign the query variables for the call type and getting the php file.
                urlQry = DataGetUrl.UPDATE_ACCESS_USER_DETAILS;
                typeOfQuery = CallType.POST_CALL;

                //Construct Hashmap for email is and access type.
                hashMap.put("email_id",tmpDataGet.getEmail());
                hashMap.put("access",tmpDataGet.getAccessType());

                //Send Database query for inquiring to the database.
                dataBaseQuery = new DataBaseQuery(hashMap,
                        urlQry,
                        typeOfQuery,
                        getApplicationContext(),
                        AccessAdmin.this
                );
                dataBaseQuery.PrepareForQuery();
            }
        }
        finish();
    }
    /** Create menu for returning to home option
     * @param menu - menu item */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    /** Selected Items are
     * @param item - selected menu item */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //get the selection menu item id.
        int id = item.getItemId();

        //If menu item is home - return to home screen.
        if(id==R.id.home) {
            Toast.makeText(this, "Main menu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AccessAdmin.this, AdminDashboard.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    /** CallBack Function for processing the Database query result.
     * @param  response - Response string received while database query.
     *         dataGetUrl - Url queried.*/
    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {

        //Get the details of the access type of the users and store in the adapter.
        if (dataGetUrl.equals(DataGetUrl.ACCESS_DETAILS)) {

            try {
                //Get the JSON Array request.
                JSONArray ja = new JSONArray(response);
                JSONObject jo = null;

                //Get the required data.
                data = new String[ja.length()];

                //Read the data with user names and access types.
                for (int i = 0; i < ja.length();i++){

                    jo=ja.getJSONObject(i);
                    String email = jo.getString("email_id");
                    String accessType = jo.getString("Access");
                    UserDataGet e = new UserDataGet(email);
                    UserDataGet tmp = new UserDataGet(email);
                    e.setAccessType(accessType);
                    tmp.setAccessType(accessType);
                    userDataGets.add(e);
                    //Store the set values

                    tempStoreDataValues.add(tmp);

                    adepter.notifyDataSetChanged();

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {

            Toast.makeText(AccessAdmin.this,response.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /** Releases the memory of all the components after intent finishes. */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        listView = null;
    }
}
