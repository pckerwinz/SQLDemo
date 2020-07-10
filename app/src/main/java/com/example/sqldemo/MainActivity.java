package com.example.sqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //reference to buttons and other controls on the layout
    Button btn_viewAll, btn_add;
    EditText et_name, et_age;
    Switch sw_activeCustomer;
    ListView lv_customerList;
    ArrayAdapter customerArrayAdapter;
    DataBaseHelper dataBaseHelper;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_viewAll = findViewById(R.id.btn_viewAll);
        btn_add = findViewById(R.id.btn_add);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        sw_activeCustomer = findViewById(R.id.sw_active);
        lv_customerList = findViewById(R.id.lv_customerList);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        ShowCustomerOnListView(dataBaseHelper);


        //button listeners for the add and view all buttons


        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CustomerModel customerModel;
                try {
                    customerModel = new CustomerModel(-1, et_name.getText().toString(),
                            Integer.parseInt(et_age.getText().toString()),sw_activeCustomer.isChecked());
                   // Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                    boolean success = dataBaseHelper.addOne(customerModel);
                    ShowCustomerOnListView(dataBaseHelper);

                    Toast.makeText(MainActivity.this, "Success= "+success, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onClick: " + success );

                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error in Creating Customer".toString(), Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1, "error",0,false);
                }




            }
        });

        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                List<CustomerModel> all = dataBaseHelper.getAll();

                ShowCustomerOnListView(dataBaseHelper);
//                Toast.makeText(MainActivity.this, all.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
            CustomerModel clickCustomer = (CustomerModel) parent.getItemAtPosition(position);

            dataBaseHelper.deleteOne(clickCustomer);
            ShowCustomerOnListView(dataBaseHelper);
                Toast.makeText(MainActivity.this, "Deleted "+ clickCustomer.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowCustomerOnListView(DataBaseHelper dataBaseHelper2) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getAll());
        lv_customerList.setAdapter(customerArrayAdapter);
    }
}