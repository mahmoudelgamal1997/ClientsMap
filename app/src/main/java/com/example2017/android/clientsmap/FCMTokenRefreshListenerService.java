package com.example2017.android.clientsmap;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by M7moud on 06-Sep-18.
 */
public class FCMTokenRefreshListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {

        Intent intent = new Intent(this, FCMRegisterationService.class);
        intent.putExtra("refreshed", true);
        startService(intent);
    }
}

