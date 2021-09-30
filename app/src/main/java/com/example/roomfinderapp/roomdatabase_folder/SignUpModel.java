package com.example.roomfinderapp.roomdatabase_folder;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SignUpModel {
    @PrimaryKey (autoGenerate = true)
    int id;

    @ColumnInfo(name = "name")
     String name;
    @ColumnInfo(name = "phone")
    String phoneNumber;
    @ColumnInfo(name = "emailId")
    String emailId;
    @ColumnInfo(name = "password")
    String password;
    @ColumnInfo(name = "confirm_password")
    String confirm_password;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public String getPurpose() {
        return purpose;
    }

    @ColumnInfo(name = "purpose")
    String purpose;
}
