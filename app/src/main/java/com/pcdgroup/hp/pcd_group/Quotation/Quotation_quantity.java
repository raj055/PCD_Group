package com.pcdgroup.hp.pcd_group.Quotation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

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
 * @version 1.0 on 28-06-2018.
 * @class_name
 * @description
 */

public class Quotation_quantity extends AppCompatActivity implements CallBackInterface {

    Button next, previous;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_quantity_slide);

        next = (Button) findViewById(R.id.btn_next);
        previous = (Button) findViewById(R.id.btn_previous);

        listView = (ListView) findViewById(R.id.product_listview);

        prodactEntities = new ArrayList<ProdactEntity>();

        brand = getIntent().getExtras().getString("SelectedBrand");
        client = getIntent().getExtras().getString("ClientInfo");

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<ProdactEntity> object = (ArrayList<ProdactEntity>) args.getSerializable("productID");
        List<ProdactEntity> lstPrd = new ArrayList<ProdactEntity>();

        for(int i = 0 ; i < object.size(); i++){
            ProdactEntity m = object.get(i);
            lstPrd.add(m);
        }
//        lstPrd.addAll(object);

        Log.v("displayItem ======= ", String.valueOf(object));

//        listView.setAdapter((ListAdapter) items);

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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Quotation_quantity.this, Quotation_finish.class);

                //customer
                intent.putExtra("ClientInfo", client);
                //brand
                intent.putExtra("SelectedBrand",brand);

                startActivity(intent);
                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Quotation_quantity.this, Quotation_product.class);
                startActivity(intent);
                overridePendingTransition(R.animator.slide_out_left, R.animator.slide_in_right);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
    }

    @Override
    public void ExecuteQueryResult(String response, DataGetUrl dataGetUrl) {
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
