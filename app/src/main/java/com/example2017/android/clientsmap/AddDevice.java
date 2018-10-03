package com.example2017.android.clientsmap;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Map;

public class AddDevice extends AppCompatActivity {

    ListView listView;
    PopupWindow mpopup;
    String devicename,devicecompany,devicenotice,deviceadress,devicetype,deviceserial,devicedepartment;
    DatabaseReference maintain,users,temp;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        firebaseAuth=FirebaseAuth.getInstance();
        maintain= FirebaseDatabase.getInstance().getReference().child("maintain");
        maintain.keepSynced(true);

        users= FirebaseDatabase.getInstance().getReference().child("username");



       firebaseAuthListener =new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

               if (firebaseAuth!=null) {
                   final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                   if (user == null) {
                       Toast.makeText(AddDevice.this, "عليك ان تسجل الدخول اولا ", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(AddDevice.this, ClientLogin.class);
                       startActivity(intent);
                   }}}};

                recyclerView = (RecyclerView) findViewById(R.id.recycle_device);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


              display();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.select_time,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.select:


                temp = maintain.push();
                //pop up window
                View view = getLayoutInflater().inflate(R.layout.device_popup, null);
                mpopup = new PopupWindow(view, ActionBar.LayoutParams.FILL_PARENT,
                        ActionBar.LayoutParams.WRAP_CONTENT, true); // Creation of popup
                mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                mpopup.showAtLocation(view, Gravity.CENTER, 0, 0); // Displaying popup
                mpopup.setIgnoreCheekPress();

                final EditText mDeviceName    = (EditText) view.findViewById(R.id.device_name);
                final EditText mDeviceCompany = (EditText) view.findViewById(R.id.device_company);
                final EditText mDeviceNotice  = (EditText) view.findViewById(R.id.device_notice);
                final EditText mDeviceAdress  = (EditText) view.findViewById(R.id.device_adress);
                final EditText mDeviceType    = (EditText) view.findViewById(R.id.device_type);
                final EditText mDeviceSerial  = (EditText) view.findViewById(R.id.device_serial);
                final EditText mDeviceDepartment = (EditText) view.findViewById(R.id.device_department);

                Button button = (Button) view.findViewById(R.id.upload);


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String user=firebaseAuth.getCurrentUser().getUid();
                        users.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                                };
                                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);

                                devicename = mDeviceName.getText().toString().trim();
                                devicecompany = mDeviceCompany.getText().toString().trim();
                                deviceadress = mDeviceAdress.getText().toString();
                                devicetype = mDeviceType.getText().toString();
                                devicedepartment = mDeviceDepartment.getText().toString();
                                deviceserial = mDeviceSerial.getText().toString();
                                devicenotice = mDeviceNotice.getText().toString();




                                String nameOfuser = map.get(user);
                                temp.child("Name").setValue(nameOfuser);
                                temp.child("DeviceName").setValue(devicename);
                                temp.child("DeviceCompany").setValue(devicecompany);
                                temp.child("DeviceAdress").setValue(deviceadress);
                                temp.child("DeviceType").setValue(devicetype);
                                temp.child("DeviceSerial").setValue(deviceserial);
                                temp.child("DeviceDepartment").setValue(devicedepartment);
                                temp.child("DeviceNotice").setValue(devicenotice);

                                temp.child("TimeRecieved").setValue(Time());
                                temp.child("TimeFinish").setValue("تحت الطلب");
                                temp.child("ID").setValue(user);
                                Toast.makeText(AddDevice.this, "تم تسجيل الجهاز بنجاح", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });


        }







        return super.onOptionsItemSelected(item);
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





    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);


    }



    public void display() {


        FirebaseRecyclerAdapter<ReportItem, Post_viewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReportItem,Post_viewholder>(
                ReportItem.class,
                R.layout.device_cardview,
                Post_viewholder.class,
                maintain


        ) {
            @Override
            protected void populateViewHolder(final Post_viewholder viewHolder, final ReportItem model, final int position) {

                viewHolder.SetData(model.getName(),model.getTimeRecieved(),model.getTimeFinish(),model.getDeviceCompany(),model.getDeviceAdress(),model.getDeviceName(),model.getDeviceDepartment(),model.getDeviceType(),model.getDeviceSerial(), model.getDeviceNotice());
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(AddDevice.this);
                        builder.setMessage("هل تم الانتهاء من اصلاح الجهاز بالفعل");
                        builder.setPositiveButton("نعم ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String key = getRef(position).getKey();

                                maintain.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                                        };
                                        Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);


                                        String ID = map.get("ID");
                                        String currentId=firebaseAuth.getCurrentUser().getUid();
                                        if (currentId.equals(ID) )
                                        {
                                            String key = getRef(position).getKey();
                                            maintain.child(key).child("TimeFinish").setValue(Time());
                                        }else {
                                            Toast.makeText(AddDevice.this, "لا تملك السماحيه لاستلام هذا الجهاز ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        });

                    AlertDialog alert=builder.create();
                        alert.show();

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
        public void SetData(String Username,String TimeSent,String TimeFinish,String CompanyName,String CompanyAdress,String DeviceName,String DeviceDepartment,String DeviceType,String DeviceSerial,String Report) {


            TextView username=(TextView)view.findViewById(R.id.textview_username);
            TextView timeSent=(TextView)view.findViewById(R.id.textview_timeSent);
            TextView companyname=(TextView)view.findViewById(R.id.textview_companyName);
            TextView companyAdress=(TextView)view.findViewById(R.id.textview_companyAdress);
            TextView devicename=(TextView)view.findViewById(R.id.textview_DeviceName);
            TextView deviceDepartment=(TextView)view.findViewById(R.id.textview_DeviceDepartment);
            TextView deviceType=(TextView)view.findViewById(R.id.textview_DeviceType);
            TextView deviceSerial=(TextView)view.findViewById(R.id.textview_DeviceSerial);
            TextView deviceNotify=(TextView)view.findViewById(R.id.textview_DeviceNotify);

            TextView timeRecieved=(TextView)view.findViewById(R.id.textview_timeRecieved);

            username.setText(Username);
            timeSent.setText(TimeSent);
            companyname.setText(CompanyName);
            companyAdress.setText(CompanyAdress);
            deviceDepartment.setText(DeviceDepartment);
            deviceSerial.setText(DeviceSerial);
            deviceType.setText(DeviceType);
            deviceNotify.setText(Report);
            timeRecieved.setText(TimeFinish);
            devicename.setText(DeviceName);
        }

    }



    }

