package com.example.roomfinderapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.example.roomfinderapp.Constants;
import com.example.roomfinderapp.R;
import com.example.roomfinderapp.Utility;
import com.example.roomfinderapp.roomdatabase_folder.DatabaseClient;
import com.example.roomfinderapp.roomdatabase_folder.SignUpModel;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    EditText p_name, p_contact, p_emailid, p_password, p_confirm_password;
    Button submitButton, purpose_spinnerButton;

    String purposeValue = "";
    List<SignUpModel> modelList;
    String email;
    SignUpModel signUpModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    private void init() {
        p_name = findViewById(R.id.p_name);
        p_contact = findViewById(R.id.p_contact);
        p_emailid = findViewById(R.id.p_emailid);
        p_password = findViewById(R.id.p_password);
        p_confirm_password = findViewById(R.id.p_confirm_password);
        purpose_spinnerButton = findViewById(R.id.purpose_spinnerButton);
        submitButton = findViewById(R.id.signup_submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDetail();
            }
        });
        final Context wrapper = new ContextThemeWrapper(this, R.style.YOURSTYLE);

        purpose_spinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(wrapper, purpose_spinnerButton);
                popupMenu.getMenuInflater().inflate(R.menu.purpose_pop_up, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        purpose_spinnerButton.setText(item.getTitle());
                        purposeValue = item.getTitle().toString();
                        return true;
                    }
                });
                popupMenu.show();

            }
        });
    }


    private void submitDetail() {

        String name = p_name.getText().toString().trim();
        String contact = p_contact.getText().toString().trim();
        email = p_emailid.getText().toString().trim();
        String password = p_password.getText().toString().trim();
        String confirm_password = p_confirm_password.getText().toString().trim();
        if (name.equals("")) {
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        } else if (contact.equals("")) {
            Toast.makeText(this, "Please Enter Contact Number", Toast.LENGTH_SHORT).show();
        } else if (contact.length() <= 9) {
            Toast.makeText(this, "Contact Number should be 10 digit", Toast.LENGTH_SHORT).show();
        } else if (email.equals("")) {
            Toast.makeText(this, "Please Enter Email Id", Toast.LENGTH_SHORT).show();
        } else if (!Utility.emailValidator(email)) {
            Toast.makeText(this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (password.length() <= 5) {
            Toast.makeText(this, "Password is too short!", Toast.LENGTH_SHORT).show();
        } else if (confirm_password.equals("")) {
            Toast.makeText(this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirm_password)) {
            Toast.makeText(this, "Password and Confirm password should be same", Toast.LENGTH_SHORT).show();
        } else if (purposeValue.equals("")) {
            Toast.makeText(this, "Please Select Purpose ", Toast.LENGTH_SHORT).show();
        } else {

            modelList = new ArrayList<>();
            Log.e(Constants.TAG, "submitDetail: " + name + " " + contact + " " + email + " " + password + " " + confirm_password + " " + purposeValue);


            signUpModel = new SignUpModel();
            signUpModel.setName(name);
            signUpModel.setPhoneNumber(contact);
            signUpModel.setEmailId(email);
            signUpModel.setPassword(password);
            signUpModel.setConfirm_password(confirm_password);
            signUpModel.setPurpose(purposeValue);

            new MyTask().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class MyTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int count = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().checkEntry(email);
            Log.e("", "doInBackground:  " + count);
            return count;
        }

        @Override
        protected void onPostExecute(Integer count) {
            if (count >= 1) {
                Toast.makeText(SignUpActivity.this, "EmailId Already Exist!", Toast.LENGTH_SHORT).show();
            } else {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().insertSignUpDetail(signUpModel);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignUpActivity.this, "Record Inserted Succesfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        }
    }
}