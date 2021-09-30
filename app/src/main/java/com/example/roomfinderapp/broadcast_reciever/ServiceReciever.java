package com.example.roomfinderapp.broadcast_reciever;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.roomfinderapp.service_classes.LocationServie;

import static com.example.roomfinderapp.Constants.BROADCAST;
import static com.example.roomfinderapp.activity.BecomeRoomMateActivity.TAG;

public class ServiceReciever extends BroadcastReceiver {

    public ServiceReciever() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ShortAlarm")
    @Override
    public void onReceive(Context context, Intent intent1) {
        if (intent1.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            Log.e(TAG, "onReceive: action BOOT COMPLETED ");
            Intent intent = new Intent(context, LocationServie.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, intent);
            } else {
                context.startService(intent);
            }

        } else if (intent1.getAction().equals(BROADCAST)) {
            Log.e(TAG, "onReceive: ACTION BROADCAST ");
            Intent intent = new Intent(context, LocationServie.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, intent);
            } else {
                context.startService(intent);
            }
        } else {
            Log.e(TAG, "onReceive: " + intent1.getAction());
            Intent intent = new Intent(context, LocationServie.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, intent);
            } else {
                context.startService(intent);
            }
        }

        //System.out.println("Format dateTime => " + formattedDate);

    }
}
