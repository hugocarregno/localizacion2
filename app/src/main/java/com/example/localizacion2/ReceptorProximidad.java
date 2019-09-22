package com.example.localizacion2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

public class ReceptorProximidad extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "se aproxima", Toast.LENGTH_LONG).show();
        //String key = LocationManager.KEY_PROXIMITY_ENTERING;
        //Boolean extra = intent.getBooleanExtra(key, false);
    }
}
