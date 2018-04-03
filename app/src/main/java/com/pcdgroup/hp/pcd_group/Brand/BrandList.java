package com.pcdgroup.hp.pcd_group.Brand;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.pcdgroup.hp.pcd_group.Product.UploadImage;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.Quotation.Invoice;
import com.pcdgroup.hp.pcd_group.Quotation.PDFViewActivity;
import com.pcdgroup.hp.pcd_group.R;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class BrandList extends AppCompatActivity {

    ListView listView;

    EditText brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brandlist);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddString();
            }
        });

        listView = (ListView) findViewById(R.id.lstv_brand);
    }

    public void AddString(){
        LayoutInflater layoutinflater = LayoutInflater.from(this);
        View promptUserView = layoutinflater.inflate(R.layout.brand_dialog_box, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptUserView);

        brand = (EditText) promptUserView.findViewById(R.id.brnadname);

        alertDialogBuilder.setTitle("Enter Brand Name.");

        // prompt for username
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });

        alertDialogBuilder .setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });


        // all set and time to build and show up!
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
}
