package com.pcdgroup.hp.pcd_group.AdminLogin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Discount.Client_Discount;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.OrderList.Order_List;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.PurchaseOrder.FinishedOrder;
import com.pcdgroup.hp.pcd_group.PurchaseOrder.PO_List;
import com.pcdgroup.hp.pcd_group.Quotation.CreateQuotation;
import com.pcdgroup.hp.pcd_group.Quotation.Invoice;
import com.pcdgroup.hp.pcd_group.Quotation.List_Quotation_Pdfs;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.VendorDealer.VendorDealerMain;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class AdminDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button LogOut, Client_Details, Image_upload, Quotation_pdf ,Quotation, Access, Orderlist,
            Discount, Vendor,Purchase_order, Procure_product;
    TextView EmailShow;
    public static String EmailHolder = "";
    Intent intent;
    private DrawerLayout drawer;

   Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindashboard);

        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.activity_main_layout);

        // Inflating layout
        LayoutInflater inflater = getLayoutInflater();
        // Setting view you want to display as a row element
        View view = inflater.inflate(R.layout.main_dashbord, mainLayout, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewmain);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this, ClientDetailsActivity.class);
                startActivity(intent);
            }
        });

        mainLayout.addView(view);

       /* //Assign Id'S
        Client_Details = (Button) findViewById(R.id.clientdetails);
        Image_upload = (Button) findViewById(R.id.imgupload);
        Quotation = (Button) findViewById(R.id.quotation);

        EmailShow = (TextView) findViewById(R.id.EmailShow);

        // intent
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(MainActivity.UserEmail);
        EmailShow.setText(EmailHolder);

        GlobalVariable gblVar = GlobalVariable.getInstance();
        gblVar.currentUserEmail = EmailHolder;

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


        // Click Quotation button
        Quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminDashboard.this, CreateQuotation.class);

                startActivity(intent);
            }
        });*/

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_quotation:
                intent = new Intent(AdminDashboard.this,List_Quotation_Pdfs.class);
                startActivity(intent);
                break;

            case R.id.nav_order:
                intent = new Intent(AdminDashboard.this,Order_List.class);
                startActivity(intent);
                break;

            case R.id.nav_vendordealer:
                intent = new Intent(AdminDashboard.this,VendorDealerMain.class);
                startActivity(intent);
                break;

            case R.id.nav_po:
                intent = new Intent(AdminDashboard.this,PO_List.class);
                startActivity(intent);
                break;

            case R.id.nav_finish:
                intent = new Intent(AdminDashboard.this,FinishedOrder.class);
                startActivity(intent);
                break;

            case R.id.nav_logout:
                intent = new Intent(AdminDashboard.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 1, 1,  menuIconWithText(getResources().getDrawable(R.drawable.settings), getResources().getString(R.string.action_brandsettings)));
        menu.add(0, 2, 2,  menuIconWithText(getResources().getDrawable(R.drawable.login),getResources().getString(R.string.action_accesslogin)));
        menu.add(0, 3, 3,  menuIconWithText(getResources().getDrawable(R.drawable.percentage),getResources().getString(R.string.action_discount)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case 1:
                intent = new Intent(AdminDashboard.this, AdminSetting.class);
                startActivity(intent);
                break;

            case 2:
                intent = new Intent(AdminDashboard.this, AccessAdmin.class);
                startActivity(intent);
                break;

            case 3:
                intent = new Intent(AdminDashboard.this, Client_Discount.class);
                startActivity(intent);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @Override
    public void onBackPressed() {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

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

    }
}