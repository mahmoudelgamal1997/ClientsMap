package com.example2017.android.clientsmap;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TimeMangement extends AppCompatActivity {

    ListView listView;
    DatabaseReference TimeTable;
    SharedPreferences sh;
    boolean am=false;
    Map<String,String> map=new HashMap<>() ;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_mangement);
        listView=(ListView)findViewById(R.id.listview);
        TimeTable=FirebaseDatabase.getInstance().getReference().child("TimeTable");



        sh=getSharedPreferences("plz",Context.MODE_PRIVATE );



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


    private void addNotification() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                          .setSmallIcon(R.mipmap.add)
                          .setContentTitle("Notifications Example")
                          .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }



    public void alarm() {

        TimeTable.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                TimeItem timeItem = dataSnapshot.getValue(TimeItem.class);
                String Date = timeItem.getDate();

                String[] parts = Date.split("-|\\  |\\:|\\ ");
                String year = parts[0];
                String month = parts[1];
                String day = parts[2];
                String hour = parts[3];
                String minute = parts[4];
                String k=dataSnapshot.getKey().toString();
                map.put(k,hour);
                SaveMap(map);
                System.out.println(map);
                Calendar calendar = Calendar.getInstance();
                int nowhour = calendar.get(Calendar.HOUR_OF_DAY);
                int nowminute = calendar.get(Calendar.MINUTE);

                if (nowhour - Integer.parseInt(hour) == 1 || (nowhour - Integer.parseInt(hour) + 12 == 1)) {
                    Toast.makeText(TimeMangement.this, " Time up ", Toast.LENGTH_SHORT).show();
                    addNotification();
                }


                if (Date.contains("صباحا")) {
                    am = true;
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



    public void SaveMap(Map<String,String> map){

    if (sh!=null)
    {
        JSONObject jsonObject=new JSONObject(map);
        String data=jsonObject.toString();
        SharedPreferences.Editor editor=sh.edit();
        editor.remove("My_map").commit();
        editor.putString("My_map",data);

    }






}

    public Map<String,String> loadMap(){
        Map<String,String> outputMap = new HashMap<String,String>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("plz", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }




}
