package com.example2017.android.clientsmap;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TimeMangement extends AppCompatActivity {

    ListView listView;
    DatabaseReference TimeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_mangement);
        listView=(ListView)findViewById(R.id.listview);
        TimeTable=FirebaseDatabase.getInstance().getReference().child("TimeTable");



        alarm();

        final FirebaseListAdapter<TimeItem> firebaseListAdapter = new FirebaseListAdapter<TimeItem>(
                this,
                TimeItem.class,
                R.layout.listview_items,
                TimeTable
        ) {
            @Override
            protected void populateView(View v, TimeItem model, final int position) {

                TextView place = (TextView) v.findViewById(R.id.textView_plcae);
                place.setText(model.getPlace());

                TextView Time = (TextView) v.findViewById(R.id.textView_Time);
                Time.setText(model.getDate());

                TextView Owner = (TextView) v.findViewById(R.id.textView_timeOwner);
                Owner.setText(model.getTimeOwner());




                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        AlertDialog.Builder builder=new AlertDialog.Builder(TimeMangement.this);
                        builder.setTitle("هل تريد مسح هذا الميعاد بالفعل");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {

                                getRef(position).removeValue();

                            }
                        });
                        AlertDialog alert=builder.create();
                        alert.show();

                    }
                });

            }

        };

        listView.setAdapter(firebaseListAdapter);


    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.select_time,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.select:
               Intent i=new Intent(TimeMangement.this,Date_Time.class);
                startActivity(i);

        }


        return super.onOptionsItemSelected(item);
    }


    public void alarm(){

        TimeTable.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                TimeItem timeItem=dataSnapshot.getValue(TimeItem.class);
                String Date=timeItem.getDate();

                String[]parts = Date.split("-|\\  |\\:|\\ ");
                String year=parts[0];
                String month=parts[1];
                String day=parts[4];

                if(Date.contains("صباحا")){
                    Toast.makeText(TimeMangement.this, "okkkk", Toast.LENGTH_SHORT).show();

                }





                //  String hour=parts[3];


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



}
