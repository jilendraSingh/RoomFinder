package com.example.roomfinderapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomfinderapp.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void setToolBar(Activity activity, String title) {
        ImageView backImageView = activity.findViewById(R.id.iv_back_arrow);
        TextView tvTitle = activity.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}