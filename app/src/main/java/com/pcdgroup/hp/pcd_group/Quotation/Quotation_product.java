package com.pcdgroup.hp.pcd_group.Quotation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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

public class Quotation_product extends AppCompatActivity implements CallBackInterface {

    GridView gridView;
    GridListAdapter adapter;
    String[] data;
    ArrayList<String> picNames;
    String recordName,brand,client;
    List<ProdactEntity> prodactEntities;
    List<String> IdList = new ArrayList<>();
    HashMap<String,String> hashMap = new HashMap<>();
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;
    ArrayList<ProdactEntity> Items = new ArrayList<ProdactEntity>();

    SelectedObject selectedObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_product_slide);

        gridView = (GridView) findViewById(R.id.grid_view);

        prodactEntities = new ArrayList<ProdactEntity>();

        selectedObject = new SelectedObject();

        selectedObject = (SelectedObject) getIntent().getParcelableExtra("Addresses");

        //Check data
       /* String brandName = selectedObject.brandAddress.get(0);

        String ClientName = selectedObject.address.get(7);

        Log.v("selected brand ==", String.valueOf(brandName));
        Log.v("selected client ==", String.valueOf(ClientName));*/
        //--

        recordName = new String("");
        picNames = new ArrayList<String>();

        adapter = new GridListAdapter(this, prodactEntities, this);
        gridView.setAdapter(adapter);

        urlQry = DataGetUrl.VIEW_PRODUCT;
        typeOfQuery = CallType.JSON_CALL;

        //Send Database query for inquiring to the database.
        dataBaseQuery = new DataBaseQuery(hashMap,
                urlQry,
                typeOfQuery,
                getApplicationContext(),
                Quotation_product.this
        );
        //Prepare for the database query
        dataBaseQuery.PrepareForQuery();
    }

    public void onClickNextProductQuotation(View view) {

        SparseBooleanArray selectedRows = adapter.getSelectedIds();

        if (selectedRows.size() > 0) {

            //Loop to all the selected rows array
            for (int i = 0; i < selectedRows.size(); i++) {

                //Check if selected rows have value i.e. checked item
                if (selectedRows.valueAt(i)) {

                    ProdactEntity m = prodactEntities.get(selectedRows.keyAt(i));

                    Items.add(m);
                }
            }

            Intent intent = new Intent(Quotation_product.this, Quotation_quantity.class);

            Bundle args = new Bundle();
            args.putSerializable("productID",(Serializable) Items);
            intent.putExtra("BUNDLE",args);

            intent.putExtra("Addresses", (Parcelable) selectedObject);
            startActivity(intent);
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
        }
    }

    public void onClickPreviousProductQuotation(View view) {
        Intent intent = new Intent(Quotation_product.this, CreateQuotation.class);
        startActivity(intent);
        overridePendingTransition(R.animator.slide_out_left, R.animator.slide_in_right);
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


                picNames.add(picname);
                ProdactEntity e = new ProdactEntity(picname,urlname,price, quantity,hsncode,gst,description,stock,reorderlevel,id);
                prodactEntities.add(e);

//                IdList.add(String.valueOf(e));

                //Adepter
                adapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
