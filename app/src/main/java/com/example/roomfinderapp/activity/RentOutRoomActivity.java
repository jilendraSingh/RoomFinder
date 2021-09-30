package com.example.roomfinderapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import com.example.roomfinderapp.R;
import com.example.roomfinderapp.Utility;
import com.example.roomfinderapp.roomdatabase_folder.DatabaseClient;
import com.example.roomfinderapp.roomdatabase_folder.RentOutRoomModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.roomfinderapp.Constants.PICK_IMAGE;

public class RentOutRoomActivity extends BaseActivity {

    Button roomType_Button, rentOut_SubmitButton;
    EditText edt_CityName, edt_RoomAddress, edt_PinCode, edt_RoomBudget, edt_MobileNo, edt_FloorNo;
    CheckBox checkbox_TV, checkbox_WiFi, checkbox_Gyser, checkbox_CarParking, checkbox_BikeParking;
    String roomType = "";
    String[] addressString;
    TextView tv_uploadImage;
    ImageView select_image;
    Bitmap bitmap = null;
    List<RentOutRoomModel> roomListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_out_room);
        setToolBar(this, "Rent Out Your Room");
        init();

        addressString = new String[3];
        roomListSize = new ArrayList<>();
    }

    private void init() {
        edt_CityName = findViewById(R.id.edt_CityName);
        edt_RoomAddress = findViewById(R.id.edt_RoomAddress);
        edt_PinCode = findViewById(R.id.edt_PinCode);
        edt_RoomBudget = findViewById(R.id.edt_RoomBudget);
        edt_MobileNo = findViewById(R.id.edt_MobileNo);
        edt_FloorNo = findViewById(R.id.edt_FloorNo);

        checkbox_TV = findViewById(R.id.checkbox_TV);
        checkbox_WiFi = findViewById(R.id.checkbox_WiFi);
        checkbox_Gyser = findViewById(R.id.checkbox_Gyser);
        checkbox_CarParking = findViewById(R.id.checkbox_CarParking);
        checkbox_BikeParking = findViewById(R.id.checkbox_BikeParking);

        roomType_Button = findViewById(R.id.roomType_Button);
        rentOut_SubmitButton = findViewById(R.id.rentOut_SubmitButton);
        tv_uploadImage = findViewById(R.id.tv_uploadImage);
        select_image = findViewById(R.id.select_image);

        // TODO restrict to place more than two rooms
       /* AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                roomListSize = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().getAllRoomDetailList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (roomListSize.size() <= 2) {
                            Toast.makeText(RentOutRoomActivity.this, "You can not place more than 2 rooms on rent!", Toast.LENGTH_LONG).show();
                            rentOut_SubmitButton.setEnabled(false);
                        }
                    }
                });
            }
        });*/
        final Context wrapper = new ContextThemeWrapper(this, R.style.YOURSTYLE);

        roomType_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(wrapper, roomType_Button);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.pop_up_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        roomType_Button.setText(item.getTitle());
                        roomType = item.getTitle().toString();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOn

        rentOut_SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showAlertDialog();
                submitFinalDetail();

            }
        });
        tv_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_image.setVisibility(View.GONE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    private void submitFinalDetail() {
        final String cityName = edt_CityName.getText().toString().trim();
        final String roomAddress = edt_RoomAddress.getText().toString().trim();
        final String pinCode = edt_PinCode.getText().toString().trim();
        final String roomBudget = edt_RoomBudget.getText().toString().trim();
        final String mobileNo = edt_MobileNo.getText().toString().trim();
        final String floorValue = edt_FloorNo.getText().toString().trim();


        if (cityName.equals("")) {
            Toast.makeText(RentOutRoomActivity.this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
        } else if (roomAddress.equals("")) {
            Toast.makeText(RentOutRoomActivity.this, "Please Enter Room Address", Toast.LENGTH_SHORT).show();
        } else if (pinCode.equals("")) {
            Toast.makeText(RentOutRoomActivity.this, "Please Enter Your Pin Code", Toast.LENGTH_SHORT).show();
        } else if (roomBudget.equals("")) {
            Toast.makeText(RentOutRoomActivity.this, "Please Enter Your Budget", Toast.LENGTH_SHORT).show();
        } else if (mobileNo.equals("")) {
            Toast.makeText(RentOutRoomActivity.this, "Please Enter Your Mobile No.", Toast.LENGTH_SHORT).show();
        } else if (floorValue.equals("")) {
            Toast.makeText(RentOutRoomActivity.this, "Please Enter Floor Number", Toast.LENGTH_SHORT).show();
        } else if (roomType.equals("")) {
            Toast.makeText(RentOutRoomActivity.this, "Please Select Room Type", Toast.LENGTH_SHORT).show();
        } else {

            String checkboxValue = "";
            if (checkbox_TV.isChecked()) {
                checkboxValue = checkboxValue + "," + " " + checkbox_TV.getText();
            }
            if (checkbox_WiFi.isChecked()) {
                checkboxValue = checkboxValue + "," + " " + checkbox_WiFi.getText();
            }
            if (checkbox_Gyser.isChecked()) {
                checkboxValue = checkboxValue + "," + " " + checkbox_Gyser.getText();
            }
            if (checkbox_CarParking.isChecked()) {
                checkboxValue = checkboxValue + "," + " " + checkbox_CarParking.getText();
            }
            if (checkbox_BikeParking.isChecked()) {
                checkboxValue = checkboxValue + "," + " " + checkbox_BikeParking.getText();
            }

            if (checkboxValue.startsWith(",")) {
                checkboxValue = checkboxValue.startsWith(",") ? checkboxValue.substring(1) : checkboxValue;
            }
            if (checkboxValue.endsWith(",")) {
                checkboxValue = checkboxValue.substring(0, checkboxValue.length() - 1);
            }
            final int floorNo = Integer.parseInt(floorValue);
            Log.i("", "onClick: " + checkboxValue);
            Log.i("", "init: " + cityName + " " + roomAddress + " " + roomBudget + " " + mobileNo + " " + floorNo + " " + roomType);

            if (bitmap == null) {
                Toast.makeText(this, "Please Upload Image of your room!", Toast.LENGTH_SHORT).show();
                return;
            }
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
            byte[] img_byteArray = blob.toByteArray();

            final RentOutRoomModel roomModel = new RentOutRoomModel();
            roomModel.setCityName(cityName);
            roomModel.setRoomAddress(roomAddress);
            roomModel.setRoomBudget(roomBudget);
            roomModel.setPinCode(pinCode);
            roomModel.setMobileNo(mobileNo);
            roomModel.setFloorNo(floorNo);
            roomModel.setRoomType(roomType);
            roomModel.setAvailablefacility(checkboxValue);
            roomModel.setImage(img_byteArray);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    long returnId = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().insertAllRoomDetail(roomModel);
                    Log.i("", "run: returnId =" + returnId);
                }
            });
            finish();
        }
    }

    private void showAlertDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_charge_apply, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //submitFinalDetail();
            }
        });
        dialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                bitmap = Utility.getRealImage(bitmap);
                select_image.setImageBitmap(bitmap);
                select_image.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}