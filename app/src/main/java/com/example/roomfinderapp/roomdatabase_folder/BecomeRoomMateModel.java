package com.example.roomfinderapp.roomdatabase_folder;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BecomeRoomMateModel {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String userName;

    @ColumnInfo(name = "age")
    public int age;

    @ColumnInfo(name = "budget")
    public int budget;

    @ColumnInfo(name = "contact")
    public String contact;

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "gender")
    public String gender;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public int getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public int getAge() {
        return age;
    }

    public int getBudget() {
        return budget;
    }

    public String getGender() {
        return gender;
    }


}
