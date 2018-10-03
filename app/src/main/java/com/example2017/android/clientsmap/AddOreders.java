package com.example2017.android.clientsmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddOreders extends AppCompatActivity {

    DatabaseReference report,temp,Users;
    EditText editText_name,editText_report;
    Button but_upload;
    SharedPreferences sh;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_oreders);


        firebaseAuth=FirebaseAuth.getInstance();


        report= FirebaseDatabase.getInstance().getReference().child("Orders");
        Users= FirebaseDatabase.getInstance().getReference().child("username");
        editText_name=(EditText)findViewById(R.id.editText_drName);
        editText_report=(EditText)findViewById(R.id.editText_report);
        but_upload=(Button)findViewById(R.id.button_ok);



        sh=getSharedPreferences("plz", Context.MODE_PRIVATE );
        final String latitude=sh.getString("latitude","31,00");
        final String logitude=sh.getString("longitude","30,00");





        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day =calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        final String CollectionDate=""+year+"-"+month+"-"+day+"  "+hour+":"+minute;
        but_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String user=firebaseAuth.getCurrentUser().getUid();


                Users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);


                        String name=map.get(user);
                        temp=report.child(name).push();
                        temp.child("ClientName").setValue(name);
                        temp.child("DrName").setValue(editText_name.getText().toString().trim());
                        temp.child("Orders").setValue(editText_report.getText().toString().trim());
                        temp.child("Time").setValue(CollectionDate);
                        temp.child("TimeRecieved").setValue("تحت الطلب");
                        temp.child("latitude").setValue(latitude);
                        temp.child("longitude").setValue(logitude);
                        Toast.makeText(AddOreders.this, "تم التسجيل التقرير بنجاح", Toast.LENGTH_SHORT).show();




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });









            }
        });




    }







}
