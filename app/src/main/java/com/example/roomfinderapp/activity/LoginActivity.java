package com.example.roomfinderapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomfinderapp.R;
import com.example.roomfinderapp.roomdatabase_folder.DatabaseClient;
import com.example.roomfinderapp.roomdatabase_folder.SignUpModel;

import static com.example.roomfinderapp.Constants.KEY_EMAIL;
import static com.example.roomfinderapp.Constants.KEY_PASSWORD;
import static com.example.roomfinderapp.Constants.KEY_PURPOSE;
import static com.example.roomfinderapp.Constants.MyPREFERENCES;

public class LoginActivity extends AppCompatActivity {

    EditText edt_Login, edt_password;
    TextView tv_createAccount;
    Button login_Button;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        init();

    }

    private void init() {
        edt_Login = findViewById(R.id.edt_Login);
        edt_password = findViewById(R.id.edt_password);
        tv_createAccount = findViewById(R.id.tv_createAccount);
        login_Button = findViewById(R.id.login_Button);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String email = (sharedPreferences.getString(KEY_EMAIL, ""));
        String password = (sharedPreferences.getString(KEY_PASSWORD, ""));

        if ((email.equals("") || password.equals(""))) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }



       /* Typeface font = Typeface.createFromAsset(LoginActivity.this.getAssets(), "RiseofKingdom.ttf");
        tv_createAccount.setTypeface(font);*/

        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkCredential();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        tv_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

    }

    private void checkCredential() throws InterruptedException {

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String email = (shared.getString(KEY_EMAIL, ""));
        String password = (shared.getString(KEY_PASSWORD, ""));

        if (!(email.equals("") || password.equals(""))) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        } else {
            final String user_email = edt_Login.getText().toString().trim();
            final String user_password = edt_password.getText().toString().trim();
            if (user_email.equals("")) {
                Toast.makeText(this, "Please Enter Email Id", Toast.LENGTH_SHORT).show();
            } else if (user_password.equals("")) {
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            } else {

                final String[] purposeofUSer = new String[1];

                Runnable purposeThread = new Runnable() {
                    @Override
                    public void run() {
                        purposeofUSer[0] = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().getPurpose(user_email);
                    }
                };

                Thread getPurposeValue = new Thread(purposeThread);
                getPurposeValue.start();
                getPurposeValue.join();

                Runnable authenticationThread = new Runnable() {
                    @Override
                    public void run() {
                        SignUpModel signUpModel = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().verifyUserDetail(user_email, user_password);

                        if (signUpModel == null) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Invalid Credential", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            editor = sharedPreferences.edit();
                            editor.putString(KEY_EMAIL, user_email);
                            editor.putString(KEY_PASSWORD, user_password);
                            editor.putString(KEY_PURPOSE, purposeofUSer[0]);
                            editor.apply();

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();

                        }
                    }
                };

                Thread checkLoginDetail = new Thread(authenticationThread);
                checkLoginDetail.start();

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}