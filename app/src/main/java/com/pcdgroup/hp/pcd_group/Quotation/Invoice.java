package com.pcdgroup.hp.pcd_group.Quotation;

import android.Manifest;
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
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
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

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */
public class Invoice extends AppCompatActivity {

    TextView name,address,state,company,country,add1,add2,pin;

    TextView item,hsn,gst,cgst,price,quantity,amount;
    TextView finalprice, finalquantity, finalamount;

    TextView date,validdate, finalPayable;

    EditText userAnswer;

    float totalPrice, totalAmount;
    int totalquantity;

    public static int REQUEST_PERMISSIONS = 1;
    ConstraintLayout cl_pdflayout;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;

    public static final String UPLOAD_URL = "http://pcddata-001-site1.1tempurl.com/server_upload_pdf.php";
    String fileName, targetPdf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice);

        date = (TextView)findViewById(R.id.date_tv);
        validdate = (TextView)findViewById(R.id.validdate_tv);
        quantity = (TextView)findViewById(R.id.tvquantity);

        name = (TextView)findViewById(R.id.textView18);
        address = (TextView)findViewById(R.id.textView19);
        company = (TextView)findViewById(R.id.textView22);
        country = (TextView)findViewById(R.id.textView21);

        item = (TextView)findViewById(R.id.tvproduct);
        hsn = (TextView)findViewById(R.id.tvhsn);
        gst = (TextView)findViewById(R.id.tvgst);
        price = (TextView)findViewById(R.id.tvprice);

        finalprice = (TextView) findViewById(R.id.itemsPrice);
        finalquantity = (TextView) findViewById(R.id.finalQuantity);
        finalamount = (TextView) findViewById(R.id.finalAmount);
        finalPayable = (TextView) findViewById(R.id.textView25);

        LinearLayout lyt = (LinearLayout)findViewById(R.id.tableRow2);
        cl_pdflayout = (ConstraintLayout) findViewById(R.id.cl_pdf);

        String str;
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                // Client
                String[]   clientInfo =  extras.getStringArray("ClientInfo");

                name.setText(clientInfo[0]);
                str =clientInfo[0] + "\n" + clientInfo[1] + "\n"+ clientInfo[2] + "\n"+ clientInfo[3] + "\n"+ clientInfo[4];

                address.setText(str);
                company.setText(clientInfo[6]);
                country.setText(clientInfo[5]);

                //Product
                ArrayList<String[]> PrdList = (ArrayList<String[]>) extras.getSerializable("ProductInfo");
                String[]   productInfo =  extras.getStringArray("ProductInfo");

                int size = PrdList.size();
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
                    hsn.setText(stringList[1]);
                    float gstValue = Integer.valueOf(stringList[2]);
                    float priceStr = Float.valueOf(stringList[3]);
                    Integer quantityStr = Integer.valueOf(stringList[4]);

                    float amt = gstValue * priceStr/100 + priceStr;
                    gstValue /= 2;
                    gst.setText(String.valueOf(gstValue));
                    cgst.setText(String.valueOf(gstValue));
                    amt *= quantityStr;
                    price.setText(stringList[3]);
                    quantity.setText(stringList[4]);
                    amount.setText(String.valueOf(amt));


                    totalPrice +=  priceStr;
                    totalAmount += amt;
                    totalquantity += quantityStr;
                }
                finalprice.setText(String.valueOf(totalPrice)); ;
                finalquantity.setText(String.valueOf(totalquantity));
                finalamount.setText(String.valueOf(totalAmount));
                finalPayable.setText(String.valueOf(totalAmount));

                str = extras.getString("date");
                date.setText(str);
                str = extras.getString("validdate");
                validdate.setText(str);
            }
        }
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

    private void UploadPdf() {

        //getting name for the image
        String name = userAnswer.getText().toString().trim();

        if (targetPdf == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(targetPdf, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
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

        Intent intent = new Intent(Invoice.this, AdminDashboard.class);
        startActivity(intent);

        finish();

    }
}
