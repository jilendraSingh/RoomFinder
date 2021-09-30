package com.example.roomfinderapp.roomdatabase_folder;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RentOutRoomModel {
    @PrimaryKey(autoGenerate = true)
    int uid;

    @ColumnInfo(name = "cityname")
    String cityName;
    @ColumnInfo(name = "roomAddress")
    String roomAddress;

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    @ColumnInfo(name = "pinCode")
    String pinCode;
    @ColumnInfo(name = "roomBudget")
    String roomBudget;
    @ColumnInfo(name = "mobileNo")
    String mobileNo;
    @ColumnInfo(name = "floorNo")
    int floorNo;
    @ColumnInfo(name = "roomType")
    String roomType;
    @ColumnInfo(name = "availablefacility")
    String availablefacility;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image ;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setRoomAddress(String roomAddress) {
        this.roomAddress = roomAddress;
    }

    public void setRoomBudget(String roomBudget) {
        this.roomBudget = roomBudget;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setFloorNo(int floorNo) {
        this.floorNo = floorNo;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setAvailablefacility(String availablefacility) {
        this.availablefacility = availablefacility;
    }

    public int getUid() {
        return uid;
    }

    public String getCityName() {
        return cityName;
    }

    public String getRoomAddress() {
        return roomAddress;
    }

    public String getRoomBudget() {
        return roomBudget;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getAvailablefacility() {
        return availablefacility;
    }

}
