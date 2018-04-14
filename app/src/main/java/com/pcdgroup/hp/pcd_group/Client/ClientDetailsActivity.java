package com.pcdgroup.hp.pcd_group.Client;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ClientDetailsActivity extends AppCompatActivity implements RecyclerViewAdapter.DataAdapterListener {

    private static final String TAG = ClientDetailsActivity.class.getSimpleName();

    List<DataAdapter> DataAdapters;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter mAdepter;

    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    String HttpURL = "http://pcddata-001-site1.1tempurl.com/ClientDataShow.php";

    View ChildView ;

    SearchView searchView;

    int RecyclerViewClickedItemPOS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientdetails);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClientDetailsActivity.this,ClientRegisterActivity.class);

                startActivity(intent);
            }
        });


        DataAdapters = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView1);
        mAdepter = new RecyclerViewAdapter(this, DataAdapters, this);

        recyclerView.setHasFixedSize(true);


        // white background notification bar
        whiteNotificationBar(recyclerView);

        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdepter);

        // JSON data web call function call from here.
        JSON_WEB_CALL();

        //RecyclerView Item click listener code starts from here.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(ClientDetailsActivity.this,
                    new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting RecyclerView Clicked item value.
                    RecyclerViewClickedItemPOS = Recyclerview.getChildAdapterPosition(ChildView);

                    Intent intent = new Intent(ClientDetailsActivity.this,SingleRecordShow.class);

                    Integer id = Recyclerview.getChildAdapterPosition(ChildView);
                    DataAdapter cldata = DataAdapters.get(id);
                    intent.putExtra("id", cldata.getId());
                    intent.putExtra("name", cldata.getName());
                    intent.putExtra("address", cldata.getAddress());
                    intent.putExtra("addressline1", cldata.getAddressline1());
                    intent.putExtra("addressline2", cldata.getAddressline2());
                    intent.putExtra("mobileno", cldata.getMobileno());
                    intent.putExtra("pin", cldata.getPin());
                    intent.putExtra("state", cldata.getState());
                    intent.putExtra("country", cldata.getcountry());
                    intent.putExtra("company", cldata.getCompanyname());
                    intent.putExtra("email_id", cldata.getEmailid());
                    intent.putExtra("designation", cldata.getDesignation());

                    startActivity(intent);

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    public void JSON_WEB_CALL(){

        jsonArrayRequest  = new JsonArrayRequest(HttpURL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<DataAdapter> items = new Gson().fromJson(response.toString(), new TypeToken<List<DataAdapter>>() {
                        }.getType());

                        // adding contacts to contacts list
                        DataAdapters.clear();
                        DataAdapters.addAll(items);

                        // refreshing recycler view
                        mAdepter.notifyDataSetChanged();

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error in getting json
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        MyApplication.getInstance().addToRequestQueue(jsonArrayRequest);

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            DataAdapter GetData = new DataAdapter();

            JSONObject json = null;

            try {
                json = array.getJSONObject(i);
                GetData.setId(json.getString("id"));
                GetData.setName(json.getString("name"));
                GetData.setType(json.getString("type"));
                GetData.setAddress(json.getString("address"));
                GetData.setaddresline1(json.getString("addressline1"));
                GetData.setAddressline2(json.getString("addressline2"));
                GetData.setMobileno(json.getString("mobileno"));
                GetData.setState(json.getString("state"));
                GetData.setCountry(json.getString("country"));
                GetData.setCompanyname(json.getString("company"));
                GetData.setPin(json.getString( "pin"));
                GetData.setEmailid(json.getString("email_id"));
                GetData.setDesignation(json.getString("designation"));

            }
            catch (JSONException e)
            {

                e.printStackTrace();
            }

            DataAdapters.add(GetData);
        }

        mAdepter = new RecyclerViewAdapter(this, DataAdapters, this);

        recyclerView.setAdapter(mAdepter);
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
                mAdepter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdepter.getFilter().filter(query);

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
    public void onDataSelected(DataAdapter dataAdapter) {

    }
}
