package com.pcdgroup.hp.pcd_group.Product;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ProductUpdate
 * @description product update to is all details and image
 */

public class ProductUpdate extends AppCompatActivity implements CallBackInterface {

    //Components for the
    String HttpURL = "http://dert.co.in/gFiles/updateproductdetails.php";
    ProgressDialog progressDialog;
    String finalResult;
    Boolean CheckEditText;

    HttpParse httpParse = new HttpParse();
    ImageView imageView_Upload;

    //Edit Text and variables for all product details.
    EditText productName, productPrice,productMinimum,productHsncode,productBrand,productDescription,
            productStock, productRecordlevel, productgst;
    String productIdHolder,productImageHolder,productNameHolder,productPriceHolder,productMinimumHolder,productHsncodeHolder,
            productBeandHolder,productDescriptionHolder,productStockHolder, productRecordlevelHolder,
            productGstHolder;
    Intent intent;
    GlobalVariable gblVar;

    private Bitmap bitmap = null;

    private int PICK_IMAGE_REQUEST = 1;

    //Database Query Components.
    DataGetUrl urlQry;
    DataBaseQuery dataBaseQuery;
    CallType typeOfQuery;
    HashMap<String,String> hashMap = new HashMap<>();

    /** Update the product details.
     *  @param savedInstanceState object of passing parameters from the previous intent */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_product);

        gblVar = GlobalVariable.getInstance();

        productName = (EditText)findViewById(R.id.editText_name);
        productPrice = (EditText)findViewById(R.id.editText_price);
        productMinimum = (EditText)findViewById(R.id.editText_minumum);
        productHsncode = (EditText)findViewById(R.id.editText_hsn);
        productBrand = (EditText)findViewById(R.id.editText_brand);
        productDescription = (EditText)findViewById(R.id.editText_deskription);
        productStock = (EditText)findViewById(R.id.editText_stock);
        productRecordlevel = (EditText)findViewById(R.id.editText_record);
        productgst= (EditText)findViewById(R.id.editText_gst);


        imageView_Upload = (ImageView)findViewById(R.id.upload_imageview);

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        // Receive product ID, Name , Address , Email, etc.. Send by previous ShowSingleRecordActivity.
        productIdHolder = getIntent().getStringExtra("id");
        productImageHolder = getIntent().getStringExtra("image");
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
//        imageView_Upload.setImageBitmap(bitmap);
        productName.setText(productNameHolder);
        productPrice.setText(productPriceHolder);
        productMinimum.setText(productMinimumHolder);
        productHsncode.setText(productHsncodeHolder);
        productBrand.setText(productBeandHolder);
        productDescription.setText(productDescriptionHolder);
        productStock.setText(productStockHolder);
        productRecordlevel.setText(productRecordlevelHolder);
        productgst.setText(productGstHolder);

        productNameHolder = productNameHolder.replace("'","''");
        productDescriptionHolder = productDescriptionHolder.replace("'","''");

    }

    public void onClickProductUpdate(View view) {

        // Getting data from EditText after button click.
        GetDataFromEditText();

        if(CheckEditText){
            productRecordUpdate (productIdHolder,productImageHolder,productNameHolder,productPriceHolder,productMinimumHolder,
                    productHsncodeHolder, productBeandHolder,productDescriptionHolder, productStockHolder,
                    productRecordlevelHolder,productGstHolder);
        }else {

            // If EditText is empty then this block will execute.
            Toast.makeText(ProductUpdate.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
        }

    }

    public void onClickImageUpdateUpload(View v) {
        showFileChooser();
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == this.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                imageView_Upload.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView_Upload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Method to get existing data from EditText.
    public void GetDataFromEditText(){

        if(bitmap != null) {
            productImageHolder = getStringImage(bitmap);
            HttpURL = "http://dert.co.in/gFiles/updateproductdetails12.php";
            hashMap.put("image",productImageHolder);
        }

        productNameHolder = productName.getText().toString();
        productPriceHolder = productPrice.getText().toString();
        productMinimumHolder = productMinimum.getText().toString();
        productHsncodeHolder = productHsncode.getText().toString();
        productBeandHolder = productBrand.getText().toString();
        productDescriptionHolder = productDescription.getText().toString();
        productStockHolder = productStock.getText().toString();
        productRecordlevelHolder = productRecordlevel.getText().toString();
        productGstHolder = productgst.getText().toString();

        productNameHolder = productNameHolder.replace("'","''");
        productDescriptionHolder = productDescriptionHolder.replace("'","''");

        hashMap.put("id", productIdHolder);

        hashMap.put("name",productNameHolder);

        hashMap.put("price",productPriceHolder);

        hashMap.put("minimum",productMinimumHolder);

        hashMap.put("hsncode",productHsncodeHolder);

        hashMap.put("brand",productBeandHolder);

        hashMap.put("description",productDescriptionHolder);

        hashMap.put("stock",productStockHolder);

        hashMap.put("reorderlevel",productRecordlevelHolder);

        hashMap.put("gst",productGstHolder);

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

    /** Update product Record.
    * @param productIdHolder - all product details */
    public void productRecordUpdate( String productIdHolder,String ClientImageHolder,
                                     String productNameHolder, String productPriceHolder,
                                     String productMinimumHolder, String productHsncodeHolder,
                                     String productBeandHolder,
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

                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }

        productRecordUpdateClass productRecordUpdateClass = new productRecordUpdateClass();

        productRecordUpdateClass.execute(productIdHolder,ClientImageHolder,productNameHolder, productPriceHolder, productMinimumHolder,
                productHsncodeHolder, productBeandHolder,
                productDescriptionHolder, productStockHolder, productRecordlevelHolder,
                productGstHolder);
    }
    /** Home Button menu
     * @param  menu Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    /** Go to the home page based on the type of user.
     * @param  item MenuItem */
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

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /** Go to earlier activity on Back Pressed. */
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProductUpdate.this);
        builder.setMessage("Are You Sure Want To Exit?");
        builder.setCancelable(true);
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ProductUpdate.this, ViewImage.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    /** CallBack Function for processing the Database query result.
     * @param  response - Response string received while database query.
     *         dataGetUrl - Url queried.*/
    @Override
    public void ExecuteQueryResult(String response, DataGetUrl dataGetUrl) {

    }
    /** Releases the memory of all the components after intent finishes. */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        productName = null;
        productPrice = null;
        productMinimum = null;
        productHsncode = null;
        productBrand = null;
        productDescription = null;
        productStock = null;
        productRecordlevel = null;
        productgst = null;
        imageView_Upload = null;
    }
}
