package com.pcdgroup.hp.pcd_group.Quotation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ShowQuotationList
 * @description show quotation list of client to add him self
 */

public class ShowQuotationList extends AppCompatActivity implements CallBackInterface {

  ListView listView;
  ProgressDialog progressDialog;
  String emailId;
  ArrayList<Pdf> pdfList = new ArrayList<Pdf>();
  PdfAdapter pdfAdapter;
  public static int REQUEST_PERMISSIONS = 1;
  boolean boolean_permission;
  HttpParse httpParse;
  HashMap<String, String> hashMap = new HashMap<>();

  DataGetUrl urlQry;
  DataBaseQuery dataBaseQuery;
  CallType typeOfQuery;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.listpdfs_activity);

      /*
          - show quotation list
          - client add quotation to show this quotation
      */

    httpParse = new HttpParse();

    listView = (ListView) findViewById(R.id.listView);

    progressDialog = new ProgressDialog(this);

    // intent
    Intent intent = getIntent();
    emailId = intent.getStringExtra("emailid");

    urlQry = DataGetUrl.SHOW_CLIENT_QUOTATION_LIST;
    typeOfQuery = CallType.POST_CALL;

    hashMap.put("emailId", emailId);

    //Send Database query for inquiring to the database.
    dataBaseQuery = new DataBaseQuery(hashMap,
            urlQry,
            typeOfQuery,
            getApplicationContext(),
            ShowQuotationList.this
    );
    //Prepare for the database query
    dataBaseQuery.PrepareForQuery();

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
  /** CallBack Function for processing the Database query result.
   * @param  response - Response string received while database query.
   *         dataGetUrl - Url queried.*/
  @Override
  public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
    try {
      JSONObject obj = new JSONObject(response);
      Toast.makeText(ShowQuotationList.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

      JSONArray jsonArray = obj.getJSONArray("pdfs");

      for(int i=0;i<jsonArray.length();i++){

        //Declaring a json object corresponding to every pdf object in our json Array
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        //Declaring a Pdf object to add it to the ArrayList  pdfList
        Pdf pdf  = new Pdf();
        String pdfName = jsonObject.getString("name");
        //String pdfEmail = jsonObject.getString("email");
        pdf.setName(pdfName);
        pdf.setEmail(emailId);
        pdfList.add(pdf);
      }

      pdfAdapter=new PdfAdapter(ShowQuotationList.this,R.layout.list_layout, pdfList);

      listView.setAdapter(pdfAdapter);

      pdfAdapter.notifyDataSetChanged();

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  /** Releases the memory of all the components after intent finishes. */
  @Override
  protected void onDestroy() {
    super.onDestroy();
    listView = null;
  }
}
