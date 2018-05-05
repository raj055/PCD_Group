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

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ShowQuotationList extends AppCompatActivity {

  public String httpUrl = "http://dert.co.in/gFiles/getpdfs.php";
  ListView listView;
  Button buttonFetch;
  ProgressDialog progressDialog;
  String emailId;
  String finalResult;

  ArrayList<Pdf> pdfList = new ArrayList<Pdf>();

  PdfAdapter pdfAdapter;

  public static int REQUEST_PERMISSIONS = 1;
  boolean boolean_permission;
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

//    Log.v("CurrentEmail:::", emailId);

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

      }

      @Override
      protected void onPostExecute(String httpResponseMsg) {

        super.onPostExecute(httpResponseMsg);

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
            pdf.setName(pdfName);
            pdfList.add(pdf);
          }

          pdfAdapter=new PdfAdapter(ShowQuotationList.this,R.layout.list_layout, pdfList);

          listView.setAdapter(pdfAdapter);

          pdfAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
          e.printStackTrace();
        }

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
