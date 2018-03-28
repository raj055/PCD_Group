package com.pcdgroup.hp.pcd_group.Client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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

public class ClientDetailsActivity extends AppCompatActivity {

    List<DataAdapter> DataAdapters;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter recyclerViewadapter;

    JsonArrayRequest jsonArrayRequest ;

    ArrayList<String> SubjectNames;

    RequestQueue requestQueue ;

    String HttpURL = "http://pcddata-001-site1.1tempurl.com/ClientDataShow.php";

    View ChildView ;

    int RecyclerViewClickedItemPOS ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientdetails);

        DataAdapters = new ArrayList<>();

        SubjectNames = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        // JSON data web call function call from here.
        JSON_WEB_CALL();

        //RecyclerView Item click listener code starts from here.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(ClientDetailsActivity.this, new GestureDetector.SimpleOnGestureListener() {

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

        jsonArrayRequest = new JsonArrayRequest(HttpURL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

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

        recyclerViewadapter = new RecyclerViewAdapter(DataAdapters, this);

        recyclerView.setAdapter(recyclerViewadapter);
    }
}
