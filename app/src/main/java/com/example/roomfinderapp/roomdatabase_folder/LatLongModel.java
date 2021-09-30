package com.example.roomfinderapp.roomdatabase_folder;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LatLongModel {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "emailId")
    String emailId;

    @ColumnInfo(name = "deviceLatitude")
    String deviceLatitude;

    @ColumnInfo(name = "deviceLongitude")
    String deviceLongitude;

    @ColumnInfo(name = "dateandTime")
    String dateandTime;

    @ColumnInfo(name = "deviceName")
    String deviceName;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    public int getId() {
        return id;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getDeviceLatitude() {
        return deviceLatitude;
    }

    public String getDeviceLongitude() {
        return deviceLongitude;
    }

    public String getDateandTime() {
        return dateandTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setDeviceLatitude(String deviceLatitude) {
        this.deviceLatitude = deviceLatitude;
    }

    public void setDeviceLongitude(String deviceLongitude) {
        this.deviceLongitude = deviceLongitude;
    }

    public void setDateandTime(String dateandTime) {
        this.dateandTime = dateandTime;
    }


}
