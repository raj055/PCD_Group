package com.pcdgroup.hp.pcd_group.Product;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.ClientRegisterActivity;
import com.pcdgroup.hp.pcd_group.Client.DataAdapter;
import com.pcdgroup.hp.pcd_group.Client.SingleRecordShow;
import com.pcdgroup.hp.pcd_group.MainActivity;
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

public class ViewImage extends AppCompatActivity {

    ListView listView;
    CustomListAdapter adapter;
    String HttpURL1 = "http://pcddata-001-site1.1tempurl.com/fimage.php";
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;
    ArrayList<String> picNames;
    String recordName,EmailHolders,user;
    List<Entity> localEntity;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image_layout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewImage.this,UploadImage.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        EmailHolders = intent.getStringExtra("email");

        localEntity = new ArrayList<Entity>();
        recordName = new String("");
        picNames = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.lstv);

        adapter = new CustomListAdapter(this, localEntity);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        //Retrieve
        getData();

        //Adepter
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ViewImage.this,ProductSingleRecord.class);

                Entity productdata = localEntity.get(position);

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

                startActivity(intent);
            }
        });

        if (EmailHolders == user){
            fab.setVisibility(View.INVISIBLE);
        }

    }

    private void getData(){

        try {
            URL url = new URL(HttpURL1);
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
                picNames.add(picname);
                Entity e = new Entity(picname,urlname,price,gst, minimum,hsncode,brand,description,stock,reorderlevel,id);
                localEntity.add(e);
            }

        }catch (Exception e){
            e.printStackTrace();
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
//                mAdepter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
//                mAdepter.getFilter().filter(query);

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
}
