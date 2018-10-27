package com.pcdgroup.hp.pcd_group.Quotation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name
 * @description
 */

public class Quotation_quantity extends AppCompatActivity implements CallBackInterface {

    ListView listView;
    QuontityAdepter adapter;
    String productData,brand,client;
    String[] data;
    List<String> IdList = new ArrayList<>();
    ArrayList<ProdactEntity> prodactEntities;
    ArrayList items = new ArrayList();
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;
    ArrayList<ProdactEntity> QuantityItems = new ArrayList<ProdactEntity>();
    SelectedObject selectedObject;
    ArrayList<ProdactEntity> object;
    String picname,urlname,description;
    Integer price,quantity,hsncode,gst,stock,reorderlevel,id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_quantity_slide);

        listView = (ListView) findViewById(R.id.product_listview);
        prodactEntities = new ArrayList<ProdactEntity>();
        selectedObject = new SelectedObject();

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        object = (ArrayList<ProdactEntity>) args.getSerializable("productID");
        List<ProdactEntity> lstPrd = new ArrayList<ProdactEntity>();

        for(int i = 0 ; i < object.size(); i++){
            ProdactEntity m = object.get(i);
            lstPrd.add(m);
        }

        selectedObject = (SelectedObject) getIntent().getParcelableExtra("Addresses");


        adapter = new QuontityAdepter(this, lstPrd, this);
        listView.setAdapter(adapter);

        urlQry = DataGetUrl.VIEW_PRODUCT;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                Quotation_quantity.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();
    }

    public void onClickNextScreenQuantity(View view) {

        for(int i = 0; i < object.size(); i++){
            ProdactEntity pe = object.get(i);
            QuantityItems.add(pe);

        }

        Log.v("Object ==========", String.valueOf(object));

        Intent intent = new Intent(Quotation_quantity.this, Quotation_finish.class);
        Bundle args = new Bundle();
        args.putSerializable("Quantity",(Serializable) QuantityItems);

        args.putSerializable("productID",(Serializable) QuantityItems);
        intent.putExtra("BUNDLE",args);

        intent.putExtra("Addresses", (Parcelable) selectedObject);

        startActivity(intent);
        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
    }

    public void onClickPreviosScreenQuantity(View view) {
        Intent intent = new Intent(Quotation_quantity.this, Quotation_product.class);
        startActivity(intent);
        overridePendingTransition(R.animator.slide_out_left, R.animator.slide_in_right);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
    }
    /** CallBack Function for processing the Database query result.
     * @param  response - Response string received while database query.
     *         dataGetUrl - Url queried.*/
    @Override
    public void ExecuteQueryResult(String response, DataGetUrl dataGetUrl) {
        try {

            JSONArray ja = new JSONArray(response);
            JSONObject jo = null;

            data = new String[ja.length()];

            for (int i=0; i<ja.length();i++){

                jo=ja.getJSONObject(i);
                picname = jo.getString("name");
                urlname = jo.getString("photo");
                price = jo.getInt("price");
                quantity = jo.getInt("minimum");
                hsncode=jo.getInt("hsncode");
                gst=jo.getInt("gst");
                description=jo.getString("description");
                stock=jo.getInt("stock");
                reorderlevel=jo.getInt("reorderlevel");
                id = jo.getInt("id");

                // Adding Student Id TO IdList Array.
                IdList.add(jo.getString("id").toString());

                ProdactEntity e = new ProdactEntity(picname,urlname,price, quantity,hsncode,gst,description,stock,reorderlevel,id);
                prodactEntities.add(e);

                IdList.add(String.valueOf(e));

                //Adepter
                adapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
