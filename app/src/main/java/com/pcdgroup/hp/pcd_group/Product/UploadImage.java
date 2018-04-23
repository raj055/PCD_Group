package com.pcdgroup.hp.pcd_group.Product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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
import com.pcdgroup.hp.pcd_group.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class UploadImage extends AppCompatActivity {

    EditText name,price,minimum,hsncode,description,stock,reorderlevel;
    ImageView imageView;
    Spinner brand;
    Spinner gst;
    Button pickImage, upload;

    private Bitmap bitmap;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private String KEY_Price = "price";
    private String KEY_Minimum = "minimum";
    private String KEY_HSNCode = "HSNCode";
    private String KEY_Brand = "Brand";
    private  String KEY_Gst= "gst";
    private String KEY_Description = "Description";
    private String KEY_Stock = "Stock";
    private String KEY_Reorderlevel = "Reorderlevel";
    private String UPLOAD_URL ="http://pcddata-001-site1.1tempurl.com/upload.php";

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_layout);

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
        pickImage= (Button) findViewById(R.id.pickImgaeButton);
        upload = (Button) findViewById(R.id.upload);

        imageView = (ImageView) findViewById(R.id.previewImage);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });
        pickImage.setOnClickListener(new View.OnClickListener() {
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
                imageView.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
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

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(UploadImage.this,s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(UploadImage.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
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

}
