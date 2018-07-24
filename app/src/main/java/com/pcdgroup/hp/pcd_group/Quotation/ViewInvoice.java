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

import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ViewInvoice
 * @description view invoice to create client
 */

public class ViewInvoice extends AppCompatActivity implements CallBackInterface {

  TextView name,address,state,company,country,pin,state1;
  TextView item,hsn,gst,cgst,price,quantity,amount, sgst, cgst1,
                    TransportationCost,DiscountValue,DiscountTextview;
  TextView finalprice, finalquantity, finalamount, nameBill, brandname;
  TextView date,validdate, finalPayable;
  HashMap<String,String> hashMap = new HashMap<>();
  EditText userAnswer;

  public static int REQUEST_PERMISSIONS = 1;
  ConstraintLayout cl_pdflayout;
  boolean boolean_permission;
  boolean boolean_save;
  Bitmap bitmap;
  ProgressDialog progressDialog;
  boolean igst = false;

  String state_holder,state1_holder;
  HashMap<String, String> map = new HashMap<String, String>();
  String fileName, targetPdf;
  String fileUrl ;

  GlobalVariable gblVar;

  DataGetUrl urlQry;
  DataBaseQuery dataBaseQuery;
  CallType typeOfQuery;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.invoice);

      /*
          - view invoice
      */

    gblVar = GlobalVariable.getInstance();

    date = (TextView) findViewById(R.id.date_tv);
    validdate = (TextView) findViewById(R.id.validdate_tv);
    quantity = (TextView) findViewById(R.id.tvquantity);

    name = (TextView) findViewById(R.id.client_name);
    brandname = (TextView) findViewById(R.id.textView13);
    address = (TextView) findViewById(R.id.textView19);
    company = (TextView) findViewById(R.id.textView22);
    country = (TextView) findViewById(R.id.textView21);
    TransportationCost =(TextView) findViewById(R.id.textView18);
    DiscountValue =(TextView ) findViewById(R.id.textView27);
    DiscountTextview =(TextView ) findViewById(R.id.textView20);


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
    nameBill = (TextView) findViewById(R.id.name);

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
        String activity = extras.getString("Activity");
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

        if(map.containsKey("bname"))brandname.setText(map.get("bname"));
        if(map.containsKey("date"))date.setText(map.get("date"));
        if(map.containsKey("validdate"))validdate.setText(map.get("validdate"));
        if(map.containsKey("pin"))pin.setText(map.get("pin"));
        if(map.containsKey("state"))state.setText(map.get("state"));
        if(map.containsKey("state1"))state1.setText(map.get("state1"));

        if(map.containsKey("address"))address.setText(map.get("address"));
        if(map.containsKey("company"))company.setText(map.get("company"));
        if(map.containsKey("country"))country.setText(map.get("country"));

        if(map.containsKey("finalprice"))finalprice.setText(map.get("finalprice"));

        if(map.containsKey("finalquantity"))finalquantity.setText(map.get("finalquantity"));
        if(map.containsKey("finalamount"))finalamount.setText(map.get("finalamount"));
        if(map.containsKey("finalpayable"))finalPayable.setText(map.get("finalpayable"));
        if(map.containsKey("name"))name.setText(map.get("name"));
        if(map.containsKey("transportfee"))TransportationCost.setText(map.get("transportfee"));
        if(map.containsKey("discountValue"))DiscountValue.setText(map.get("discountValue"));
        if(map.containsKey("discountText"))DiscountTextview.setText(map.get("discountText"));

        state_holder = state.getText().toString();
        state1_holder = state1.getText().toString();
        if(activity!= null){
          if(activity.contains("OrderList"))
          nameBill.setText("BILL");
        }

        if (state1_holder.contains(state_holder)){

        }else {
          TableRow tblr = (TableRow) findViewById(R.id.tableRow);
          tblr.removeView(sgst);
          cgst1.setText("IGST");
          igst = true;
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
        nameBill.setText("BILL");
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

            urlQry = DataGetUrl.UPDATE_BILL;
            typeOfQuery = CallType.POST_CALL;

            String billedStatus = "True";

            // Sending Client id.
            hashMap.put("billed", billedStatus);
            hashMap.put("url", fileUrl);

            //Send Database query for inquiring to the database.
            dataBaseQuery = new DataBaseQuery(hashMap,
                    urlQry,
                    typeOfQuery,
                    getApplicationContext(),
                    ViewInvoice.this
            );
            //Prepare for the database query
            dataBaseQuery.PrepareForQuery();

          }
        });

        // all set and time to build and show up!
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        break;
    }
    return super.onOptionsItemSelected(item);
  }

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
  /** CallBack Function for processing the Database query result.
   * @param  response - Response string received while database query.
   *         dataGetUrl - Url queried.*/
  @Override
  public void ExecuteQueryResult(String response,DataGetUrl dataGetUrl) {
    Toast.makeText(ViewInvoice.this, response.toString(), Toast.LENGTH_LONG).show();
  }
  /** Releases the memory of all the components after intent finishes. */
  @Override
  protected void onDestroy() {
    super.onDestroy();
    name = null;
    address = null;
    state = null;
    company = null;
    country = null;
    pin = null;
    state1 = null;
    item = null;
    hsn = null;
    gst = null;
    cgst = null;
    price = null;
    quantity = null;
    amount = null;
    sgst = null;
    cgst1 = null;
    TransportationCost = null;
    DiscountValue = null;
    DiscountTextview = null;
    finalprice = null;
    finalquantity = null;
    finalamount = null;
    nameBill = null;
    brandname = null;
    date = null;
    validdate = null;
    finalPayable = null;
    userAnswer = null;
    progressDialog = null;
  }
}
