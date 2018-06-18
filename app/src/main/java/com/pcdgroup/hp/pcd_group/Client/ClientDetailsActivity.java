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
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
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
 * @version 1.0 on 28-03-2018.
 */

public class ClientDetailsActivity extends AppCompatActivity
  implements CallBackInterface , RecyclerViewAdapter.DataAdapterListener {


    HttpParse httpParse = new HttpParse();
    HashMap<String, String> hashMap = new HashMap<>();
    String finalResult;

    // Http URL for delete Already Open Client Record.
    String HttpUrlDeleteRecord = "http://dert.co.in/gFiles/deletemultiple.php";

    //
    ProgressDialog progressDialog2;

    //
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    List<DataAdapter> DataAdapters;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter mAdepter;

    View ChildView;

    SearchView searchView;

    int RecyclerViewClickedItemPOS;

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;


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
        urlQry = DataGetUrl.GET_CLIENT_DETAILS;

        typeOfQuery = CallType.JSON_CALL;


        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
          urlQry,
          typeOfQuery,
          getApplicationContext(),
          ClientDetailsActivity.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();

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
                    intent.putExtra("first_name", cldata.getfName());
                    intent.putExtra("last_name", cldata.getlName());
                    intent.putExtra("address", cldata.getAddress());
                    intent.putExtra("address_line1", cldata.getAddressline1());
                    intent.putExtra("address_line2", cldata.getAddressline2());
                    intent.putExtra("mobile_num", cldata.getMobileno());
                    intent.putExtra("pin", cldata.getPin());
                    intent.putExtra("state", cldata.getState());
                    intent.putExtra("country", cldata.getcountry());
                    intent.putExtra("company_name", cldata.getCompanyname());
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
    @Override
    public void ExecuteQueryResult(String response){
      //adding contacts to contacts list
      DataAdapters.clear();

      //refreshing recycler view
      mAdepter.notifyDataSetChanged();

      try {

        JSONArray array = new JSONArray(response);
        for (int i = 0; i < array.length(); i++) {
          Log.e("array", String.valueOf(array.length()));
          DataAdapter GetData = new DataAdapter();

          JSONObject json = null;

          json = array.getJSONObject(i);
          GetData.setId(json.getString("id"));
          GetData.setfName(json.getString("first_name"));
          GetData.setlName(json.getString("last_name"));
          GetData.setType(json.getString("type"));
          GetData.setAddress(json.getString("address"));
          GetData.setaddresline1(json.getString("address_line1"));
          GetData.setAddressline2(json.getString("address_line2"));
          GetData.setMobileno(json.getString("mobile_num"));
          GetData.setState(json.getString("state"));
          GetData.setCountry(json.getString("country"));
          GetData.setCompanyname(json.getString("company_name"));
          GetData.setPin(json.getString("pin"));
          GetData.setEmailid(json.getString("email_id"));
          GetData.setDesignation(json.getString("designation"));
            DataAdapters.add(GetData);
        }
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }


      mAdepter = new RecyclerViewAdapter(this, DataAdapters, this);

      recyclerView.setAdapter(mAdepter);
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
                       String str = currRecord.getId();
                       String key = "id[" + i + "]";
                        hashMap.put(key, str);
                        mAdepter.removeData(selectedItemPositions.get(i));
                    }
                    deleteMessages("");
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
                finalResult = httpParse.postRequest(hashMap, HttpUrlDeleteRecord);

                return finalResult;
            }
        }

        ClientDeleteClass ClientDeleteClass = new ClientDeleteClass();

        ClientDeleteClass.execute(ClientID);
    }
}