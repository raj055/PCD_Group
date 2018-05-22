package com.pcdgroup.hp.pcd_group.Quotation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.AdminLogin.AccessAdmin;
import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.AdminLogin.BrandAdepter;
import com.pcdgroup.hp.pcd_group.AdminLogin.Category;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserRegistarActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class CreateQuotation extends AppCompatActivity {

    private EditText quantity,transportationcost,discountprice;
    private Button add_client,add_product,preview,date,validdate,addAddress;
    public TextView client, textdate, textvaliddate, textaddres;
    public ListView product;
    public TextView tvDiscount;
    Boolean CheckEditText;
    GlobalVariable globalVariable;
    ProductListAdapter itemsAdapter;
    public ArrayList<ProductInfoAdapter>  items = new ArrayList<ProductInfoAdapter>();
    public ArrayList<String[]> PrdList = new ArrayList<String[]>();

    String HttpURL_get = "http://dert.co.in/gFiles/listbrands.php";

    List<Category> categoriesList;
    BrandAdepter adepter;
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;

    Intent intent;
    String Tpcost,discount,DiscountVallue;

    private int year;
    private int month;
    private int day;

    static final int DATE_PICKER_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        globalVariable = GlobalVariable.getInstance();

        tvDiscount = (TextView) findViewById(R.id.textView12);

        quantity = (EditText) findViewById(R.id.quantity_et);
        transportationcost = (EditText) findViewById(R.id.editText10);
        discountprice = (EditText) findViewById(R.id.editText11);

        client = (TextView) findViewById(R.id.tv_client);
        product = (ListView) findViewById(R.id.tv_product);
        textdate = (TextView) findViewById(R.id.tv_date);
        textvaliddate = (TextView) findViewById(R.id.tv_uptodate);
        textaddres = (TextView) findViewById(R.id.address);

        add_client = (Button) findViewById(R.id.btn_client);
        add_product = (Button) findViewById(R.id.btn_product);
        preview = (Button) findViewById(R.id.btn_preview);
        date = (Button) findViewById(R.id.btn_date);
        validdate = (Button) findViewById(R.id.btn_validupto);
        addAddress = (Button) findViewById(R.id.btn_address);

        categoriesList = new ArrayList<Category>();
        adepter = new BrandAdepter(this, categoriesList);

        DiscountVallue = globalVariable.DiscountType;

        tvDiscount.setText("Discount" + "\t" + DiscountVallue + "%");

        getData();

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        textdate.setText(new StringBuilder()
                .append(day).append("-")
                .append(month + 1).append("-")
                .append(year).append(" "));

        itemsAdapter = new ProductListAdapter(this,  items);
        product.setAdapter(itemsAdapter);

        // Client add in database
        add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateQuotation.this, SelectClient.class);

                startActivityForResult(intent, 1);
            }
        });

        // Product add in database
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateQuotation.this, SelectProduct.class);

                startActivityForResult(intent, 3);

            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateQuotation.this, BrandList.class);

                startActivityForResult(intent, 2);

            }
        });

        // Date add
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialog(DATE_PICKER_ID);
            }
        });

        // Valid up to Date
        validdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });

        discountprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

//                String str = editable.toString();
               final String str = new String(editable.toString());

                if((str != null) && (str != "") && (str.matches("^[0-9]+$"))) {
                    int discount=Integer.parseInt(str);
                    int currDiscountVal=Integer.parseInt(DiscountVallue);
                    if (discount > currDiscountVal) {

                        Toast.makeText(getApplicationContext(), "Discount Value wrong.", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(CreateQuotation.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(CreateQuotation.this);
                        }
                        builder.setTitle("Wrong Discount Value")
                                .setMessage("Please Enter Perfect Value Of Discount.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        String str = discountprice.getText().toString();
                                        str = str.substring ( 0, str.length() - 1 );
                                        discountprice.setText(str);

                                        dialog.cancel();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }
        });

        // Preview add in database
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateQuotation.this, Invoice.class);
                //customer
                intent.putExtra("ClientInfo", globalVariable.globalClient);

                //product
                int itemsCount = 0;
                for (ProductInfoAdapter pradap: items){

                    String[] glstr = PrdList.get(itemsCount++);
                    if(glstr != null)
                        glstr[4] =  pradap.getAmount();
                }
                intent.putExtra("ProductInfo", PrdList);
    //                intent.putExtra("proqunt", quantity.getText());
                intent.putExtra("date", textdate.getText());
                intent.putExtra("validdate", textvaliddate.getText());

                Tpcost = transportationcost.getText().toString();
                discount = discountprice.getText().toString();

                intent.putExtra("transportioncost",Tpcost);
                intent.putExtra("discountperce",discount);

                intent.putExtra(("SelectedBrand"),globalVariable.globalBarnd);

                startActivity(intent);
                finish();
            }
        });

        quantity.addTextChangedListener(new TextWatcher() {
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
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Client Details
                    if (extras.containsKey("address")) {
                        globalVariable.globalClient[0] = extras.getString("address");
                        globalVariable.globalClient[1] = extras.getString("ad1");
                        globalVariable.globalClient[2] = extras.getString("ad2");
                        globalVariable.globalClient[3] = extras.getString("pin");
                        globalVariable.globalClient[4] = extras.getString("state");
                        globalVariable.globalClient[5] = extras.getString("country");
                        globalVariable.globalClient[6] = extras.getString("company");
                        globalVariable.globalClient[7] = extras.getString("name");
                    }
                    client.setText(globalVariable.globalClient[7]);
                }
            }
        }
        if (requestCode == 2) {
                if(resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {

                        //Product Details
                        if (extras.containsKey("name")) {
                            globalVariable.globalBarnd[0] = extras.getString("name");
                            globalVariable.globalBarnd[1] = extras.getString("address");
                            globalVariable.globalBarnd[2] = extras.getString("address1");
                            globalVariable.globalBarnd[3] = extras.getString("address2");
                            globalVariable.globalBarnd[4] = extras.getString("pincode");
                            globalVariable.globalBarnd[5] = extras.getString("state");
                            globalVariable.globalBarnd[6] = extras.getString("mobileno");
                            globalVariable.globalBarnd[7] = extras.getString("email");
                            globalVariable.globalBarnd[8] = extras.getString("website");
                            globalVariable.globalBarnd[9] = extras.getString("pan");
                            globalVariable.globalBarnd[10] = extras.getString("gst");
                        }
                        textaddres.setText(globalVariable.globalBarnd[0]);
                    }
                }
        }
        else {
            if (requestCode == 3) {
                if(resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {

                        //Product Details
                        if (extras.containsKey("pname")) {
                            globalVariable.globalProduct[0] = extras.getString("pname");
                            Integer temp = new Integer(extras.getInt("phsn"));
                            globalVariable.globalProduct[1] = temp.toString();
                            temp = extras.getInt("pgst");
                            globalVariable.globalProduct[2] = temp.toString();
                            temp = extras.getInt("pprice");
                            globalVariable.globalProduct[3] = temp.toString();
                            globalVariable.globalProduct[4] = "0";

                            ProductInfoAdapter tempAdapter = new ProductInfoAdapter();
                            tempAdapter.setName(globalVariable.globalProduct[0]);
                            items.add(tempAdapter);
                            itemsAdapter.notifyDataSetChanged();

                            String[] strpr = globalVariable.globalProduct.clone();
                            PrdList.add(strpr);
                        }
                    }
                }
            }

        }
    }
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            textvaliddate.setText(new StringBuilder()
                    .append(day).append("-")
                    .append(month + 1).append("-")
                    .append(year).append(" "));
        }
    };

    private void getData(){

        try {
            URL url = new URL(HttpURL_get);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is = new BufferedInputStream(con.getInputStream());

        }catch (Exception e){
            e.printStackTrace();
        }

        //Read in content into String
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        //Parse json data
        try {

            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            data = new String[ja.length()];

            for (int i=0; i<ja.length();i++){

                jo=ja.getJSONObject(i);
                String name = jo.getString("name");
                String address = jo.getString("address");
                String address1 = jo.getString("address1");
                String address2 = jo.getString("address2");
                String pincode = jo.getString("pincode");
                String state = jo.getString("state");
                String mobileno = jo.getString("mobileno");
                String email = jo.getString("email");
                String website = jo.getString("website");
                String pan = jo.getString("pan");
                String gst = jo.getString("gst");
                Category e = new Category(name, address, address1, address2, pincode,
                        state, mobileno, email, website, pan, gst);
                categoriesList.add(e);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.home) {
            if (globalVariable.AccessType.contains("Admin")) {

                intent = new Intent(this, AdminDashboard.class);

            }
            else if (globalVariable.AccessType.contains("Manager")) {

                intent = new Intent(this, UserDashbord.class);

            }
            else if (globalVariable.AccessType.contains("Client")) {

                intent = new Intent(this, UserDashbord.class);

            }
            else {

                intent = new Intent(this, ViewImage.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
