package com.pcdgroup.hp.pcd_group.AdminLogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Discount.Client_Discount;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.OrderList.Order_List;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.Quotation.CreateQuotation;
import com.pcdgroup.hp.pcd_group.Quotation.List_Quotation_Pdfs;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.VendorDealer.VendorDealerMain;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class AdminDashboard extends AppCompatActivity {

    Button LogOut, Client_Details, Image_upload, Quotation_pdf ,Quotation, Access, Orderlist,
            Discount, Vendor;
    TextView EmailShow;
    public static String EmailHolder = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindashboard);

        //Assign Id'S
        LogOut = (Button) findViewById(R.id.button);
        Client_Details = (Button) findViewById(R.id.clientdetails);
        Image_upload = (Button) findViewById(R.id.imgupload);
        Quotation_pdf = (Button) findViewById(R.id.btn_quotation);
        Quotation = (Button) findViewById(R.id.quotation);
        Access = (Button) findViewById(R.id.btn_access);
        Orderlist = (Button) findViewById(R.id.btn_orderlist);
        Discount = (Button) findViewById(R.id.btn_discount);
        Vendor = (Button) findViewById(R.id.btn_vender);

        EmailShow = (TextView) findViewById(R.id.EmailShow);

        // intent
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(MainActivity.UserEmail);
        EmailShow.setText(EmailHolder);

        GlobalVariable gblVar = GlobalVariable.getInstance();
        gblVar.currentUserEmail = EmailHolder;

        // Click logout button
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDashboard.this, MainActivity.class);

                startActivity(intent);

                finish();

                Toast.makeText(AdminDashboard.this, "Log Out Successfully", Toast.LENGTH_LONG).show();
            }
        });

        // Click Client_details button
        Client_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminDashboard.this, ClientDetailsActivity.class);
                intent.putExtra("emailid", EmailHolder);
                startActivity(intent);
            }
        });

        // Click image upload button
        Image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminDashboard.this, ViewImage.class);

                intent.putExtra("email",EmailHolder);

                startActivity(intent);
            }
        });

        // Click Orders button
        Quotation_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminDashboard.this, List_Quotation_Pdfs.class);
                intent.putExtra("emailid", EmailHolder);
                startActivity(intent);
            }
        });

        // Click Quotation button
        Quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminDashboard.this, CreateQuotation.class);

                startActivity(intent);
            }
        });

        // Click Access button
        Access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDashboard.this, AccessAdmin.class);

                startActivity(intent);

            }
        });
        // Click Order button
        Orderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDashboard.this, Order_List.class);

                startActivity(intent);

            }
        });
        // Click Discount button
        Discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDashboard.this, Client_Discount.class);

                startActivity(intent);

            }
        });
        // Click Vender button
        Vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDashboard.this, VendorDealerMain.class);

                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);
        builder.setMessage("Are You Sure Want To Exit ?");
        builder.setCancelable(true);
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.settings), getResources().getString(R.string.action_settings)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(AdminDashboard.this, AdminSetting.class);

        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

}