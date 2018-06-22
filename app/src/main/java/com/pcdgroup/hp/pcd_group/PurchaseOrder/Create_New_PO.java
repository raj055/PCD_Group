package com.pcdgroup.hp.pcd_group.PurchaseOrder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Quotation.ProductInfoAdapter;
import com.pcdgroup.hp.pcd_group.Quotation.ProductListAdapter;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.VendorDealer.VendorList;
import com.pcdgroup.hp.pcd_group.VendorDealer.VendorProductAdd;

import java.util.ArrayList;

/**
 * @author Grasp
 * @version 1.0 on 18-06-2018.
 */

public class Create_New_PO extends AppCompatActivity {

    Button selectVendor,selectProduct,CreatePurchaseOrder;
    TextView VendorName;
    ListView listView;
    EditText AddQuantity;
    LinearLayout linearVendor,linearProduct;
    GlobalVariable globalVariable;
    ProductListAdapter itemsAdapter;
    public ArrayList<ProductInfoAdapter> items = new ArrayList<ProductInfoAdapter>();
    public ArrayList<String[]> PrdList = new ArrayList<String[]>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_po);

        globalVariable = GlobalVariable.getInstance();

        selectVendor = (Button) findViewById(R.id.btn_add_vendor);
        selectProduct = (Button) findViewById(R.id.btn_add_product);
        CreatePurchaseOrder = (Button) findViewById(R.id.btn_create_po);

        VendorName = (TextView) findViewById(R.id.tv_selectvendor);
        AddQuantity = (EditText) findViewById(R.id.et_addQuantity);
        listView = (ListView) findViewById(R.id.p_list);

        linearVendor = (LinearLayout) findViewById(R.id.ll_vendor);
        linearProduct = (LinearLayout) findViewById(R.id.ll_Product);

        linearVendor.setVisibility(View.INVISIBLE);
        linearProduct.setVisibility(View.INVISIBLE);
        CreatePurchaseOrder.setVisibility(View.INVISIBLE);

        itemsAdapter = new ProductListAdapter(this,  items);
        listView.setAdapter(itemsAdapter);

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

                //vendor
                intent.putExtra("vendorInfo", globalVariable.globalVendor);

                //product
                int itemsCount = 0;
                for (ProductInfoAdapter pradap: items){

                    String[] glstr = PrdList.get(itemsCount++);
                    if(glstr != null)
                        glstr[4] =  pradap.getAmount();
                }
                intent.putExtra("ProductInfo", PrdList);

                startActivity(intent);
                finish();
            }
        });

        AddQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String str = editable.toString();
                int listSize = items.size() ;
                if(listSize != 0) {
                    listSize -= 1;
                    ProductInfoAdapter prinfo = items.get(listSize);
                    prinfo.setAmount(str);
                    itemsAdapter.notifyDataSetChanged();
                }
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

                            Log.v("name =====",globalVariable.globalVendorProduct[0]);
                        }
                        linearProduct.setVisibility(View.VISIBLE);
                        CreatePurchaseOrder.setVisibility(View.VISIBLE);

                        ProductInfoAdapter tempAdapter = new ProductInfoAdapter();
                        tempAdapter.setName(globalVariable.globalVendorProduct[0]);
                        items.add(tempAdapter);
                        itemsAdapter.notifyDataSetChanged();

                        String[] strpr = globalVariable.globalVendorProduct.clone();
                        PrdList.add(strpr);


                    }
                }
            }

        }
    }
}
