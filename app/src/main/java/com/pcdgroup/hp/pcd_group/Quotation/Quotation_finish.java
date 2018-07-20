package com.pcdgroup.hp.pcd_group.Quotation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.R;

import java.util.Calendar;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name
 * @description
 */

public class Quotation_finish extends AppCompatActivity {

    Button Finish,validdate;
    private EditText transportationcost,discountprice;
    private TextView textdate, textvaliddate,tvDiscount;
    GlobalVariable globalVariable;
    String Tpcost,discount,DiscountVallue;
    String brand,client;

    private int year;
    private int month;
    private int day;

    static final int DATE_PICKER_ID = 1111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_finish_slide);

        brand = getIntent().getExtras().getString("SelectedBrand");
        client = getIntent().getExtras().getString("ClientInfo");

        globalVariable = GlobalVariable.getInstance();

        tvDiscount = (TextView) findViewById(R.id.tv_discount);
        textdate = (TextView) findViewById(R.id.tv_date);
        textvaliddate = (TextView) findViewById(R.id.tv_uptodate);

        transportationcost = (EditText) findViewById(R.id.et_trasport);
        discountprice = (EditText) findViewById(R.id.et_discount);

        validdate = (Button) findViewById(R.id.btn_validupto);
        Finish = (Button) findViewById(R.id.btn_finish);

        DiscountVallue = globalVariable.DiscountType;

        tvDiscount.setText("Discount" + "\t\t" + DiscountVallue + "%");

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        textdate.setText(new StringBuilder()
                .append(day).append("-")
                .append(month + 1).append("-")
                .append(year).append(" "));

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
                            builder = new AlertDialog.Builder(Quotation_finish.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(Quotation_finish.this);
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

        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Quotation_finish.this, Invoice.class);

                //customer
                intent.putExtra("ClientInfo", client);
                //brand
                intent.putExtra("SelectedBrand",brand);

                intent.putExtra("date", textdate.getText());
                intent.putExtra("validdate", textvaliddate.getText());

                Tpcost = transportationcost.getText().toString();
                intent.putExtra("transportioncost",Tpcost);

                discount = discountprice.getText().toString();
                intent.putExtra("discountperce",discount);

                startActivity(intent);
                finish();
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        transportationcost = null;
        discountprice = null;
        validdate = null;
        textdate = null;
        textvaliddate = null;
        tvDiscount = null;
    }
}
