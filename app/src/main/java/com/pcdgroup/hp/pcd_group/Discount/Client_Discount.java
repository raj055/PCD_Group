package com.pcdgroup.hp.pcd_group.Discount;

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

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.AdminLogin.UserAdminAdepter;
import com.pcdgroup.hp.pcd_group.AdminLogin.UserDataGet;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name Client_Discount
 * @description admin set how many get discount get client
 */

public class Client_Discount extends AppCompatActivity implements CallBackInterface {

    String[] data;
    ListView listView;
    UserAdminAdepter adepter;
    HashMap<String,String> hashMap = new HashMap<>();
    String currentDicountValue;
    List<UserDataGet> userDataGets;
    List<UserDataGet> tempStoreDataValues;
    UserDataGet usrDGet;
    Intent intent;
    GlobalVariable gblv;
    String discountValue = "";
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    /** Lists users / clients for setting the discount value.
     * @param savedInstanceState object of passing parameters from the previous intent */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        // list of user database
        // set discount to user to create new quotation to another

        userDataGets = new ArrayList<UserDataGet>();
        tempStoreDataValues = new ArrayList<UserDataGet>();
        listView = (ListView) findViewById(R.id.lstv1);

        gblv = GlobalVariable.getInstance();

        adepter = new UserAdminAdepter(this, userDataGets);
        listView.setAdapter(adepter);
        adepter.notifyDataSetChanged();

        urlQry = DataGetUrl.ACCESS_DETAILS;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                Client_Discount.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();


        //Adepter
        adepter.notifyDataSetChanged();

        //access listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int clientListViewPos = parent.getPositionForView(view);

                SelectionBox(position);

            }
        });
    }

    public void onClickClientDiscount(View v) {
        SaveDiscount();
    }

    private void SelectionBox(int position) {

        usrDGet = userDataGets.get(position);
        currentDicountValue = usrDGet.getAccessType();

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Client_Discount.this);
        builder.setTitle("Select Discount Level");

        // add a radio button list
        String[] Client = {"5","10", "20", "30", "40","50"};
        int checkedItem = 0; // cow
        if(currentDicountValue.contains("10")){
            checkedItem = 1;
        }else if (currentDicountValue.contains("20")){
            checkedItem = 2;
        }else if (currentDicountValue.contains("30")){
            checkedItem = 3;
        }else if (currentDicountValue.contains("40")){
            checkedItem = 4;
        }else if (currentDicountValue.contains("50")){
            checkedItem = 5;
        }

        builder.setSingleChoiceItems(Client, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        discountValue = "5";
                        break;
                    case 1:
                        discountValue = "10";
                        break;
                    case 2:
                        discountValue = "20";
                        break;
                    case 3:
                        discountValue = "30";
                        break;
                    case 4:
                        discountValue = "40";
                        break;
                    case 5:
                        discountValue = "50";
                        break;
                }

                usrDGet.setAccessType(discountValue);

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

    private void SaveDiscount() {

        for(int index = 0 ; index < userDataGets.size(); index++){

            UserDataGet tempStoredata = tempStoreDataValues.get(index);
            UserDataGet tmpDataGet = userDataGets.get(index);
            //Get the earlier and current stored strings.
            String accessTypeE = tmpDataGet.getAccessType();
            String accessTypeU = tempStoredata.getAccessType();

            if ( accessTypeE != accessTypeU)
            {
                urlQry = DataGetUrl.DISCOUNT_DETAILS;
                typeOfQuery = CallType.POST_CALL;

                hashMap.put("email",tmpDataGet.getEmail());

                hashMap.put("discount",tmpDataGet.getAccessType());

                //Send Database query for inquiring to the database.
                dataBaseQuery = new DataBaseQuery(hashMap,
                        urlQry,
                        typeOfQuery,
                        getApplicationContext(),
                        Client_Discount.this
                );
                //Prepare for the database query
                dataBaseQuery.PrepareForQuery();

            }
        }
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.home) {
            if (gblv.AccessType.contains("Admin")) {

                intent = new Intent(this, AdminDashboard.class);

            } else if (gblv.AccessType.contains("Manager")) {

                intent = new Intent(this, AdminDashboard.class);

            }

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

        if (dataGetUrl.equals(DataGetUrl.ACCESS_DETAILS)) {

            try {

                JSONArray ja = new JSONArray(response);
                JSONObject jo = null;

                data = new String[ja.length()];

                for (int i=0; i<ja.length();i++){

                    jo=ja.getJSONObject(i);
                    String email = jo.getString("email_id");
                    String accessType = jo.getString("Discount");
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

        } else {
            Toast.makeText(Client_Discount.this, response.toString(), Toast.LENGTH_LONG).show();
        }
    }
    /** Releases the memory of all the components after intent finishes. */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        listView = null;
    }
}
