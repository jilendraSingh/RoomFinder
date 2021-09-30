package com.example.roomfinderapp.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomfinderapp.Constants;
import com.example.roomfinderapp.R;
import com.example.roomfinderapp.adapter_classes.RoomMateListAdapter;
import com.example.roomfinderapp.roomdatabase_folder.BecomeRoomMateModel;
import com.example.roomfinderapp.roomdatabase_folder.DatabaseClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomListActivity extends AppCompatActivity {

    RecyclerView roommateListView;

    List<BecomeRoomMateModel> roomMateList;
    List<HashMap<String, String>> arrayList;
    RoomMateListAdapter adapter;
    Handler handler;
    Runnable runnable;
    SharedPreferences sharedPreferences;
    String purposeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_mate_list);
        roomMateList = new ArrayList<>();
        arrayList = new ArrayList<>();


        sharedPreferences = getSharedPreferences(Constants.MyPREFERENCES, MODE_PRIVATE);
        purposeValue = sharedPreferences.getString(Constants.KEY_PURPOSE, "");

        roommateListView = findViewById(R.id.roommateListView);
        roommateListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomMateListAdapter(this, arrayList,purposeValue);
        roommateListView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arrayList.size() != 0) {
            arrayList.clear();
            roomMateList.clear();
        }

        handler = new Handler();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                roomMateList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().getAllRoomMateList();

            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                if (roomMateList.size() != 0) {
                    addData();
                } else {
                    handler.postDelayed(runnable, 100);
                }
            }
        };
        handler.post(runnable);
    }

    private void addData() {
        HashMap<String, String> hashMap;
        for (int i = 0; i < roomMateList.size(); i++) {
            hashMap = new HashMap<>();
            hashMap.put("name", roomMateList.get(i).getUserName());
            hashMap.put("budget", String.valueOf(roomMateList.get(i).getBudget()));
            hashMap.put("gender", roomMateList.get(i).getGender());
            hashMap.put("address", roomMateList.get(i).getAddress());
            hashMap.put("phone", roomMateList.get(i).getContact());
            arrayList.add(hashMap);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}