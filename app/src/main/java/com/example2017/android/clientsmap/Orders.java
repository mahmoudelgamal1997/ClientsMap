package com.example2017.android.clientsmap;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Orders extends AppCompatActivity {

    DatabaseReference reports;
    RecyclerView recyclerView;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Firebase.setAndroidContext(this);





        Intent myIntent = getIntent();
        String name=myIntent.getStringExtra("name");

        if (name ==null){
           sh=getSharedPreferences("keyOrders",MODE_PRIVATE);
          name= sh.getString("orders","amr shalaby");
        }
        reports = FirebaseDatabase.getInstance().getReference().child("Orders").child(name);
        reports.keepSynced(true);

        recyclerView = (RecyclerView) findViewById(R.id.view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();


        display();

    }







    public void display(){


        FirebaseRecyclerAdapter<ReportItem,Post_viewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ReportItem, Post_viewholder>(
                ReportItem.class,
                R.layout.orders_cardview,
                Post_viewholder.class,
                reports


        ) {
            @Override
            protected void populateViewHolder(final Post_viewholder viewHolder, final ReportItem model, final int position) {

                viewHolder.SetData(model.getClientName(),model.getTime(),model.getTimeRecieved(),model.getDrName(),model.getOrders());
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String key=getRef(position).getKey();
                        reports.child(key).child("TimeRecieved").setValue(Time());


                    }
                });


                viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Orders.this);
                        builder.setTitle("هل تريد مسح هذا الطلب بالفعل ");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                getRef(position).removeValue();

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();



                        return false;
                    }
                });







            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);




}

    public  static  class Post_viewholder extends RecyclerView.ViewHolder {

        View view;

        public Post_viewholder(View itemView) {
            super(itemView);
            view = itemView;
        }
        public void SetData(String Username,String Time,String TimeRecieved,String Drname,String Report) {


            TextView username=(TextView)view.findViewById(R.id.textview_username);
            TextView timeSent=(TextView)view.findViewById(R.id.textview_timeSent);
            TextView DrName=(TextView)view.findViewById(R.id.textview_drName);
            TextView report=(TextView)view.findViewById(R.id.textview_orders);
            TextView timeRecieved=(TextView)view.findViewById(R.id.textview_timeRecieved);

            username.setText(Username);
            timeSent.setText(Time);
            DrName.setText(Drname);
            report.setText(Report);
            timeRecieved.setText(TimeRecieved);
        }

    }


    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public String Time(){

        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day =calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        final String CollectionDate=""+year+"-"+month+"-"+day+"  "+hour+":"+minute;

        return CollectionDate;
    }



}
