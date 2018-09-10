package com.example2017.android.clientsmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Admin extends AppCompatActivity {

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


    public void reports (View v)
    {
        Intent intent=new Intent(Admin.this,Reports.class);
        startActivity(intent);
    }





}
