package com.pcdgroup.hp.pcd_group.Quotation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.pcdgroup.hp.pcd_group.Product.CustomListAdapter;
import com.pcdgroup.hp.pcd_group.Product.Entity;
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
 * @class_name SelectProduct
 * @description product list to select product in quotation
 */

public class SelectProduct  extends AppCompatActivity implements ProductCustomListAdapter.DataAdapterListener ,
        CallBackInterface {

    ListView listView;
    ProductCustomListAdapter adapter;
    String[] data;
    ArrayList<String> picNames;
    String recordName;
    List<ProdactEntity> prodactEntities;
    List<String> IdList = new ArrayList<>();
    SearchView searchView;
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        /*
            - selected product to create new invoice
        */

        prodactEntities = new ArrayList<ProdactEntity>();
        recordName = new String("");
        picNames = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.lstv);

        // white background notification bar
        whiteNotificationBar(listView);

        adapter = new ProductCustomListAdapter(this, prodactEntities, this);
        listView.setAdapter(adapter);

        //Adding ListView Item click Listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                Intent intent = new Intent();

                // Sending ListView clicked value using intent.
                ProdactEntity pcdata = prodactEntities.get((int) id);
                intent.putExtra("pname", pcdata.gettitle());
                intent.putExtra("phsn", pcdata.getHsncode());
                intent.putExtra("pgst", pcdata.getGst());
                intent.putExtra("pprice", pcdata.getPrice());
                setResult(RESULT_OK, intent);

                finish();

            }
        });

        urlQry = DataGetUrl.VIEW_PRODUCT;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                SelectProduct.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();


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

    @Override
    public void onDataSelected(Entity dataAdapter) {

    }

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
                Integer price = jo.getInt("price");
                Integer quantity = jo.getInt("minimum");
                Integer hsncode=jo.getInt("hsncode");
                Integer gst=jo.getInt("gst");
                String description=jo.getString("description");
                Integer stock=jo.getInt("stock");
                Integer reorderlevel=jo.getInt("reorderlevel");
                Integer id = jo.getInt("id");

                // Adding Student Id TO IdList Array.
                IdList.add(jo.getString("id").toString());

                picNames.add(picname);
                ProdactEntity e = new ProdactEntity(picname,urlname,price, quantity,hsncode,gst,description,stock,reorderlevel,id);
                prodactEntities.add(e);

                //Adepter
                adapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
