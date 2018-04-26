package com.pcdgroup.hp.pcd_group.Product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.Client.UpdateActivity;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Quotation.CreateQuotation;
import com.pcdgroup.hp.pcd_group.Quotation.Invoice;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ProductSingleRecord extends AppCompatActivity {

    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    // Http Url For Filter product Data from Id Sent from previous activity.
    String HttpURL = "http://dert.co.in/gFiles/filterproductdata.php";

    // Http URL for delete Already Open product Record.
    String HttpUrlDeleteRecord = "http://dert.co.in/gFiles/deleteproduct.php";

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    String ParseResult ;
    HashMap<String,String> ResultHash = new HashMap<>();
    String FinalJSonObject ;
    String IdHolder,NameHolder, PriceHolder, MinimumHolder,HsnHolder,BrandHolder,DescriptionHolder,
            StockHolder, RecordlevelHolder,GstHolder;
    Button UpdateButton, DeleteButton;
    ProgressDialog progressDialog2;

    public TextView TextViewName;
    public TextView TextViewPrice;
    public TextView TextviewMinimum;
    public TextView TextviewHsn;
    public TextView TextviewBrand;
    public TextView TextviewDescription;
    public TextView TextviewStock;
    public TextView TextViewRecordlevel;
    public TextView TextviewGst;

    String EmailHolders,user;

    Intent intent;
    GlobalVariable gblVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productrecord_activity);

        gblVar = GlobalVariable.getInstance();

        Intent intent = getIntent();
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

        //Calling method to filter product Record and open selected record.
        HttpWebCall(IdHolder);


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

                startActivity(intent);

                // Finishing current activity after opening next activity.
                finish();

            }
        });

        // Add Click listener on Delete button.
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling product delete method to delete current record using product ID.
                productDelete(IdHolder);

            }
        });

        if (EmailHolders == user){
            UpdateButton.setVisibility(View.INVISIBLE);
            DeleteButton.setVisibility(View.INVISIBLE);
        }

    }

    // Method to Delete product Record
    public void productDelete(final String productID) {

        class productDeleteClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog2 = ProgressDialog.show(ProductSingleRecord.this, "Loading Data",
                        null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog2.dismiss();

                Toast.makeText(ProductSingleRecord.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                // Sending product id.
                hashMap.put("id",IdHolder);

                finalResult = httpParse.postRequest(hashMap, HttpUrlDeleteRecord);

                return finalResult;
            }
        }

        productDeleteClass productDeleteClass = new productDeleteClass();

        productDeleteClass.execute(productID);
    }


    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem){

        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ProductSingleRecord.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg ;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new ProductSingleRecord.GetHttpResponse(ProductSingleRecord.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("id",IdHolder);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }


    // Parsing Complete JSON Object.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                if(FinalJSonObject != null)
                {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(FinalJSonObject);

                        JSONObject jsonObject;

                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            // Storing product Name, Phone Number, Class into Variables.
                            IdHolder =  jsonObject.getString("id").toString() ;
                            NameHolder = jsonObject.getString("name").toString() ;
                            PriceHolder = jsonObject.getString("price").toString() ;
                            MinimumHolder = jsonObject.getString("minimum").toString() ;
                            HsnHolder = jsonObject.getString("hsncode").toString() ;
                            BrandHolder = jsonObject.getString("brand").toString() ;
                            DescriptionHolder = jsonObject.getString("description").toString() ;
                            StockHolder = jsonObject.getString("stock").toString() ;
                            RecordlevelHolder = jsonObject.getString("reorderlevel").toString() ;
                            GstHolder = jsonObject.getString("gst").toString() ;
                        }
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
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
            if (gblVar.admin.contains("Admin")) {

                intent = new Intent(this, AdminDashboard.class);

            }else {

                intent = new Intent(this, UserDashbord.class);
            }

            Toast.makeText(this, "Main menu", Toast.LENGTH_SHORT).show();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
}
