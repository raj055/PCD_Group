package com.pcdgroup.hp.pcd_group.Quotation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowQuotationList extends AppCompatActivity {
  public String PDF_FETCH_URL = "http://pcddata-001-site1.1tempurl.com/getpdfs.php";
  public String httpUrl = "http://pcddata-001-site1.1tempurl.com/getpdfs.php";
  ListView listView;
  Button buttonFetch;
  ProgressDialog progressDialog;
  String emailId;
  String finalResult;

  ArrayList<Pdf> pdfList = new ArrayList<Pdf>();

  PdfAdapter pdfAdapter;

  public static int REQUEST_PERMISSIONS = 1;
  boolean boolean_permission;
  ProgressDialog progressDialog2;
  HttpParse httpParse;

  HashMap<String, String> hashMap = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.listpdfs_activity);
    httpParse = new HttpParse();

    listView = (ListView) findViewById(R.id.listView);
    buttonFetch = (Button) findViewById(R.id.buttonFetchPdf);

    progressDialog = new ProgressDialog(this);

    // intent
    Intent intent = getIntent();
    emailId = intent.getStringExtra("emailid");

    Log.v("CurrentEmail:::", emailId);

    //Setting clicklistener
    buttonFetch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        GetPdfList(emailId);
      }
    });

    //setting listView on item click listener
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        fn_permission();
          String fileUrl = pdfList.get(position).getUrl();


        Intent intent = new Intent(ShowQuotationList.this, ViewInvoice.class);
        intent.putExtra("FileUrl", fileUrl);
        startActivity(intent);
      }
    });
  }

  // Method to Get the Invoice List
  public void GetPdfList(final String ClientID) {

    class GetPdfList extends AsyncTask<String, Void, String> {

      @Override
      protected void onPreExecute() {
        super.onPreExecute();

//        progressDialog = ProgressDialog.show(ShowQuotationList.this, "Loading Data", null, true, true);
      }

      @Override
      protected void onPostExecute(String httpResponseMsg) {

        super.onPostExecute(httpResponseMsg);

//        progressDialog.dismiss();

        try {
          JSONObject obj = new JSONObject(httpResponseMsg);
          Toast.makeText(ShowQuotationList.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

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

          pdfAdapter=new PdfAdapter(ShowQuotationList.this,R.layout.list_layout, pdfList);

          listView.setAdapter(pdfAdapter);

          pdfAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
          e.printStackTrace();
        }


//        Toast.makeText(ShowQuotationList.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

      }

      @Override
      protected String doInBackground(String... params) {

        // Sending Client id.
        hashMap.put("emailId", emailId);

        finalResult = httpParse.postRequest(hashMap, httpUrl);

        return finalResult;
      }
    }

    GetPdfList GetPdfList = new GetPdfList();

    GetPdfList.execute(ClientID);
  }

  private void getInvoice() {

    progressDialog.setMessage("Fetching Invoices... Please Wait");
    progressDialog.show();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, PDF_FETCH_URL,

      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

          progressDialog.dismiss();
          try {
            JSONObject obj = new JSONObject(response);
            Toast.makeText(ShowQuotationList.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

            JSONArray jsonArray = obj.getJSONArray("pdfs");

            for (int i = 0; i < jsonArray.length(); i++) {

              //Declaring a json object corresponding to every pdf object in our json Array
              JSONObject jsonObject = jsonArray.getJSONObject(i);
              //Declaring a Pdf object to add it to the ArrayList  pdfList
              Pdf pdf = new Pdf();
              String pdfName = jsonObject.getString("name");
              String pdfUrl = jsonObject.getString("url");
              pdf.setName(pdfName);
              pdf.setUrl(pdfUrl);
              pdfList.add(pdf);

            }

            pdfAdapter = new PdfAdapter(ShowQuotationList.this, R.layout.list_layout, pdfList);

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
//    if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
//      (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
//
//      if ((ActivityCompat.shouldShowRequestPermissionRationale(List_Pdfs.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
//      } else {
//        ActivityCompat.requestPermissions(List_Pdfs.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//          REQUEST_PERMISSIONS);
//      }
//
//      if ((ActivityCompat.shouldShowRequestPermissionRationale(List_Pdfs.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
//      } else {
//        ActivityCompat.requestPermissions(List_Pdfs.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//          REQUEST_PERMISSIONS);
//      }
//    } else {
//      boolean_permission = true;
//
//    }
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
