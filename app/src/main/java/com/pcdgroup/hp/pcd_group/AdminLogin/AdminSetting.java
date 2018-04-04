package com.pcdgroup.hp.pcd_group.AdminLogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.Brand.BrandList;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.Quotation.CreateQuotation;
import com.pcdgroup.hp.pcd_group.R;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class AdminSetting extends AppCompatActivity {

    Button Address, Brand, Access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsetting);

        Address = (Button) findViewById(R.id.btn_address);
        Brand = (Button) findViewById(R.id.btn_brand);
        Access = (Button) findViewById(R.id.btn_access);

        // Click Address button
        Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckAnyAddress();
            }
        });

        // Click Brand button
        Brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSetting.this, BrandList.class);

                startActivity(intent);

                finish();

            }
        });

        // Click Access button
        Access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSetting.this, AdminSetting.class);

                startActivity(intent);

                finish();

            }
        });
    }

    private void CheckAnyAddress() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminSetting.this);
        builder.setTitle("Select Client");

        // add a radio button list
        String[] Client = {"raj","sameer"};
        int checkedItem = 1; // cow
        builder.setSingleChoiceItems(Client, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });
        builder.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
