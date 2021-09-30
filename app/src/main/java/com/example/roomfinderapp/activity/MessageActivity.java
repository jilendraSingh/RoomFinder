package com.example.roomfinderapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.roomfinderapp.R;

public class MessageActivity extends AppCompatActivity {

    Button button_SendMessage;
    EditText edt_Input;
    String phNumber = "";
    String messageValue = "";
    TextView tv_showNumber;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    BroadcastReceiver sendSMS, deliverSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkRuntimePermission();
        }

        Bundle bundle = getIntent().getExtras();
        try {
            phNumber = bundle.getString("phone");
            System.out.println(phNumber);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        tv_showNumber = findViewById(R.id.tv_showNumber);
        tv_showNumber.setText("+91- " + phNumber);
        button_SendMessage = findViewById(R.id.button_SendMessage);
        edt_Input = findViewById(R.id.edt_inputMessage);
        button_SendMessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                System.out.println("Floating Button Clicked");
                messageValue = edt_Input.getText().toString().trim();
                System.out.println(phNumber + " " + messageValue);

                if (!messageValue.equals("")) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 101);
                    } else {
                        sendMessage(phNumber, messageValue);
                    }
                } else {
                    Toast.makeText(MessageActivity.this, "Please Type Message.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkRuntimePermission() {

        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 101);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Permission granted");
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 101);
            }
        }
    }


    public void sendMessage(String phNumber, String messageValue) {
        // Intent Filter Tags for SMS SEND and DELIVER
      /*  String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";*/
// STEP-1___
        // SEND PendingIntent
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        // DELIVER PendingIntent
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
// STEP-2___
        // SEND BroadcastReceiver
        sendSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 100);

                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        // DELIVERY BroadcastReceiver
        deliverSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
// STEP-3___
        // ---Notify when the SMS has been sent---


        // ---Notify when the SMS has been delivered---

        registerReceiver(sendSMS, new IntentFilter(SENT));
        registerReceiver(deliverSMS, new IntentFilter(DELIVERED));

    /*    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", "12125551212");
        smsIntent.putExtra("sms_body","Body of Message");
        startActivity(smsIntent);*/
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phNumber, null, messageValue, sentPI, deliveredPI);
    }

/*    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(sendSMS, new IntentFilter(SENT));
        registerReceiver(deliverSMS, new IntentFilter(DELIVERED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(sendSMS);
        unregisterReceiver(deliverSMS);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}