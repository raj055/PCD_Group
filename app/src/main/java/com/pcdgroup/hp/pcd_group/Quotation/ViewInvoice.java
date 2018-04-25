package com.pcdgroup.hp.pcd_group.Quotation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.Client.SingleRecordShow;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ViewInvoice  extends AppCompatActivity {

  TextView name,address,state,company,country,add1,add2,pin,state1;
  HttpParse httpParse = new HttpParse();

  TextView item,hsn,gst,cgst,price,quantity,amount, sgst, cgst1;
  TextView finalprice, finalquantity, finalamount;

  TextView date,validdate, finalPayable;
  HashMap<String,String> hashMap = new HashMap<>();

  EditText userAnswer;

  float totalPrice, totalAmount;
  int totalquantity;

  public static int REQUEST_PERMISSIONS = 1;
  ConstraintLayout cl_pdflayout;
  boolean boolean_permission;
  boolean boolean_save;
  Bitmap bitmap;
  ProgressDialog progressDialog;

  ProgressDialog progressDialog2;
  String finalResult ;
  boolean igst = false;

  String state_holder,state1_holder;

  HashMap<String, String> map = new HashMap<String, String>();

  // Http Url For adding the bills to the admin.
  String HttpURL = "http://dert.co.in/gFiles/updatebill.php";

  public static final String UPLOAD_URL = "http://dert.co.in/gFiles/server_upload_bills.php";
  String fileName, targetPdf;
  String fileUrl ;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.invoice);

    date = (TextView) findViewById(R.id.date_tv);
    validdate = (TextView) findViewById(R.id.validdate_tv);
    quantity = (TextView) findViewById(R.id.tvquantity);

    name = (TextView) findViewById(R.id.client_name);
    address = (TextView) findViewById(R.id.textView19);
    company = (TextView) findViewById(R.id.textView22);
    country = (TextView) findViewById(R.id.textView21);

    state = (TextView)findViewById(R.id.text_state);
    pin = (TextView)findViewById(R.id.text_pin);
    sgst = (TextView)findViewById(R.id.sgst);
    cgst1 = (TextView)findViewById(R.id.cgst);

    state1 = (TextView)findViewById(R.id.text_state1);
    item = (TextView) findViewById(R.id.tvproduct);
    hsn = (TextView) findViewById(R.id.tvhsn);
    gst = (TextView) findViewById(R.id.tvgst);
    price = (TextView) findViewById(R.id.tvprice);

    finalprice = (TextView) findViewById(R.id.itemsPrice);
    finalquantity = (TextView) findViewById(R.id.finalQuantity);
    finalamount = (TextView) findViewById(R.id.finalAmount);
    finalPayable = (TextView) findViewById(R.id.textView25);

    LinearLayout lyt = (LinearLayout) findViewById(R.id.tableRow2);
    cl_pdflayout = (ConstraintLayout) findViewById(R.id.cl_pdf);

    //Allow network in main thread
    StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

    String str;
    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if (extras != null) {
        // Client
        fileUrl = extras.getString("FileUrl");
        URL url;
        Properties prFile = new Properties();
        try {
          url = new URL(fileUrl);
          BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
          prFile.load(in);

          for (final String name : prFile.stringPropertyNames())
            map.put(name, prFile.getProperty(name));
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }

        if(map.containsKey("name"))name.setText(map.get("name"));
        if(map.containsKey("date"))date.setText(map.get("date"));
        if(map.containsKey("validdate"))validdate.setText(map.get("validdate"));
        if(map.containsKey("pin"))pin.setText(map.get("pin"));
        if(map.containsKey("state"))state.setText(map.get("state"));
        if(map.containsKey("state1"))state1.setText(map.get("state1"));

        if(map.containsKey("address"))address.setText(map.get("address"));
        if(map.containsKey("company"))company.setText(map.get("company"));
        if(map.containsKey("country"))country.setText(map.get("country"));

        if(map.containsKey("finalprice"))finalprice.setText(map.get("finalprice"));

        finalquantity.setText(map.get("finalquantity"));
        finalamount.setText(map.get("finalamount"));
        finalPayable.setText(map.get("finalamount"));
        state_holder = state.getText().toString();
        state1_holder = state1.getText().toString();

        if (state1_holder.contains(state_holder)){

//                    sgst.setText("SGST");
//                    cgst1.setText("CGST");

        }else {
          TableRow tblr = (TableRow) findViewById(R.id.tableRow);
          tblr.removeView(sgst);
//                    sgst.setVisibility(View.INVISIBLE);
          cgst1.setText("IGST");
          igst = true;
          //cgst.setVisibility(View.INVISIBLE);
//          gstValue /= 1;
        }

        String temp;
        String[] productList = new String[0];
        String[] gstList= new String[0];
        String[] cgstList= new String[0];
        String[] pricesList= new String[0];
        String[] quantityList= new String[0];
        String[] amountList= new String[0];
        String[] hsnList = new String[0];
        if((map.containsKey("products")) &&
          (map.containsKey("gst"))  &&
          (map.containsKey("cgst")) &&
          (map.containsKey("prices")) &&
          (map.containsKey("quantities"))&&
          (map.containsKey("amount"))&&
          (map.containsKey("hsncode"))){
           temp =  map.get("products");
           productList = temp.split(",");
           gstList = map.get("gst").split(",");
           cgstList = map.get("cgst").split(",");
           pricesList = map.get("prices").split(",");
           quantityList = map.get("quantities").split(",");
           amountList = map.get("amount").split(",");
          hsnList = map.get("hsncode").split(",");

        }

        Log.v("Product Length","" +productList.length);

          for (int i = 0; i < productList.length; i++) {

            View child = (View) getLayoutInflater().inflate(R.layout.product_list, null);
            lyt.addView(child);

            item = (TextView) child.findViewById(R.id.tvproduct);
            hsn = (TextView) child.findViewById(R.id.tvhsn);
            gst = (TextView) child.findViewById(R.id.tvgst);
            price = (TextView) child.findViewById(R.id.tvprice);
            quantity = (TextView) child.findViewById(R.id.tvquantity);
            cgst = (TextView) child.findViewById(R.id.sgst);
            amount = (TextView) child.findViewById(R.id.amount);
            LinearLayout tblRw = (LinearLayout) child.findViewById(R.id.tableRow2);

            if(igst == true){
              tblRw.removeView(cgst);
            }
            else

            cgst.setText(cgstList[i]);
            //product information
            item.setText(productList[i]);
            gst.setText(gstList[i]);

            price.setText(pricesList[i]);
            quantity.setText(quantityList[i]);
            amount.setText(amountList[i]);
            hsn.setText(hsnList[i]);
        }
      }
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
//    menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.edit), getResources().getString(R.string.action_edit)));
    menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.pdf), getResources().getString(R.string.bill_convert)));
    return true;
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {

      case 1:

        LayoutInflater layoutinflater = LayoutInflater.from(this);
        View promptUserView = layoutinflater.inflate(R.layout.name_dialog_box, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptUserView);

        userAnswer = (EditText) promptUserView.findViewById(R.id.username);

        alertDialogBuilder.setTitle("Save File Name.");

        // prompt for username
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {

            fileName =  userAnswer.getText().toString();
            targetPdf = "/sdcard/" + fileName + ".pdf";
            fn_permission();

            if (boolean_permission) {
              progressDialog = new ProgressDialog(ViewInvoice.this);
              progressDialog.setMessage("Please wait");
              bitmap = loadBitmapFromView(cl_pdflayout, cl_pdflayout.getWidth(), cl_pdflayout.getHeight());
              createPdf();
            }
            if (boolean_save) {
              Intent intent1 = new Intent(getApplicationContext(), PDFViewActivity.class);
              intent1.putExtra("FileName",targetPdf);
              startActivity(intent1);
            }

            AddToOrderList(fileUrl);
          }
        });

        // all set and time to build and show up!
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        break;
    }
    return super.onOptionsItemSelected(item);
  }
  private void AddToOrderList(final String textFile) {

      class AddToOrderList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
          super.onPreExecute();

          progressDialog2 = ProgressDialog.show(ViewInvoice.this, "Loading Data",
            null, true, true);
        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {

          super.onPostExecute(httpResponseMsg);

          progressDialog2.dismiss();

          Toast.makeText(ViewInvoice.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... params) {

          String billedStatus = "True";

          // Sending Client id.
          hashMap.put("billed", billedStatus);
          hashMap.put("url", textFile);


          finalResult = httpParse.postRequest(hashMap, HttpURL);

          return finalResult;
        }
      }

    AddToOrderList AddToOrderList = new AddToOrderList();

    AddToOrderList.execute(textFile);
    }





    //getting name for the image
//    String name = userAnswer.getText().toString().trim();

//    Log.v("FileName::::::::::::", targetPdf);
//    if (targetPdf == null) {
//
//      Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
//    } else {
//      //Uploading code
//      try {
//        String uploadId = UUID.randomUUID().toString();
//
//        GlobalVariable gblVar = GlobalVariable.getInstance();
//
//        String emailId = "";
//        if(gblVar.currentUserEmail != null)
//        {
//          emailId = gblVar.currentUserEmail;
//
//        }

        //Creating a multi part request
//        new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
//          .addFileToUpload(targetPdf, "txt") //Adding file
//          .addParameter("name", name) //Adding text parameter to the request
//          .addParameter("email", emailId)
//          .setNotificationConfig(new UploadNotificationConfig())
//          .setMaxRetries(2)
//          .startUpload(); //Starting the upload

//      } catch (Exception exc) {
//        Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//      }
//    }
//  }


  public static Bitmap loadBitmapFromView(View v, int width, int height) {
    Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(b);
    v.draw(c);

    return b;
  }

  private void fn_permission() {
    if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
      (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

      if ((ActivityCompat.shouldShowRequestPermissionRationale(ViewInvoice.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
      } else {
        ActivityCompat.requestPermissions(ViewInvoice.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
          REQUEST_PERMISSIONS);
      }

      if ((ActivityCompat.shouldShowRequestPermissionRationale(ViewInvoice.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
      } else {
        ActivityCompat.requestPermissions(ViewInvoice.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
          REQUEST_PERMISSIONS);
      }
    } else {
      boolean_permission = true;

    }
  }

  private void createPdf(){
    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    DisplayMetrics displaymetrics = new DisplayMetrics();
    this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    float hight = displaymetrics.heightPixels ;
    float width = displaymetrics.widthPixels ;

    int convertHighet = (int) hight, convertWidth = (int) width;

    PdfDocument document = new PdfDocument();
    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
    PdfDocument.Page page = document.startPage(pageInfo);

    Canvas canvas = page.getCanvas();

    Paint paint = new Paint();
    canvas.drawPaint(paint);

    bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

    paint.setColor(Color.BLUE);
    canvas.drawBitmap(bitmap, 0, 0 , null);
    document.finishPage(page);

    targetPdf = "/sdcard/" + fileName + ".pdf";

    File filePath = new File(targetPdf);
    try {
      document.writeTo(new FileOutputStream(filePath));

      boolean_save=true;
    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
    }
    // close the document
    document.close();
  }
  private CharSequence menuIconWithText(Drawable r, String title) {

    r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
    SpannableString sb = new SpannableString(" " + title);
    ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
    sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    return sb;
  }

}
