package com.pcdgroup.hp.pcd_group.PurchaseOrder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.VendorDealer.VendorList;

/**
 * @author Grasp
 * @version 1.0 on 18-06-2018.
 */

public class Create_New_PO extends AppCompatActivity {

    Button selectVendor,selectProduct,CreatePurchaseOrder;
    TextView VendorName,Product1,Product2,Product3;
    EditText editProduct1,editProduct2,editProduct3;
    LinearLayout linearVendor,linearProduct;
    GlobalVariable globalVariable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_po);

        globalVariable = GlobalVariable.getInstance();

        selectVendor = (Button) findViewById(R.id.btn_add_vendor);
        selectProduct = (Button) findViewById(R.id.btn_add_product);
        CreatePurchaseOrder = (Button) findViewById(R.id.btn_create_po);

        VendorName = (TextView) findViewById(R.id.tv_selectvendor);
        Product1 = (TextView) findViewById(R.id.product1);
        Product2 = (TextView) findViewById(R.id.product2);
        Product3 = (TextView) findViewById(R.id.product3);

        editProduct1 = (EditText) findViewById(R.id.et_p1);
        editProduct2 = (EditText) findViewById(R.id.et_p2);
        editProduct3 = (EditText) findViewById(R.id.et_p3);

        linearVendor = (LinearLayout) findViewById(R.id.ll_vendor);
        linearProduct = (LinearLayout) findViewById(R.id.ll_Product);

        linearVendor.setVisibility(View.INVISIBLE);
        linearProduct.setVisibility(View.INVISIBLE);
        CreatePurchaseOrder.setVisibility(View.INVISIBLE);

        // selectVendor in database
        selectVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Create_New_PO.this, VendorList.class);

                startActivityForResult(intent, 1);

            }
        });

        // selectProduct in database
        selectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Create_New_PO.this, SelectVendorProducts.class);
                intent.putExtra("vendor_email", globalVariable.globalVendor);
                startActivityForResult(intent, 2);

            }
        });

        // CreatePurchaseOrder in database
        CreatePurchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Create_New_PO.this, ViewPurchaseOrder.class);

                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {

                    //Product Details
                    if (extras.containsKey("name")) {
                        globalVariable.globalVendor[0] = extras.getString("id");
                        globalVariable.globalVendor[1] = extras.getString("name");
                        globalVariable.globalVendor[2] = extras.getString("address");
                        globalVariable.globalVendor[3] = extras.getString("location");
                        globalVariable.globalVendor[4] = extras.getString("state");
                        globalVariable.globalVendor[5] = extras.getString("email");
                        globalVariable.globalVendor[6] = extras.getString("mobileno");
                        globalVariable.globalVendor[7] = extras.getString("organisation");
                        globalVariable.globalVendor[8] = extras.getString("gstno");
                        globalVariable.globalVendor[9] = extras.getString("products");
                    }
                    linearVendor.setVisibility(View.VISIBLE);
                    selectVendor.setVisibility(View.INVISIBLE);
                    VendorName.setText(globalVariable.globalVendor[1]);
                }
            }
        }
        else {
            if (requestCode == 2) {
                if(resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {

                        //Product Details
                        if (extras.containsKey("name")) {
                            globalVariable.globalVendorProduct[0] = extras.getString("name");
                            globalVariable.globalVendorProduct[1] = extras.getString("price");
                            globalVariable.globalVendorProduct[2] = extras.getString("hsncode");
                            globalVariable.globalVendorProduct[3] = extras.getString("gst");
                            globalVariable.globalVendorProduct[4] = extras.getString("stock");
                            globalVariable.globalVendorProduct[5] = extras.getString("description");
                            globalVariable.globalVendorProduct[6] = extras.getString("reorderlevel");
                        }
                        linearProduct.setVisibility(View.VISIBLE);
                        CreatePurchaseOrder.setVisibility(View.VISIBLE);
                        selectProduct.setVisibility(View.INVISIBLE);

                        String str = globalVariable.globalVendorProduct[0];

                        Log.v("String value ===== ",str);

                        for (int i =0; i < str.length()  ; i++) {
                            Product1.setText(str);
                            Product2.setText(str);
                            Product3.setText(str);
                        }
                    }
                }
            }

        }
    }
}
