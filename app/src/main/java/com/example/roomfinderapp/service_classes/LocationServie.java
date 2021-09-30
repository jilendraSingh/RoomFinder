package com.example.roomfinderapp.service_classes;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.roomfinderapp.R;
import com.example.roomfinderapp.Utility;
import com.example.roomfinderapp.broadcast_reciever.ServiceReciever;
import com.example.roomfinderapp.roomdatabase_folder.DatabaseClient;
import com.example.roomfinderapp.roomdatabase_folder.LatLongModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;
import static com.example.roomfinderapp.Constants.BROADCAST;
import static com.example.roomfinderapp.Constants.KEY_EMAIL;
import static com.example.roomfinderapp.Constants.MyPREFERENCES;

public class LocationServie extends Service {
    private static final String TAG = "LocationServie";
    public static final int NOTIFICATION_ID = 1234;
    public static final String CHANNEL_ID = "FIND_LOST_DEVICE";
    ServiceReciever serviceReciever;
    IntentFilter intentFilter;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Log.e(TAG, "onCreate: ");
        serviceReciever = new ServiceReciever();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST);
        this.registerReceiver(serviceReciever, intentFilter);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: Starting Service");
        setNotification();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                getLateLong();
            }
        });
        return START_NOT_STICKY;

    }


    private void getLateLong() {


        LocationClass.LocationResult locationResult = new LocationClass.LocationResult() {
            @Override
            public void gotLocation(Location location) {

                if (location == null) {
                    Log.e(TAG, "gotLocation: is Null" );
                    return;
                }
                double userLat = location.getLatitude();
                double userLong = location.getLongitude();

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a", Locale.US);
                String formattedDate = simpleDateFormat.format(currentTime);
                //System.out.println("Format dateTime => " + formattedDate);

                String emailid = sharedPreferences.getString(KEY_EMAIL, "");
                Log.e(TAG, "gotLocation: " + emailid + " " + userLat + " " + userLong + " " + formattedDate + Utility.getDeviceName());

                final LatLongModel latLongModel = new LatLongModel();
                latLongModel.setEmailId(emailid);
                latLongModel.setDeviceLatitude(String.valueOf(userLat));
                latLongModel.setDeviceLongitude(String.valueOf(userLong));
                latLongModel.setDateandTime(formattedDate);
                latLongModel.setDeviceName(Utility.getDeviceName());
                final long[] id = new long[1];

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run: inserting data");
                        id[0] = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().insertLatLong(latLongModel);
                        if (id[0] != 0) {
                            Log.e(TAG, "record: ID  " + id[0]);
                            scheduleService();
                        }
                    }
                });


            }
        };
        LocationClass myLocation = new LocationClass();
        myLocation.getLocation(this, locationResult);
    }

    private void scheduleService() {
        Log.e(TAG, "scheduleService: " );
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getApplicationContext(), ServiceReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        assert manager != null;
        //manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        //stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: Called");
        this.unregisterReceiver(serviceReciever);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true); //true will remove notification
        }
    }

    public void setNotification() {
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert mNotifyManager != null;
            createChannel(mNotifyManager);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Notification notification = mBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createChannel(NotificationManager notificationManager) {
        String name = "FileDownload";
        String description = "Notifications for download status";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);
    }
}
