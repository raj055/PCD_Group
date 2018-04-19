package com.pcdgroup.hp.pcd_group.Quotation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.PageSize;
import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import javax.xml.validation.Validator;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */
public class Invoice extends AppCompatActivity {

    TextView name,address,state,company,country,add1,add2,pin;
    TextView state1,sgst,cgst1;
    TextView item,hsn,gst,cgst,price,quantity,amount;
    TextView finalprice, finalquantity, finalamount;

    TextView date,validdate, finalPayable;

    EditText userAnswer;
    float gstValue;

    String state_holder,state1_holder;

    float totalPrice, totalAmount;
    int totalquantity;

    public static int REQUEST_PERMISSIONS = 1;
    ConstraintLayout cl_pdflayout,cl_pdflayout1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap,bitmap1;
    ProgressDialog progressDialog;
    HashMap<String, String> hsmap = new HashMap<String, String>();
    public static final String UPLOAD_URL =
                                    "http://pcddata-001-site1.1tempurl.com/uploadtxtfile.php";
    String fileName, targetPdf;
    LinearLayout lyt;
    String getAllProducts, getGst, getCgst, getPrice, getQuantity, getAmount, getHsn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice);

        initialiseLayouts();

        state1.setText("maharashtra");

        String str;
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                // Client
                String[]   clientInfo =  extras.getStringArray("ClientInfo");

                name.setText(clientInfo[7]);
                str = clientInfo[0] + "\n" + clientInfo[1] + "\n"+ clientInfo[2];
                address.setText(str);
                pin.setText(clientInfo[3]);
                state.setText(clientInfo[4]);
                company.setText(clientInfo[6]);
                country.setText(clientInfo[5]);

                //Product
                ArrayList<String[]> PrdList = (ArrayList<String[]>) extras.getSerializable("ProductInfo");
                String[]   productInfo =  extras.getStringArray("ProductInfo");

                int size = PrdList.size();

                getAllProducts = "";
                Log.v("Products---------", getAllProducts);
                getGst = new String();
                getPrice = new String();
                getCgst = new String();
                getQuantity = new String();
                getAmount = new String();
                getHsn = new String();

                for(int i = 0; i < size; i++){

                    String[] stringList = PrdList.get(i);

                    View child = (View) getLayoutInflater().inflate(R.layout.product_list, null);
                    lyt.addView(child);

                    item = (TextView)child.findViewById(R.id.tvproduct);
                    hsn = (TextView)child.findViewById(R.id.tvhsn);
                    gst = (TextView)child.findViewById(R.id.tvgst);
                    price = (TextView)child.findViewById(R.id.tvprice);
                    quantity = (TextView)child.findViewById(R.id.tvquantity);
                    cgst = (TextView)child.findViewById(R.id.sgst);
                    amount = (TextView)child.findViewById(R.id.amount);

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
                    getPrice =getPrice.concat(",");
                    Integer quantityStr = Integer.valueOf(stringList[4]);

                    float amt = gstValue * priceStr/100 + priceStr;
                    gstValue /= 2;
                    gst.setText(String.valueOf(gstValue));
                    getGst = getGst.concat(String.valueOf(gstValue));
                    getGst = getGst.concat(",");
                    getCgst = getCgst.concat(String.valueOf(gstValue));
                    getCgst = getCgst.concat(",");

                    cgst.setText(String.valueOf(gstValue));
                    amt *= quantityStr;
                    price.setText(stringList[3]);

                    quantity.setText(stringList[4]);
                    getQuantity = getQuantity.concat(stringList[4]);
                    getQuantity = getQuantity.concat(",");

                    amount.setText(String.valueOf(amt));
                    getAmount = getAmount.concat(String.valueOf(amt));
                    getAmount =getAmount.concat(",");

                    totalPrice +=  priceStr;
                    totalAmount += amt;
                    totalquantity += quantityStr;
                }
                finalprice.setText(String.valueOf(totalPrice));
                finalquantity.setText(String.valueOf(totalquantity));
                finalamount.setText(String.valueOf(totalAmount));
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

            state_holder = state.getText().toString();
            state1_holder = state1.getText().toString();

            if (state1_holder.contains(state_holder)){

                sgst.setText("SGST");
                cgst1.setText("CGST");

            }else
            {
                sgst.setVisibility(View.INVISIBLE);
                cgst1.setText("IGST");

                cgst.setVisibility(View.INVISIBLE);
                gstValue /= 1;
            }
        }
    }

    void fillHashMap(){
        hsmap.put("date", date.getText().toString());
        hsmap.put("validdate", validdate.getText().toString());
        hsmap.put("address", address.getText().toString());
        hsmap.put("company", company.getText().toString());
        hsmap.put("country", country.getText().toString());

        hsmap.put("finalprice", finalprice.getText().toString());
        hsmap.put("finalquantity", finalquantity.getText().toString());
        hsmap.put("finalamount", finalamount.getText().toString());

        Log.v("Products---------", getAllProducts);
        hsmap.put("products", getAllProducts.toString());
        hsmap.put("cgst", getCgst.toString());
        hsmap.put("gst", getGst.toString());
        hsmap.put("prices", getPrice.toString());
        hsmap.put("quantities", getQuantity.toString());
        hsmap.put("hsncode", getHsn.toString());
        hsmap.put("amount", getAmount.toString());
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
        finalquantity = (TextView) findViewById(R.id.finalQuantity);
        finalamount = (TextView) findViewById(R.id.finalAmount);
        finalPayable = (TextView) findViewById(R.id.textView25);

        lyt = (LinearLayout) findViewById(R.id.tableRow2);
        cl_pdflayout = (ConstraintLayout) findViewById(R.id.cl_pdf);
        cl_pdflayout1 = (ConstraintLayout) findViewById(R.id.cl_pdf1);

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

                alertDialogBuilder.setTitle("Save File Name.");

                // prompt for username
                alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        fileName =  userAnswer.getText().toString();
                        targetPdf = "/sdcard/" + fileName + ".pdf";
                        fn_permission();

                        if (boolean_permission) {
                            progressDialog = new ProgressDialog(Invoice.this);
                            progressDialog.setMessage("Please wait");
                            bitmap = loadBitmapFromView(cl_pdflayout, cl_pdflayout.getWidth(), cl_pdflayout.getHeight());
                            bitmap1 = loadBitmapFromView(cl_pdflayout1, cl_pdflayout1.getWidth(), cl_pdflayout1.getHeight());
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
//        HashMap<String, String> hsmap = new HashMap<String, String>();
//        hsmap.put("date", date.getText().toString());
//        hsmap.put("validdate", validdate.getText().toString());
////        hsmap.put("quantity", quantity.getText().toString());
//        hsmap.put("address", address.getText().toString());
//        hsmap.put("company", company.getText().toString());
//        hsmap.put("country", country.getText().toString());
////        hsmap.put("item", item.getText().toString());
////        hsmap.put("hsn", hsn.getText().toString());
////        hsmap.put("gst", gst.getText().toString());
////        hsmap.put("price", price.getText().toString());
//        hsmap.put("finalprice", finalprice.getText().toString());
//        hsmap.put("finalquantity", finalquantity.getText().toString());
//        hsmap.put("finalamount", finalamount.getText().toString());
//        hsmap.put("finalamount", finalamount.getText().toString());

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

            targetPdf = "/sdcard/" +  fileName + ".txt";
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
            Log.v("FileName::::::::::::::::::::::::::::::", path);
            Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
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
        float hight = PageSize.A4.getHeight() ;
        float width = PageSize.A4.getWidth() ;

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

        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(convertWidth,convertHighet, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        canvas.drawPaint(paint);

        bitmap1 = Bitmap.createScaledBitmap(bitmap1, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap1, 0, 0 , null);
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

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void fn_permission() {
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
    }

    @Override
    public void onBackPressed() {

//        Intent intent = new Intent(Invoice.this, AdminDashboard.class);
//        startActivity(intent);

        finish();
    }
}
