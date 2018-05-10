package com.pcdgroup.hp.pcd_group.Product;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.UpdateActivity;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ProductUpdate extends AppCompatActivity {

    String HttpURL = "http://dert.co.in/gFiles/updateproductdetails12.php";
//    String HttpURLImage = "http://dert.co.in/gFiles/updateproductimage.php";
    ProgressDialog progressDialog;
    String finalResult;
    Boolean CheckEditText;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    ImageView imageView_Upload;
    EditText productName, productPrice,productMinimum,productHsncode,productBrand,productDescription,
            productStock, productRecordlevel, productgst;
    Button UpdateProduct,Image_Upload;
    String productIdHolder,productImageHolder,productNameHolder,productPriceHolder,productMinimumHolder,productHsncodeHolder,
            productBeandHolder,productDescriptionHolder,productStockHolder, productRecordlevelHolder,
            productGstHolder;
    Intent intent;
    GlobalVariable gblVar;

    private Bitmap bitmap;


    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_product);

        gblVar = GlobalVariable.getInstance();

        imageView_Upload = (ImageView) findViewById(R.id.upload_imageview);

        productName = (EditText)findViewById(R.id.editText_name);
        productPrice = (EditText)findViewById(R.id.editText_price);
        productMinimum = (EditText)findViewById(R.id.editText_minumum);
        productHsncode = (EditText)findViewById(R.id.editText_hsn);
        productBrand = (EditText)findViewById(R.id.editText_brand);
        productDescription = (EditText)findViewById(R.id.editText_deskription);
        productStock = (EditText)findViewById(R.id.editText_stock);
        productRecordlevel = (EditText)findViewById(R.id.editText_record);
        productgst= (EditText)findViewById(R.id.editText_gst);

        Image_Upload = (Button)findViewById(R.id.btn_imgupload);
        UpdateProduct = (Button)findViewById(R.id.btn_submit);

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
//        imageView_Upload.setImageBitmap();
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
                    productRecordUpdate (productIdHolder,productImageHolder,productNameHolder,productPriceHolder,productMinimumHolder,
                            productHsncodeHolder, productBeandHolder,productDescriptionHolder, productStockHolder,
                            productRecordlevelHolder,productGstHolder);
                }else {

                    // If EditText is empty then this block will execute.
                    Toast.makeText(ProductUpdate.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }

            }
        });

        Image_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
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

        productImageHolder = getStringImage(bitmap);

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
    public void productRecordUpdate( String productIdHolder,String ClientImageHolder, String productNameHolder, String productPriceHolder, String productMinimumHolder,
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

                hashMap.put("image",params[1]);

                hashMap.put("name",params[2]);

                hashMap.put("price",params[3]);

                hashMap.put("minimum",params[4]);

                hashMap.put("hsncode",params[5]);

                hashMap.put("brand",params[6]);

                hashMap.put("description",params[7]);

                hashMap.put("stock",params[8]);

                hashMap.put("reorderlevel",params[9]);

                hashMap.put("gst",params[10]);

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

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProductUpdate.this);
        builder.setMessage("Are You Sure Want To Exit Register ?");
        builder.setCancelable(true);
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ProductUpdate.this, ViewImage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
}
