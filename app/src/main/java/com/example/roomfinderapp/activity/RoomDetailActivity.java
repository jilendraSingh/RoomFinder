package com.example.roomfinderapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.roomfinderapp.R;

public class RoomDetailActivity extends AppCompatActivity {


    TextView rd_Budget, rd_RoomType, rd_City, rd_Address, rd_FloorNo, rd_MobileNo, rd_facility;
    LinearLayout facilityLayout;
    Button find_Room_sendMessageButton;
    String budget, roomtype, city, roomAddress, floorNo, mobileNo, facility;
    ImageView imageView_roomDetail;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);


        Bundle bundle = getIntent().getExtras();
        try {

            assert bundle != null;
            budget = bundle.getString("budget");
            city = bundle.getString("city");
            mobileNo = bundle.getString("phone");
            floorNo = bundle.getString("floor");
            roomtype = bundle.getString("roomtype");
            facility = bundle.getString("facility");
            roomAddress = bundle.getString("address");

            byte[] img_byteArray = (byte[]) bundle.get("roomimage");
            try {
                assert img_byteArray != null;
                bitmap = BitmapFactory.decodeByteArray(img_byteArray, 0, img_byteArray.length);

            } catch (NullPointerException e) {
                e.printStackTrace();
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        rd_Budget = findViewById(R.id.rd_Budget);
        rd_RoomType = findViewById(R.id.rd_RoomType);
        rd_City = findViewById(R.id.rd_City);
        rd_Address = findViewById(R.id.rd_Address);
        rd_FloorNo = findViewById(R.id.rd_FloorNo);
        rd_MobileNo = findViewById(R.id.rd_MobileNo);
        rd_facility = findViewById(R.id.rd_facility);
        facilityLayout = findViewById(R.id.facilityLayout);
        find_Room_sendMessageButton = findViewById(R.id.find_Room_sendMessageButton);
        imageView_roomDetail = findViewById(R.id.imageView_roomDetail);
        imageView_roomDetail.setImageBitmap(bitmap);


    }


    @Override
    protected void onResume() {
        super.onResume();

        rd_Budget.setText(budget);
        rd_RoomType.setText(roomtype);
        rd_City.setText(city);
        rd_Address.setText(roomAddress);
        rd_MobileNo.setText(mobileNo);
        rd_facility.setText(facility);
        if (facility.equals("")) {
            facilityLayout.setVisibility(View.GONE);
        }

        find_Room_sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomDetailActivity.this, MessageActivity.class);
                intent.putExtra("phone", mobileNo);
                startActivity(intent);
            }
        });
        switch (floorNo) {
            case "0":
                floorNo = "Ground Floor";
                rd_FloorNo.setText(floorNo);
                break;
            case "1":
                floorNo = floorNo + "st" + " Floor";
                rd_FloorNo.setText(floorNo);
                break;
            case "2":
                floorNo = floorNo + "nd" + " Floor";
                rd_FloorNo.setText(floorNo);
                break;
            case "3":
                floorNo = floorNo + "rd" + " Floor";
                rd_FloorNo.setText(floorNo);
                break;
            default:
                floorNo = floorNo + "th" + " Floor";
                rd_FloorNo.setText(floorNo);
                break;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}