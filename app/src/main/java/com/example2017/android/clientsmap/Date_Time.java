package com.example2017.android.clientsmap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.Calendar;

public class Date_Time extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

        DatabaseReference timetable,temp;
        Button but_picktime,but_push;
        String CollectionTime="";
        String TimeOwner="";
        EditText place;
        int year,month,day,yearfinal,monthfinal,dayfinal,hour,minute,hourfinal,minutefinal;
        CheckBox eng1,eng2,eng3;
        String peroid="صباحا";
        boolean e1,e2,e3=false;
        //eng1 = amr
        //eng2 = mohmaed
        //eng3 = secratry

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date__time);

        but_picktime=(Button)findViewById(R.id.DatePicker);
        but_push=(Button)findViewById(R.id.but_push);
        eng1=(CheckBox) findViewById(R.id.engAmr);
        eng2=(CheckBox) findViewById(R.id.engMoh);
        eng3=(CheckBox) findViewById(R.id.secratry);
        place=(EditText)findViewById(R.id.editText_place);
        timetable= FirebaseDatabase.getInstance().getReference().child("TimeTable");

         final AdminMap adminMap=new AdminMap();

         but_picktime.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Calendar c=Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH);
        day=c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker=new DatePickerDialog(Date_Time.this,Date_Time.this,
        year,
        month,
        day
        );
        datePicker.show();
        }
        });





            but_push.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            if (adminMap.checkInternetConnection(getApplicationContext())){
                                    temp=timetable.push();
                                    String p = place.getText().toString().trim();
                                    TimeOwner="";
                                    if (eng1.isChecked()){TimeOwner+="Eng Amr"+"\n";}
                                    if (eng2.isChecked()){TimeOwner+="Eng Mohammed"+"\n";}
                                    if (eng3.isChecked()){TimeOwner+="Secratry";}
                                    temp.child("TimeOwner").setValue(TimeOwner);
                                    temp.child("Date").setValue(CollectionTime);
                                    temp.child("Place").setValue( p );
                                    Toast.makeText(Date_Time.this, "تم تسجيل المعاد بنجاح", Toast.LENGTH_SHORT).show();

                            }else {

                                    Toast.makeText(Date_Time.this, "خطا في شبكه الانترنت ", Toast.LENGTH_SHORT).show();

                            }

                    }
            });






        }

@Override
public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        yearfinal=i;
        monthfinal=i1+1;
        dayfinal=i2;



        Calendar c=Calendar.getInstance();
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog=new TimePickerDialog(Date_Time.this, Date_Time.this,
        hour,
        minute,false

        );

        timePickerDialog.show();

        }

@Override
public void onTimeSet(TimePicker timePicker, int i, int i1) {

        hourfinal=i;
        minutefinal=i1;

        if (hourfinal>12){
                hourfinal-=12;
                peroid="مساءا" ;

        }

        CollectionTime=yearfinal+"-"+monthfinal+"-"+dayfinal+"  "+hourfinal+":"+minutefinal+" "+ peroid;




        }
        }