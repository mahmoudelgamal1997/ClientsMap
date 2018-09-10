package com.example2017.android.clientsmap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by M7moud on 03-Sep-18.
 */
public class Alarm extends Service {
Map<String,String> map=new HashMap<>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            TimeMangement timeMangement=new TimeMangement();
         map=   timeMangement.loadMap();
            for(String key:map.keySet()){

                Toast.makeText(Alarm.this, ""+ map.get(key), Toast.LENGTH_SHORT).show();
            }


        return flags;
    }

}
