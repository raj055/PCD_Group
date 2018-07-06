package com.pcdgroup.hp.pcd_group.Product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ProductSingleRecord
 * @description show full product single record
 */
public class ProductSingleRecord extends AppCompatActivity implements CallBackInterface {

    HashMap<String,String> hashMap = new HashMap<>();
    HashMap<String,String> ResultHash = new HashMap<>();
    String FinalJSonObject ;
    String IdHolder,NameHolder, PriceHolder, MinimumHolder,HsnHolder,BrandHolder,DescriptionHolder,
            StockHolder, RecordlevelHolder,GstHolder;
    Button UpdateButton, DeleteButton;

    private TextView TextViewName, TextViewPrice, TextviewMinimum, TextviewHsn, TextviewBrand,
            TextviewDescription, TextviewStock, TextViewRecordlevel, TextviewGst;

    String EmailHolders;
    Intent intent;
    GlobalVariable gblVar;

    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productrecord_activity);

        /*
            - display single record
            - this record update and delete button show
        */

        gblVar = GlobalVariable.getInstance();

        final Intent intent = getIntent();
        EmailHolders = intent.getStringExtra("email");

        TextViewName = (TextView) findViewById(R.id.p_name) ;
        TextViewPrice = (TextView) findViewById(R.id.p_price) ;
        TextviewMinimum = (TextView) findViewById(R.id.p_minimum) ;
        TextviewHsn = (TextView) findViewById(R.id.p_hsn) ;
        TextviewBrand = (TextView) findViewById(R.id.p_brand) ;
        TextviewDescription = (TextView) findViewById(R.id.p_descrition) ;
        TextviewStock = (TextView) findViewById(R.id.p_stock) ;
        TextViewRecordlevel = (TextView) findViewById(R.id.p_recordlevel) ;
        TextviewGst = (TextView) findViewById(R.id.p_gst) ;

        UpdateButton = (Button)findViewById(R.id.btn_edit_record);
        DeleteButton = (Button)findViewById(R.id.btn_delete_record);

        //Receiving the ListView Clicked item value send by previous activity.
        IdHolder = getIntent().getStringExtra("id");
        NameHolder = getIntent().getStringExtra("name");
        PriceHolder = getIntent().getStringExtra("price");
        MinimumHolder = getIntent().getStringExtra("minimum");
        HsnHolder = getIntent().getStringExtra("hsncode");
        BrandHolder = getIntent().getStringExtra("brand");
        DescriptionHolder = getIntent().getStringExtra("description");
        StockHolder = getIntent().getStringExtra("stock");
        RecordlevelHolder = getIntent().getStringExtra("reorderlevel");
        GstHolder = getIntent().getStringExtra("gst");


        // Setting product Name, Phone Number, Class into TextView after done all process .
        TextViewName.setText(NameHolder);
        TextViewPrice.setText(PriceHolder);
        TextviewMinimum.setText(MinimumHolder);
        TextviewHsn.setText(HsnHolder);
        TextviewBrand.setText(BrandHolder);
        TextviewDescription.setText(DescriptionHolder);
        TextviewStock.setText(StockHolder);
        TextViewRecordlevel.setText(RecordlevelHolder);
        TextviewGst.setText(GstHolder);

        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProductSingleRecord.this,ProductUpdate.class);

                // Sending product Id, Name, Number and Class to next UpdateActivity.
                intent.putExtra("id",IdHolder);
                intent.putExtra("name", NameHolder);
                intent.putExtra("price", PriceHolder);
                intent.putExtra("minimum", MinimumHolder);
                intent.putExtra("hsncode", HsnHolder);
                intent.putExtra("brand", BrandHolder);
                intent.putExtra("description", DescriptionHolder);
                intent.putExtra("stock", StockHolder);
                intent.putExtra("reorderlevel", RecordlevelHolder);
                intent.putExtra("gst", GstHolder);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);

                // Finishing current activity after opening next activity.
                finish();

            }
        });

        // Add Click listener on Delete button.
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                urlQry = DataGetUrl.DELETE_PRODUCTS;
                typeOfQuery = CallType.POST_CALL;

                hashMap.put("id",IdHolder);

                //Send Database query for inquiring to the database.
                dataBaseQuery = new DataBaseQuery(hashMap,
                        urlQry,
                        typeOfQuery,
                        getApplicationContext(),
                        ProductSingleRecord.this
                );
                //Prepare for the database query
                dataBaseQuery.PrepareForQuery();

                Intent intent = new Intent(ProductSingleRecord.this,ViewImage.class);
                startActivity(intent);

            }
        });

        if (gblVar.AccessType.contains("Manager")){
            UpdateButton.setVisibility(View.INVISIBLE);
            DeleteButton.setVisibility(View.INVISIBLE);
        }
        else if (gblVar.AccessType.contains("Client")){
            UpdateButton.setVisibility(View.INVISIBLE);
            DeleteButton.setVisibility(View.INVISIBLE);
        } else if (gblVar.AccessType.contains("User")){
            UpdateButton.setVisibility(View.INVISIBLE);
            DeleteButton.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.home) {
            if (gblVar.AccessType.contains("Admin")) {

                intent = new Intent(this, AdminDashboard.class);

            }
            else if (gblVar.AccessType.contains("Manager")) {

                intent = new Intent(this, AdminDashboard.class);

            }
            else if (gblVar.AccessType.contains("Client")) {

                intent = new Intent(this, AdminDashboard.class);

            }
           else {

                intent = new Intent(this, ViewImage.class);
            }

            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {

        Toast.makeText(ProductSingleRecord.this, response.toString(), Toast.LENGTH_LONG).show();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        TextViewName = null;
        TextViewPrice = null;
        TextviewMinimum = null;
        TextviewHsn = null;
        TextviewBrand = null;
        TextviewDescription = null;
        TextviewStock = null;
        TextViewRecordlevel = null;
        TextviewGst = null;
        UpdateButton = null;
        DeleteButton = null;
    }
}
