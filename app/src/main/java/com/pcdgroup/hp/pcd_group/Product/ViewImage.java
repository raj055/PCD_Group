package com.pcdgroup.hp.pcd_group.Product;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminSetting;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Quotation.ViewInvoice;
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
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ViewImage
 * @description view image to show a product data and image to list view
 */

public class ViewImage extends AppCompatActivity implements CallBackInterface {

    //Components of the Products view
    ListView listView;
    CustomListAdapter adapter;
    String[] data;
    ArrayList<String> picNames;
    String recordName,EmailHolders;
    List<Entity> localEntity;
    SearchView searchView;

    //Global Variables
    GlobalVariable gblVar;

    //Database Components
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    /** Creates and shows list of products.
     * @param savedInstanceState object of passing parameters from the previous intent */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image_layout);

            // show image in list view
            // product details shoe in card view

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewImage.this,UploadImage.class);
                startActivityForResult(intent, 1);

            }
        });

        Intent intent = getIntent();
        EmailHolders = intent.getStringExtra("email");

        gblVar = GlobalVariable.getInstance();


        localEntity = new ArrayList<Entity>();
        recordName = new String("");
        picNames = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.lstv);

        //Find the access type of the user and set visibility of the addition button.
        if (gblVar.AccessType.contains("Manager")){
            fab.setVisibility(View.INVISIBLE);
            listView.setClickable(false);
        }
        else if (gblVar.AccessType.contains("Client")){
            fab.setVisibility(View.INVISIBLE);
            listView.setClickable(false);
        } else if (gblVar.AccessType.contains("User")){
            fab.setVisibility(View.INVISIBLE);
            listView.setClickable(false);
        }

        // white background notification bar
        whiteNotificationBar(listView);

        adapter = new CustomListAdapter(this, localEntity,this);
        listView.setAdapter(adapter);

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        urlQry = DataGetUrl.VIEW_PRODUCT;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                ViewImage.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

        //Adepter
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ViewImage.this,ProductSingleRecord.class);

                Entity productdata = localEntity.get(position);

                //Send the data to the next intent.
                intent.putExtra("id",productdata.getId());
                intent.putExtra("name",productdata.getTitle());
                intent.putExtra("price", productdata.getPrice());
                intent.putExtra("minimum", productdata.getMinimum());
                intent.putExtra("hsncode", productdata.getHsncode());
                intent.putExtra("brand", productdata.getBrand());
                intent.putExtra("description", productdata.getDescription());
                intent.putExtra("stock", productdata.getstock());
                intent.putExtra("reorderlevel", productdata.getReorderlevel());
                intent.putExtra("gst", productdata.getGst());

                intent.putExtra("email",EmailHolders);

                //Start the next activity.
                startActivityForResult(intent, 2);
                finish();

            }
        });

    }

    /** Get the product details after starting the details of the .*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Client Details
                    if (extras.containsKey("name")) {
                        gblVar.GlobalImageUpload[0] = extras.getString("name");
                        gblVar.GlobalImageUpload[1] = extras.getString("photo");
                        gblVar.GlobalImageUpload[2] = extras.getString("price");
                        gblVar.GlobalImageUpload[3] = extras.getString("minimum");
                        gblVar.GlobalImageUpload[4] = extras.getString("hsncode");
                        gblVar.GlobalImageUpload[5] = extras.getString("brand");
                        gblVar.GlobalImageUpload[6] = extras.getString("description");
                        gblVar.GlobalImageUpload[7] = extras.getString("stock");
                        gblVar.GlobalImageUpload[8] = extras.getString("reorderlevel");
                        gblVar.GlobalImageUpload[9] = extras.getString("gst");
                    }
                }
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Client Details
                    if (extras.containsKey("nameRecord")) {
                        gblVar.GlobalImageSingleRecord[0] = extras.getString("nameRecord");
                        gblVar.GlobalImageSingleRecord[1] = extras.getString("price");
                        gblVar.GlobalImageSingleRecord[2] = extras.getString("minimum");
                        gblVar.GlobalImageSingleRecord[3] = extras.getString("hsncode");
                        gblVar.GlobalImageSingleRecord[4] = extras.getString("brand");
                        gblVar.GlobalImageSingleRecord[5] = extras.getString("description");
                        gblVar.GlobalImageSingleRecord[6] = extras.getString("stock");
                        gblVar.GlobalImageSingleRecord[7] = extras.getString("reorderlevel");
                        gblVar.GlobalImageSingleRecord[8] = extras.getString("gst");
                    }
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
    /** CallBack Function for processing the Database query result.
     * @param  response - Response string received while database query.
     *         dataGetUrl - Url queried.*/
    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
        try {

            JSONArray ja = new JSONArray(response);
            JSONObject jo = null;

            data = new String[ja.length()];

            for (int i=0; i<ja.length();i++){

                jo=ja.getJSONObject(i);
                String picname = jo.getString("name");
                String urlname = jo.getString("photo");
                String price = jo.getString("price");
                String minimum = jo.getString("minimum");
                String hsncode=jo.getString("hsncode");
                String gst = jo.getString("gst");
                String brand=jo.getString("brand");
                String description=jo.getString("description");
                String stock=jo.getString("stock");
                String reorderlevel=jo.getString("reorderlevel");
                String id = jo.getString("id");
                adapter.notifyDataSetChanged();
                picNames.add(picname);
                Entity e = new Entity(picname,urlname,price,gst, minimum,hsncode,brand,description,stock,reorderlevel,id);
                localEntity.add(e);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /** Releases the memory of all the components after intent finishes. */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        listView = null;
    }
}
