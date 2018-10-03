package com.example2017.android.clientsmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Admin extends AppCompatActivity {

    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);




    }

    public void show (View v)
    {
        Intent intent=new Intent(Admin.this,AdminMap.class);
        startActivity(intent);
    }

    public void time (View v)
    {
        Intent intent=new Intent(Admin.this,TimeMangement.class);
        startActivity(intent);
    }


    public void orders (View v)
    {



        Intent intent=new Intent(Admin.this,OrderPerson.class);
        intent.putExtra("key","orders");
        startActivity(intent);
    }

    public void reports (View v)
    {


        Intent intent=new Intent(Admin.this,OrderPerson.class);
        intent.putExtra("key","reports");
        startActivity(intent);
    }




}
