package com.pcdgroup.hp.pcd_group.Quotation;

import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pcdgroup.hp.pcd_group.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class List_Quotation_Pdfs extends AppCompatActivity {

    public static final String PDF_FETCH_URL = "http://dert.co.in/gFiles/QuotationList.php";

    ListView listView;
    ProgressDialog progressDialog;

    ArrayList<Pdf> pdfList= new ArrayList<Pdf>();

    PdfAdapter pdfAdapter;

    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listpdfs_activity);


        listView = (ListView) findViewById(R.id.listView);


        progressDialog = new ProgressDialog(this);

        getPdfs();

        //setting listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                fn_permission();

                Pdf pdf = (Pdf) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(pdf.getUrl()));
                startActivity(intent);

            }
        });
    }

    private void getPdfs() {

        progressDialog.setMessage("Fetching Pdfs... Please Wait");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PDF_FETCH_URL,

            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        Toast.makeText(List_Quotation_Pdfs.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = obj.getJSONArray("pdfs");

                        for(int i=0;i<jsonArray.length();i++){

                            //Declaring a json object corresponding to every pdf object in our json Array
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            //Declaring a Pdf object to add it to the ArrayList  pdfList
                            Pdf pdf  = new Pdf();
                            String pdfName = jsonObject.getString("name");
                            String pdfUrl = jsonObject.getString("url");
                            pdf.setName(pdfName);
                            pdf.setUrl(pdfUrl);
                            pdfList.add(pdf);

                        }

                        pdfAdapter=new PdfAdapter(List_Quotation_Pdfs.this,R.layout.list_layout, pdfList);

                        listView.setAdapter(pdfAdapter);

                        pdfAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);

    }


    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(List_Quotation_Pdfs.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(List_Quotation_Pdfs.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(List_Quotation_Pdfs.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(List_Quotation_Pdfs.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;

            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

}
