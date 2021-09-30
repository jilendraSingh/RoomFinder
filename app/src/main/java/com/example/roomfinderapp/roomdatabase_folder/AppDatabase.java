package com.example.roomfinderapp.roomdatabase_folder;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {BecomeRoomMateModel.class,RentOutRoomModel.class,SignUpModel.class,LatLongModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RoomDao roomDao();
}
