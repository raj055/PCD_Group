package com.pcdgroup.hp.pcd_group.Product;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name UploadImage
 * @description upload image to server in compress images
 */

public class UploadImage extends AppCompatActivity {

    EditText name,price,minimum,hsncode,description,stock,reorderlevel;
    ImageView imageView;
    Spinner brand;
    Spinner gst;
    private File actualImage, compressedImage;

    private Bitmap bitmap;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private String KEY_Price = "price";
    private String KEY_Minimum = "minimum";
    private String KEY_HSNCode = "HSNCode";
    private String KEY_Brand = "Brand";
    private String KEY_Gst= "gst";
    private String KEY_Description = "Description";
    private String KEY_Stock = "Stock";
    private String KEY_Reorderlevel = "Reorderlevel";
    private String UPLOAD_URL ="http://dert.co.in/gFiles/upload.php";

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_layout);

        /*
            - compress image to upload to server
            - set name image and upload database
        */

        //Assign Id'S
        name= (EditText) findViewById(R.id.name);
        price= (EditText) findViewById(R.id.price);
        minimum= (EditText) findViewById(R.id.minimum);
        hsncode= (EditText) findViewById(R.id.HSNCode);
        brand= (Spinner) findViewById(R.id.spinner3);
        gst= (Spinner)findViewById(R.id.spinner4);
        description= (EditText) findViewById(R.id.Descriprion);
        stock= (EditText) findViewById(R.id.Stock);
        reorderlevel= (EditText) findViewById(R.id.Reorderlevel);
    }

    public void onClickUploadImage(View v) {

        if (name.getText().toString().length() <= 0)
        {
            name.setError("Please Enter Name !");
        }
        else if (price.getText().toString().length() <= 0)
        {
            price.setError("Please Enter Price !");
        }
        else if (minimum.getText().toString().length() <= 0)
        {
            minimum.setError("Please Enter Minimum Value !");
        }
        else if (hsncode.getText().toString().length() <= 0)
        {
            hsncode.setError("Please Enter HSN Code !");
        }
        else if (description.getText().toString().length() <= 0)
        {
            description.setError("Please Enter Description !");
        }
        else if (stock.getText().toString().length() <= 0)
        {
            stock.setError("Please Enter Stock !");
        }
        else if (reorderlevel.getText().toString().length() <= 0)
        {
            reorderlevel.setError("Please Enter Reorderlevel !");
        }
        else if (bitmap==null)
        {
            Toast.makeText(UploadImage.this,"Please Upload Image", Toast.LENGTH_SHORT).show();
        }
        else {
            uploadImage();

        }
    }

    public void onClickChoiceImage(View v) {
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
                imageView.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                actualImage = FileUtil.from(this, filePath);
                Toast.makeText(this, "Image Size =  "+   actualImage.length()
                  , Toast.LENGTH_LONG).show();
                compressImage();
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
    public void compressImage() {
        if (actualImage == null) {
            showError("Please choose an image!");
        } else {

            // Compress image in main thread
            // Compress image using RxJava in background thread
            new Compressor(this)
              .compressToFileAsFlowable(actualImage)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Consumer<File>() {
                  @Override
                  public void accept(File file) {
                      compressedImage = file;
                      setCompressedImage();
                  }
              }, new Consumer<Throwable>() {
                  @Override
                  public void accept(Throwable throwable) {
                      throwable.printStackTrace();
                      showError(throwable.getMessage());
                  }
              });
        }
    }
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
    private void setCompressedImage() {
        imageView.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
        bitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
        Toast.makeText(this, "Compressed image = " + compressedImage.length(), Toast.LENGTH_LONG).show();
        Log.d("Compressor", "Compressed image save in " + compressedImage.getPath());
    }

    private void uploadImage(){

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(loading != null) {
                            if (loading.isShowing()) {
                                loading.dismiss();
                            }
                        }

                        //Showing toast message of the response
                        Toast.makeText(UploadImage.this,s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        if(loading != null) {
                            if (loading.isShowing()) {
                                loading.dismiss();
                            }
                        }

                        //Showing toast
                        Toast.makeText(UploadImage.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name1 = name.getText().toString().trim();
                String name2 = price.getText().toString().trim();
                String name3 = minimum.getText().toString().trim();
                String name4 = hsncode.getText().toString().trim();
                String name5= brand.getSelectedItem().toString().trim();
                String name6= description.getText().toString().trim();
                String name7= stock.getText().toString().trim();
                String name8= reorderlevel.getText().toString().trim();
                String name9=gst.getSelectedItem().toString().trim();
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name1);
                params.put(KEY_Price, name2);
                params.put(KEY_Minimum, name3);
                params.put(KEY_HSNCode,name4);
                params.put(KEY_Brand,name5);
                params.put(KEY_Description,name6);
                params.put(KEY_Stock,name7);
                params.put(KEY_Reorderlevel,name8);
                params.put(KEY_Gst,name9);
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        name = null;
        price = null;
        minimum = null;
        hsncode = null;
        description = null;
        stock = null;
        reorderlevel = null;
        imageView = null;
        brand = null;
        gst = null;
    }
}
