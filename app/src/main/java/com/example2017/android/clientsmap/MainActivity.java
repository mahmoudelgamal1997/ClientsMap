package com.example2017.android.clientsmap;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    LocationRequest mLocationRequest;
    int preventNotification=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        createLocationRequest();
           alarmOrder();
           alarmReports();


         }


    public void client(View v){

        Intent intent=new Intent(MainActivity.this,ClientLogin.class);
        startActivity(intent);
    }

    public void admin(View v){
        FirebaseUser isUser=FirebaseAuth.getInstance().getCurrentUser();
        if (isUser!=null ){

            if(isUser.getEmail().equals("info@multimedica.com") || isUser.getEmail().equals("amr@multimedica.com") || isUser.getEmail().equals("mohamed@multimedica.com")|| isUser.getEmail().equals("mahmoud@multimedica.com")) {
                Intent intent = new Intent(MainActivity.this, Admin.class);
                startActivity(intent);
            }else {
                Toast.makeText(MainActivity.this, "ليس لديك الصلاحيه للدخول لهذه الصفحه ", Toast.LENGTH_SHORT).show();
            }
            }else {
            Toast.makeText(MainActivity.this, "قم بتسجيل الدخول ", Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View v){
        Intent intent=new Intent(MainActivity.this,SignUp.class);
        startActivity(intent);
    }


    public void addDevice(View v){
        Intent intent=new Intent(MainActivity.this,AddDevice.class);
        startActivity(intent);
    }


    public void logout(View v){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("هل حقا تريد تسجيل الخروح");
        builder.setTitle("تنبيه");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Toast.makeText(MainActivity.this, "لقد تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

       AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }


    public void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to accses location ", Toast.LENGTH_SHORT).show();
                    finish();
                    System.exit(0);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made


                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        System.exit(0);
                        finish();
                        break;
                    default:
                        break;
                }
                break;
        }


    }


    public void locationSetting(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);


    }


    protected void createLocationRequest() {

            locationSetting();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setNeedBle(true);
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        MainActivity.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });


    }








    public void alarmOrder() {

        final DatabaseReference reports = FirebaseDatabase.getInstance().getReference().child("Orders");

        final FirebaseUser isUser = FirebaseAuth.getInstance().getCurrentUser();


        if (isUser != null) {
        final String email =isUser.getEmail();
            reports.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    for (DataSnapshot key : dataSnapshot.getChildren()) {
                        ReportItem reportItem = key.getValue(ReportItem.class);
                        final String name = reportItem.getClientName();
                        reports.child(name).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                                //to send Notification to Admins Only

                                if(email.equals("info@multimedica.com") || email.equals("amr@multimedica.com") || email.equals("mohamed@multimedica.com")|| email.equals("mahmoud@multimedica.com")) {
                                    sendNotificationOrders(name, "you have a orders ");

                                }
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
    public void alarmReports(){

        final DatabaseReference reports = FirebaseDatabase.getInstance().getReference().child("Reports");
        final FirebaseUser isUser = FirebaseAuth.getInstance().getCurrentUser();

        if (isUser != null) {
            final String email =isUser.getEmail();
            reports.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                    for (DataSnapshot key : dataSnapshot.getChildren()) {
                        final ReportItem reportItem = key.getValue(ReportItem.class);
                        final String name = reportItem.getClientName();
                        reports.child(name).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                ReportItem reportItem1 = dataSnapshot.getValue(ReportItem.class);
                                String Drname = reportItem1.getDrName();
                                String Adress = reportItem1.getAdress();
                                String Reports = reportItem1.getReports();
                                String Mobile = reportItem1.getMobile();
                                String Speciality = reportItem1.getSpecialisty();
                                String OldUnit = reportItem1.getOldUnit();
                                String Time = reportItem1.getTime();

                                //to send Notification to Admins Only

                                if(email.equals("info@multimedica.com") || email.equals("amr@multimedica.com") || email.equals("mohamed@multimedica.com") || email.equals("mahmoud@multimedica.com")) {

                                 //   sendNotificationReports(name, "you have a report ");
                                    WriteExcelFile(name, Time, Drname, Adress, Mobile, Speciality, OldUnit, Reports);

                                }
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

    private void sendNotificationOrders(String title, String messageBody) {
        // to get NameSender and use it to arrive to his page of orders
        SharedPreferences sh=getSharedPreferences("keyOrders",MODE_PRIVATE);
        SharedPreferences.Editor editor=sh.edit();
        editor.putString("orders",title);
        editor.commit();

        Random r=new Random();
        int id=r.nextInt(5);
        Intent intent = new Intent(getApplicationContext(), Orders.class);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id /* Request code */, intent,
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

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }



    private void sendNotificationReports(String title, String messageBody) {

        // to get NameSender and use it to arrive to his page of Reports


        Random r=new Random();
        int id=r.nextInt(5);

        SharedPreferences sharedPreferences = getSharedPreferences("keyReports",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("reports",title);
        editor.commit();


        Intent intent = new Intent(getApplicationContext(), Reports.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id /* Request code */, intent,
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

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }



    public void WriteExcelFile(String ClientName,String TimeRecieved,String DrName ,String Adress ,String Phone ,String Speciality,String OldUnit,String Comment){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Sheet No  1");
//        HSSFSheet secondSheet = workbook.createSheet("Sheet No  2");
        HSSFRow rowA = firstSheet.createRow(0);


        HSSFCell cellA = rowA.createCell(0);
        HSSFCell cellB = rowA.createCell(1);
        HSSFCell cellC = rowA.createCell(2);
        HSSFCell cellD = rowA.createCell(3);
        HSSFCell cellE = rowA.createCell(4);
        HSSFCell cellF = rowA.createCell(5);
        HSSFCell cellG = rowA.createCell(6);

        cellA.setCellValue(new HSSFRichTextString("Dr NAME"));
        cellB.setCellValue(new HSSFRichTextString("adress"));
        cellC.setCellValue(new HSSFRichTextString("Phone"));
        cellE.setCellValue(new HSSFRichTextString("Speciality"));
        cellF.setCellValue(new HSSFRichTextString("OLD Unit"));
        cellG.setCellValue(new HSSFRichTextString("Comment"));


        HSSFRow rowB = firstSheet.createRow(1);

        HSSFCell cellA2 = rowB.createCell(0);
        HSSFCell cellB2 = rowB.createCell(1);
        HSSFCell cellC2 = rowB.createCell(2);
        HSSFCell cellD2 = rowB.createCell(3);
        HSSFCell cellE2 = rowB.createCell(4);
        HSSFCell cellF2 = rowB.createCell(5);
        HSSFCell cellG2 = rowB.createCell(6);

        cellA2.setCellValue(new HSSFRichTextString(DrName));
        cellB2.setCellValue(new HSSFRichTextString(Adress));
        cellC2.setCellValue(new HSSFRichTextString(Phone));
        cellE2.setCellValue(new HSSFRichTextString(Speciality));
        cellF2.setCellValue(new HSSFRichTextString(OldUnit));
        cellG2.setCellValue(new HSSFRichTextString(Comment));






        FileOutputStream fos = null;
        try {
            String str_path = Environment.getExternalStorageDirectory()+File.separator+"Multi Medical"+File.separator+ClientName;
            File directory=new File(str_path);
            if ( ! directory.exists() ){
                directory.mkdirs();
            }
            File file ;
            file = new File(str_path,TimeRecieved + ".xls");

            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(MainActivity.this, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
        }
    }



}
