package com.pcdgroup.hp.pcd_group.Client;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
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
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Product.ProductSingleRecord;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ClientDetailsActivity extends AppCompatActivity implements RecyclerViewAdapter.DataAdapterListener {

    HttpParse httpParse = new HttpParse();
    HashMap<String,String> hashMap = new HashMap<>();
    HashMap<String,String> ResultHash = new HashMap<>();
    String IdHolder;
    ProgressDialog pDialog;
    String FinalJSonObject ;
    String ParseResult ;
    String finalResult ;

    // Http URL for delete Already Open Client Record.
    String HttpUrlDeleteRecord = "http://dert.co.in/gFiles/deleteclient.php";

    ProgressDialog progressDialog2;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    List<DataAdapter> DataAdapters;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter mAdepter;

    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    String HttpURL = "http://dert.co.in/gFiles/ClientDataShow.php";

    View ChildView;

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

        recyclerView.setAdapter(mAdepter);

        actionModeCallback = new ActionModeCallback();

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

                        // adding contacts to contacts list
                        DataAdapters.clear();

                        // refreshing recycler view
                        mAdepter.notifyDataSetChanged();

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error in getting json
                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        MyApplication.getInstance().addToRequestQueue(jsonArrayRequest);

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        Log.e("array", String.valueOf(array.length()));

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
        finish();
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

    @Override
    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdepter.toggleSelection(position);
        int count = mAdepter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void onIconImportantClicked(int position) {
        // Star icon is clicked,
        // mark the message as important
        DataAdapter message = DataAdapters.get(position);
        DataAdapters.set(position, message);
        mAdepter.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (mAdepter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // read the message which removes bold from the row
            DataAdapter message = DataAdapters.get(position);
            DataAdapters.set(position, message);
            mAdepter.notifyDataSetChanged();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_contextual_mode, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages

                    mAdepter.resetAnimationIndex();
                    List<Integer> selectedItemPositions =
                            mAdepter.getSelectedItems();
                    for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                       DataAdapter currRecord = DataAdapters.get(selectedItemPositions.get(i));
                        deleteMessages(currRecord.getId());
                        mAdepter.removeData(selectedItemPositions.get(i));
                    }
                    mAdepter.notifyDataSetChanged();

                    mode.finish();
                    return true;


                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdepter.clearSelections();
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdepter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages(final String ClientID) {

        class ClientDeleteClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog2 = ProgressDialog.show(ClientDetailsActivity.this, "Loading Data",
                        null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog2.dismiss();

                Toast.makeText(ClientDetailsActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                // Sending Client id.
                hashMap.put("id",ClientID);
                finalResult = httpParse.postRequest(hashMap, HttpUrlDeleteRecord);

                return finalResult;
            }
        }

        ClientDeleteClass ClientDeleteClass = new ClientDeleteClass();

        ClientDeleteClass.execute(ClientID);
    }
}