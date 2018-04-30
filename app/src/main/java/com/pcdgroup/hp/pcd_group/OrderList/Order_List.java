package com.pcdgroup.hp.pcd_group.OrderList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.Client.UpdateActivity;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.Quotation.Pdf;
import com.pcdgroup.hp.pcd_group.Quotation.PdfAdapter;
import com.pcdgroup.hp.pcd_group.Quotation.ShowQuotationList;
import com.pcdgroup.hp.pcd_group.Quotation.ViewInvoice;
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

public class Order_List extends AppCompatActivity {

    ArrayList<Pdf> pdfList = new ArrayList<Pdf>();
    PdfAdapter pdfAdapter;
    public String httpUrl = "http://dert.co.in/gFiles/orderlist.php";
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    ProgressDialog progressDialog2;
    HttpParse httpParse;
    String finalResult;
    ListView lstVeiw;

    HashMap<String, String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);

        httpParse = new HttpParse();

        lstVeiw = (ListView) findViewById(R.id.orderList);
        GetPdfList();

        lstVeiw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String fileUrl = pdfList.get(position).getUrl();

            Intent intent = new Intent(Order_List.this, ViewInvoice.class);
            intent.putExtra("FileUrl", fileUrl);
            intent.putExtra("Activity", "OrderList");
            startActivity(intent);
            }
        });
    }

    // Method to Get the Invoice List
    public void GetPdfList() {

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
                    Toast.makeText(Order_List.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

                    JSONArray jsonArray = obj.getJSONArray("pdfs");

                    for(int i=0; i < jsonArray.length() ; i++){

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

                    pdfAdapter=new PdfAdapter(Order_List.this,R.layout.list_layout, pdfList);
                    lstVeiw.setAdapter(pdfAdapter);

                    pdfAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                // Sending Client id.
                hashMap.put("billed", "true");
                finalResult = httpParse.postRequest(hashMap, httpUrl);
                return finalResult;
            }
        }

        GetPdfList GetPdfList = new GetPdfList();

        GetPdfList.execute();
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
            Toast.makeText(this, "Main menu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Order_List.this, AdminDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
