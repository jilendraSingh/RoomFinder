package com.example.roomfinderapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.example.roomfinderapp.adapter_classes.FindRoomAdapter;
import com.example.roomfinderapp.R;
import com.example.roomfinderapp.roomdatabase_folder.DatabaseClient;
import com.example.roomfinderapp.roomdatabase_folder.RentOutRoomModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindRoomActivity extends AppCompatActivity {
    RecyclerView findroom_ListView;


    List<RentOutRoomModel> roomDetailList;
    List<HashMap<String, String>> arrayList;
    List<HashMap<String, byte[]>> imgArrayList;
    Handler handler;
    Runnable runnable;
    FindRoomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_mate_list);

        roomDetailList = new ArrayList<>();
        arrayList = new ArrayList<>();
        imgArrayList = new ArrayList<>();

        handler = new Handler();


        findroom_ListView = findViewById(R.id.roommateListView);
        findroom_ListView.setLayoutManager(new LinearLayoutManager(this));

        findroom_ListView.addItemDecoration(new DividerItemDecoration(findroom_ListView.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new FindRoomAdapter(this, arrayList, imgArrayList);
        findroom_ListView.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        roomDetailList.clear();
        arrayList.clear();
        imgArrayList.clear();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                roomDetailList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().getAllRoomDetailList();

            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                if (roomDetailList.size() != 0) {
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
        HashMap<String, byte[]> img_hashmap;
        for (int i = 0; i < roomDetailList.size(); i++) {
            hashMap = new HashMap<>();
            hashMap.put("id", String.valueOf(roomDetailList.get(i).getUid()));
            hashMap.put("budget", String.valueOf(roomDetailList.get(i).getRoomBudget()));
            hashMap.put("city", roomDetailList.get(i).getCityName());
            hashMap.put("phone", roomDetailList.get(i).getMobileNo());
            hashMap.put("floor", String.valueOf(roomDetailList.get(i).getFloorNo()));
            hashMap.put("roomtype", String.valueOf(roomDetailList.get(i).getRoomType()));
            hashMap.put("facility", String.valueOf(roomDetailList.get(i).getAvailablefacility()));
            hashMap.put("address", roomDetailList.get(i).getRoomAddress());
            arrayList.add(hashMap);
        }

        for (int i = 0; i < roomDetailList.size(); i++) {
            img_hashmap = new HashMap<>();
            img_hashmap.put("roomimage", roomDetailList.get(i).getImage());
            imgArrayList.add(img_hashmap);
        }

        if (arrayList.size() != 0) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
