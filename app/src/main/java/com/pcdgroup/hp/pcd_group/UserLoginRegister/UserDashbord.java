package com.pcdgroup.hp.pcd_group.UserLoginRegister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.ClientRegisterActivity;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.SharedPreferences.MySharedPreferences;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class UserDashbord extends AppCompatActivity {

    Button LogOut, Client_Details, Product;
    TextView EmailShow;
    String EmailHolder;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);

        //Assign Id'S
        LogOut = (Button)findViewById(R.id.button);
        Client_Details = (Button)findViewById(R.id.clientdetails);
        Product = (Button)findViewById(R.id.imgupload);

        EmailShow = (TextView)findViewById(R.id.EmailShow);
        EmailShow.setText(mUsername);

        // Click logout button
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserDashbord.this, MainActivity.class);

                startActivity(intent);

                finish();

                Toast.makeText(UserDashbord.this, "Log Out Successfully", Toast.LENGTH_LONG).show();
            }
        });

        // intent
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(MainActivity.UserEmail);
        EmailShow.setText(EmailHolder);

        /*// check to see if the user is already logged in
        mUsername = MySharedPreferences.getUsername(this);
        if (mUsername == null) {
            Intent loginIntent = new Intent(UserDashbord.this, MainActivity.class);
            startActivity(loginIntent);
            return;
        }*/

        // Click Client_details button
        Client_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserDashbord.this, ClientDetailsActivity.class);

                startActivity(intent);
            }
        });

        // Click image upload button
        Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserDashbord.this, ViewImage.class);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserDashbord.this);
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
