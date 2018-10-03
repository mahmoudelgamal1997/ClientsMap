package com.example2017.android.clientsmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Select_orders_reports extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_orders_reports);
    }


    public void selectOrders(View v){
        Intent intent=new Intent(Select_orders_reports.this,AddOreders.class);
        startActivity(intent);
    }

    public void selectReports(View v){
        Intent intent=new Intent(Select_orders_reports.this,AddReports.class);
        startActivity(intent);
    }


}
