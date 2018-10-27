package com.pcdgroup.hp.pcd_group.Quotation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
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

import com.itextpdf.text.PageSize;
import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallBackInterface;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.CallType;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataBaseQuery;
import com.pcdgroup.hp.pcd_group.DatabaseComponents.DataGetUrl;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.xml.validation.Validator;

import static android.app.PendingIntent.getActivity;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name Invoice
 * @description create invoice  to add client and company details
 */

public class Invoice extends AppCompatActivity {

    TextView name,address,state,company,country,pin;
    TextView state1,sgst,cgst1;
    TextView item,hsn,gst,cgst,price,quantity,amount;
    TextView finalprice, finalquantity, finalamount;
    TextView b_name,b_address,b_pin,b_state,b_mobile,b_pan;
    TextView date,validdate, finalPayable,TransportationCost,DiscountValue,DiscountTextview;

    float amt;
    String transport;
    String discount = String.valueOf(0);
    boolean igst = false;
    EditText userAnswer;
    float gstValue;
    String state_holder,state1_holder;
    float totalPrice, totalAmount;
    int totalquantity;
    ConstraintLayout cl_pdflayout;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    HashMap<String, String> hsmap = new HashMap<String, String>();
    public static final String UPLOAD_URL =
                                    "http://dert.co.in/gFiles/uploadtxtfile.php";
    String fileName, targetPdf;
    LinearLayout lyt;
    String getAllProducts, getGst, getCgst, getPrice, getQuantity, getAmount, getHsn;

    private int REQUEST_CODE_OPEN_DIRECTORY = 0;
    public static int REQUEST_PERMISSIONS = 1;
    String mCurrentDirectoryUri;
    String DiscountTVvalue;
    GlobalVariable globalVariable;
    SelectedObject selectedObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice);

        /*
            - show pdf of invoice
            - send to server and create invoice to quotation
        */

        initialiseLayouts();

        transport = getIntent().getExtras().getString("transportioncost");
        TransportationCost.setText(transport);

        discount = getIntent().getExtras().getString("discountperce");
        DiscountTVvalue = globalVariable.DiscountType;

        DiscountTextview.setText("Discount:" + "\t" + DiscountTVvalue + "%");

        selectedObject = (SelectedObject) getIntent().getParcelableExtra("Addresses");

        Bundle extras = getIntent().getExtras();
        //Check data
       /* String brandName = selectedObject.brandAddress.get(0);

        String ClientName = selectedObject.address.get(7);

        Log.v("selected brand ==", String.valueOf(brandName));
        Log.v("selected client ==", String.valueOf(ClientName));*/
        //--

        // Brand Details

        String str;

        b_name.setText(selectedObject.brandAddress.get(0));
        str = selectedObject.brandAddress.get(1) + "," + "\n" + selectedObject.brandAddress.get(2) + "," + "\n" + selectedObject.brandAddress.get(3);
        b_address.setText(str);

        b_state.setText(selectedObject.brandAddress.get(5));

        b_pin.setText(selectedObject.brandAddress.get(4));

        str = selectedObject.brandAddress.get(6) + "," + "\n" + selectedObject.brandAddress.get(7) + "," + "\n" + selectedObject.brandAddress.get(9);
        b_mobile.setText(str);

        str = selectedObject.brandAddress.get(9) + "," + "\n" + selectedObject.brandAddress.get(10);
        b_pan.setText(str);

        // Client Details

        name.setText(selectedObject.address.get(7));
        str = selectedObject.address.get(0) + "," + "\n" + selectedObject.address.get(1) + "," + "\n" + selectedObject.address.get(2);

        address.setText(str);
        state.setText(selectedObject.address.get(4));
        pin.setText(selectedObject.address.get(3));
        company.setText(selectedObject.address.get(6));
        country.setText(selectedObject.address.get(5));

        //Product Details

        ArrayList<String[]> PrdList = (ArrayList<String[]>) extras.getSerializable("ProductInfo");
        int size = PrdList.size();

        getAllProducts = "";
        Log.v("Products---------", getAllProducts);
        getGst = new String();
        getPrice = new String();
        getCgst = new String();
        getQuantity = new String();
        getAmount = new String();
        getHsn = new String();
        state_holder = state.getText().toString();
        state1_holder = state1.getText().toString();

        if (state1_holder.contains(state_holder)) {

        } else {
            TableRow tblr = (TableRow) findViewById(R.id.tableRow);
            tblr.removeView(sgst);

            cgst1.setText("IGST");
            igst = true;

            gstValue /= 1;
        }

        for (int i = 0; i < size; i++) {

            String[] stringList = PrdList.get(i);

            View child = (View) getLayoutInflater().inflate(R.layout.product_list, null);
            lyt.addView(child);
            LinearLayout tblRw = (LinearLayout) child.findViewById(R.id.tableRow2);

            item = (TextView) child.findViewById(R.id.tvproduct);
            hsn = (TextView) child.findViewById(R.id.tvhsn);
            gst = (TextView) child.findViewById(R.id.tvgst);
            price = (TextView) child.findViewById(R.id.tvprice);
            quantity = (TextView) child.findViewById(R.id.tvquantity);
            cgst = (TextView) child.findViewById(R.id.sgst);
            amount = (TextView) child.findViewById(R.id.amount);

            //product information
            item.setText(stringList[0]);
            getAllProducts = getAllProducts.concat(stringList[0]);
            getAllProducts = getAllProducts.concat(",");

            hsn.setText(stringList[1]);
            getHsn = getHsn.concat(stringList[1]);
            getHsn = getHsn.concat(",");

            float gstValue = Integer.valueOf(stringList[2]);
            float priceStr = Float.valueOf(stringList[3]);
            getPrice = getPrice.concat(stringList[3]);
            getPrice = getPrice.concat(",");
            Integer quantityStr = Integer.valueOf(stringList[4]);


            amt = gstValue * priceStr / 100 + priceStr;
            if (igst != true) gstValue /= 2;
            gst.setText(String.valueOf(gstValue));
            getGst = getGst.concat(String.valueOf(gstValue));
            getGst = getGst.concat(",");
            getCgst = getCgst.concat(String.valueOf(gstValue));
            getCgst = getCgst.concat(",");
            if (igst == true) {
                tblRw.removeView(cgst);
            } else
//                        gst.
                cgst.setText(String.valueOf(gstValue));
            amt *= quantityStr;
            price.setText(stringList[3]);

            quantity.setText(stringList[4]);
            getQuantity = getQuantity.concat(stringList[4]);
            getQuantity = getQuantity.concat(",");

            amount.setText(String.valueOf(amt));
            getAmount = getAmount.concat(String.valueOf(amt));
            getAmount = getAmount.concat(",");

            totalPrice += priceStr;
            totalAmount += amt;
            totalquantity += quantityStr;
        }

        finalprice.setText(String.valueOf(totalPrice));
        finalquantity.setText(String.valueOf(totalquantity));
        finalamount.setText(String.valueOf(totalAmount));

        float totalDiscount = Float.valueOf(discount);
        float tDiscount = totalDiscount * amt / 100;
        DiscountValue.setText(String.valueOf(tDiscount));

        float totalTransport = Float.valueOf(transport);
        totalAmount += totalTransport;

        totalAmount -= tDiscount;

        finalPayable.setText(String.valueOf(totalAmount));

        str = extras.getString("date");
        String str2 = getAllProducts;
        Log.v("Products---------", str2);
        Log.v("Products---------", getAllProducts);
        Log.v("Date Put---------", str);

        date.setText(str);
        str = extras.getString("validdate");
        validdate.setText(str);

        fillHashMap();
    }


    void fillHashMap(){

        hsmap.put("date", date.getText().toString());
        hsmap.put("name", name.getText().toString());
        hsmap.put("validdate", validdate.getText().toString());
        hsmap.put("address", address.getText().toString());
        hsmap.put("company", company.getText().toString());
        hsmap.put("country", country.getText().toString());
        hsmap.put("pin", pin.getText().toString());
        hsmap.put("state", state.getText().toString());
        hsmap.put("state1", state1.getText().toString());

        hsmap.put("finalprice", finalprice.getText().toString());
        hsmap.put("finalquantity", finalquantity.getText().toString());
        hsmap.put("transportfee", TransportationCost.getText().toString());
        hsmap.put("discountValue", DiscountValue.getText().toString());
        hsmap.put("discountText", DiscountTextview.getText().toString());
        hsmap.put("finalpayable", finalPayable.getText().toString());


        Log.v("Products---------", getAllProducts);
        hsmap.put("products", getAllProducts.toString());
        hsmap.put("cgst", getCgst.toString());
        hsmap.put("gst", getGst.toString());
        hsmap.put("prices", getPrice.toString());
        hsmap.put("quantities", getQuantity.toString());
        hsmap.put("hsncode", getHsn.toString());
        hsmap.put("amount", getAmount.toString());

        hsmap.put("bname", b_name.getText().toString());
    }

    void initialiseLayouts() {
        date = (TextView) findViewById(R.id.date_tv);
        validdate = (TextView) findViewById(R.id.validdate_tv);
        quantity = (TextView) findViewById(R.id.tvquantity);

        name = (TextView) findViewById(R.id.client_name);
        address = (TextView) findViewById(R.id.textView19);
        state = (TextView)findViewById(R.id.text_state);
        pin = (TextView)findViewById(R.id.text_pin);
        company = (TextView) findViewById(R.id.textView22);
        country = (TextView) findViewById(R.id.textView21);

        state1 = (TextView)findViewById(R.id.text_state1);
        sgst = (TextView)findViewById(R.id.sgst);
        cgst1 = (TextView)findViewById(R.id.cgst);

        item = (TextView) findViewById(R.id.tvproduct);
        hsn = (TextView) findViewById(R.id.tvhsn);
        gst = (TextView) findViewById(R.id.tvgst);
        price = (TextView) findViewById(R.id.tvprice);

        finalprice = (TextView) findViewById(R.id.itemsPrice);
        finalquantity = ( TextView) findViewById(R.id.finalQuantity);
        finalamount = (TextView) findViewById(R.id.finalAmount);
        finalPayable = (TextView) findViewById(R.id.textView25);
        TransportationCost =(TextView) findViewById(R.id.textView18);
        DiscountValue =(TextView ) findViewById(R.id.textView27);
        DiscountTextview =(TextView ) findViewById(R.id.textView20);

        lyt = (LinearLayout) findViewById(R.id.tableRow2);
        cl_pdflayout = (ConstraintLayout) findViewById(R.id.cl_pdf);
//        cl_pdflayout1 = (ConstraintLayout) findViewById(R.id.cl_pdf1);

        b_name = (TextView) findViewById(R.id.textView13);
        b_address = (TextView) findViewById(R.id.textView14);
        b_pin = (TextView) findViewById(R.id.pin_cuntry);
        b_state = (TextView) findViewById(R.id.text_state);
        b_mobile = (TextView) findViewById(R.id.textView15);
        b_pan = (TextView) findViewById(R.id.textView16);

        globalVariable = GlobalVariable.getInstance();

        selectedObject = new SelectedObject();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.edit), getResources().getString(R.string.action_edit)));
        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.pdf), getResources().getString(R.string.action_pdf)));
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case 1:
                Intent intent = new Intent(Invoice.this, CreateQuotation.class);

                startActivity(intent);

                finish();

                break;

            case 2:

                LayoutInflater layoutinflater = LayoutInflater.from(this);
                View promptUserView = layoutinflater.inflate(R.layout.name_dialog_box, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setView(promptUserView);

                userAnswer = (EditText) promptUserView.findViewById(R.id.username);

                alertDialogBuilder.setTitle("Save Invoice File Name.");

                // prompt for username
                alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        fileName =  userAnswer.getText().toString();
                        targetPdf =  fileName + ".pdf";

//                        fn_permission();

                        if (boolean_permission) {
                            progressDialog = new ProgressDialog(Invoice.this);
                            progressDialog.setMessage("Please wait");
                            bitmap = loadBitmapFromView(cl_pdflayout, cl_pdflayout.getWidth(), cl_pdflayout.getHeight());
                            createPdf();
                        }
                        if (boolean_save) {
                            Intent intent1 = new Intent(getApplicationContext(), PDFViewActivity.class);
                            intent1.putExtra("FileName",targetPdf);
                            startActivity(intent1);
                        }

                        UploadPdf();
                    }

                });

                // all set and time to build and show up!
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Properties getHashMap(){

        Properties prHsmp = new Properties();

        for(HashMap.Entry<String, String> ent: hsmap.entrySet()){
            prHsmp.put(ent.getKey(), ent.getValue());
        }
        return prHsmp;
    }
    @SuppressLint("LongLogTag")
    private void createTextFile(){

        Properties currentFileInfo = getHashMap();
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
            Date now = new Date();
            String fName = formatter.format(now) ;

            targetPdf = fileName + ".txt";

            File root = new File(getFilesDir() + "/", "Report");

            if (!root.exists())
            {
                root.mkdirs();
            }

            fName = fileName + ".txt";
            File gpxfile = new File(root, fName);
            FileWriter writer = new FileWriter(gpxfile,true);
            currentFileInfo.store(writer, null);
            writer.flush();
            writer.close();
            String path = gpxfile.getAbsolutePath();
            targetPdf = path;
//            Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void UploadPdf() {

        //getting name for the image
        String name = userAnswer.getText().toString().trim();

        createTextFile();

        if (targetPdf == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                GlobalVariable gblVar = GlobalVariable.getInstance();

                String emailId = "";
                if(gblVar.currentUserEmail != null)
                {
                    emailId = gblVar.currentUserEmail;
                }
                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(targetPdf, "txt") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("email", emailId)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        targetPdf = fileName + ".pdf";

        File root = new File(Environment.getExternalStorageDirectory(), "PCDGroup");
        File PcdQuotation = new File(root,"Quotation");

        if(!PcdQuotation.exists())
        {
            PcdQuotation.mkdirs();
            Toast.makeText(Invoice.this, "Make directory", Toast.LENGTH_LONG).show();
        }

        File gpxfile = new File(PcdQuotation, targetPdf);

        try {

            document.writeTo(new FileOutputStream(gpxfile));

            FileWriter writer = new FileWriter(gpxfile,true);
            writer.flush();
            writer.close();
            String path = gpxfile.getAbsolutePath();
            targetPdf = path;
            boolean_save=true;

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + PcdQuotation + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

   /* private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Invoice.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(Invoice.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Invoice.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(Invoice.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
    }*/

    @Override
    public void onBackPressed() {
        finish();
    }

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
        sgst = null;
        cgst1 = null;
        item = null;
        hsn = null;
        gst = null;
        cgst = null;
        price = null;
        quantity = null;
        amount = null;
        finalprice = null;
        finalquantity = null;
        finalamount = null;
        b_name = null;
        b_address = null;
        b_pin = null;
        b_state = null;
        b_mobile = null;
        b_pan = null;
        date = null;
        validdate = null;
        finalPayable = null;
        TransportationCost = null;
        DiscountValue = null;
        DiscountTextview = null;
        userAnswer = null;
    }
}
