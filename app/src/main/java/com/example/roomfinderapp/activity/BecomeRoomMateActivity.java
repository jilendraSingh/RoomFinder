package com.example.roomfinderapp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.roomfinderapp.R;
import com.example.roomfinderapp.roomdatabase_folder.BecomeRoomMateModel;
import com.example.roomfinderapp.roomdatabase_folder.DatabaseClient;


public class BecomeRoomMateActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText edt_becomeName, edt_becomeAge, edt_becomeBudget, edt_becomeContact, edt_becomeAddress;
    RadioGroup become_radioGroup;
    Button become_submitButton;
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_room_mate);

        init();


    }

    private void init() {

        edt_becomeName = findViewById(R.id.edt_becomeName);
        edt_becomeAge = findViewById(R.id.edt_becomeAge);
        edt_becomeBudget = findViewById(R.id.edt_becomeBudget);
        edt_becomeContact = findViewById(R.id.edt_becomeContact);
        edt_becomeAddress = findViewById(R.id.edt_becomeAddress);
        become_radioGroup = findViewById(R.id.become_radioGroup);
        become_submitButton = findViewById(R.id.become_submitButton);

        become_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedid) {
                switch (checkedid) {
                    case R.id.become_radioMale:
                        gender = "Male";
                        break;
                    case R.id.become_radioFeMale:
                        gender = "Female";
                        break;
                }
            }
        });

        become_submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
                String name = edt_becomeName.getText().toString().trim();
                String age = edt_becomeAge.getText().toString().trim();
                String budget = edt_becomeBudget.getText().toString().trim();
                String contact = edt_becomeContact.getText().toString().trim();
                String address = edt_becomeAddress.getText().toString().trim();

                Log.e(TAG, "onClick: " + name + " " + age + " " + budget + " " + contact + " " + address + " " + gender);

                if (name.equals("")) {
                    Toast.makeText(BecomeRoomMateActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                } else if (age.equals("")) {
                    Toast.makeText(BecomeRoomMateActivity.this, "Please Enter Your Age", Toast.LENGTH_SHORT).show();
                } else if (budget.equals("")) {
                    Toast.makeText(BecomeRoomMateActivity.this, "Please Enter Your Budget", Toast.LENGTH_SHORT).show();
                } else if (contact.equals("")) {
                    Toast.makeText(BecomeRoomMateActivity.this, "Please Enter Contact Number", Toast.LENGTH_SHORT).show();
                } else if (contact.length() < 10) {
                    Toast.makeText(BecomeRoomMateActivity.this, "Invalid Contact Number", Toast.LENGTH_SHORT).show();
                } else if (address.equals("")) {
                    Toast.makeText(BecomeRoomMateActivity.this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
                } else if (gender.equals("")) {
                    Toast.makeText(BecomeRoomMateActivity.this, "Please Select Your gender", Toast.LENGTH_SHORT).show();
                } else {


                    final BecomeRoomMateModel object = new BecomeRoomMateModel();
                    object.setUserName(name);
                    object.setAge(Integer.parseInt(age));
                    object.setBudget(Integer.parseInt(budget));
                    object.setGender(gender);
                    object.setContact(contact);
                    object.setAddress(address);

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().insertAll(object);
                        }
                    });

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
