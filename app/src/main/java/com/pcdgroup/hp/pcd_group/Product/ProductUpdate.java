package com.pcdgroup.hp.pcd_group.Product;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.R;

import java.util.HashMap;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ProductUpdate extends AppCompatActivity {

    String HttpURL = "http://dert.co.in/gFiles/updateproductdetails.php";
    ProgressDialog progressDialog;
    String finalResult;
    Boolean CheckEditText;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    EditText productName, productPrice,productMinimum,productHsncode,productBrand,productDescription,
            productStock, productRecordlevel, productgst;
    Button UpdateProduct;
    String productIdHolder,productNameHolder,productPriceHolder,productMinimumHolder,productHsncodeHolder,
            productBeandHolder,productDescriptionHolder,productStockHolder, productRecordlevelHolder,
            productGstHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_product);

        productName = (EditText)findViewById(R.id.editText_name);
        productPrice = (EditText)findViewById(R.id.editText_price);
        productMinimum = (EditText)findViewById(R.id.editText_minumum);
        productHsncode = (EditText)findViewById(R.id.editText_hsn);
        productBrand = (EditText)findViewById(R.id.editText_brand);
        productDescription = (EditText)findViewById(R.id.editText_deskription);
        productStock = (EditText)findViewById(R.id.editText_stock);
        productRecordlevel = (EditText)findViewById(R.id.editText_record);
        productgst= (EditText)findViewById(R.id.editText_gst);

        UpdateProduct = (Button)findViewById(R.id.btn_submit);

        // Receive product ID, Name , Address , Email, etc.. Send by previous ShowSingleRecordActivity.
        productIdHolder = getIntent().getStringExtra("id");
        productNameHolder = getIntent().getStringExtra("name");
        productPriceHolder = getIntent().getStringExtra("price");
        productMinimumHolder = getIntent().getStringExtra("minimum");
        productHsncodeHolder = getIntent().getStringExtra("hsncode");
        productBeandHolder = getIntent().getStringExtra("brand");
        productDescriptionHolder = getIntent().getStringExtra("description");
        productStockHolder = getIntent().getStringExtra("stock");
        productRecordlevelHolder = getIntent().getStringExtra("reorderlevel");
        productGstHolder = getIntent().getStringExtra("gst");

        // Setting Received Student Name, Phone Number, Class into EditText.
        productName.setText(productNameHolder);
        productPrice.setText(productPriceHolder);
        productMinimum.setText(productMinimumHolder);
        productHsncode.setText(productHsncodeHolder);
        productBrand.setText(productBeandHolder);
        productDescription.setText(productDescriptionHolder);
        productStock.setText(productStockHolder);
        productRecordlevel.setText(productRecordlevelHolder);
        productgst.setText(productGstHolder);

        // Adding click listener to update button .
        UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting data from EditText after button click.
                GetDataFromEditText();

                if(CheckEditText){
                    productRecordUpdate (productIdHolder,productNameHolder,productPriceHolder,productMinimumHolder,
                            productHsncodeHolder, productBeandHolder,productDescriptionHolder, productStockHolder,
                            productRecordlevelHolder,productGstHolder);
                }else {

                    // If EditText is empty then this block will execute.
                    Toast.makeText(ProductUpdate.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    // Method to get existing data from EditText.
    public void GetDataFromEditText(){

        productNameHolder = productName.getText().toString();
        productPriceHolder = productPrice.getText().toString();
        productMinimumHolder = productMinimum.getText().toString();
        productHsncodeHolder = productHsncode.getText().toString();
        productBeandHolder = productBrand.getText().toString();
        productDescriptionHolder = productDescription.getText().toString();
        productStockHolder = productStock.getText().toString();
        productRecordlevelHolder = productRecordlevel.getText().toString();
        productGstHolder = productgst.getText().toString();

        if(TextUtils.isEmpty(productNameHolder) || TextUtils.isEmpty(productPriceHolder) || TextUtils.isEmpty(productMinimumHolder)
                || TextUtils.isEmpty(productHsncodeHolder) || TextUtils.isEmpty(productBeandHolder)
                || TextUtils.isEmpty(productDescriptionHolder) || TextUtils.isEmpty(productStockHolder)
                || TextUtils.isEmpty(productRecordlevelHolder)|| TextUtils.isEmpty(productGstHolder))
        {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }

    }

    // Method to Update Student Record.
    public void productRecordUpdate( String productIdHolder, String productNameHolder, String productPriceHolder, String productMinimumHolder,
                                    String productHsncodeHolder, String productBeandHolder,
                                    String productDescriptionHolder, String productStockHolder,
                                    String productRecordlevelHolder, String productGstHolder){

        class productRecordUpdateClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ProductUpdate.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(ProductUpdate.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("id",params[0]);

                hashMap.put("name",params[1]);

                hashMap.put("price",params[2]);

                hashMap.put("minimum",params[3]);

                hashMap.put("hsncode",params[4]);

                hashMap.put("brand",params[5]);

                hashMap.put("description",params[6]);

                hashMap.put("stock",params[7]);

                hashMap.put("reorderlevel",params[8]);

                hashMap.put("gst",params[9]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        productRecordUpdateClass productRecordUpdateClass = new productRecordUpdateClass();

        productRecordUpdateClass.execute(productIdHolder,productNameHolder, productPriceHolder, productMinimumHolder,
                productHsncodeHolder, productBeandHolder,
                productDescriptionHolder, productStockHolder, productRecordlevelHolder,
                productGstHolder);
    }

}
