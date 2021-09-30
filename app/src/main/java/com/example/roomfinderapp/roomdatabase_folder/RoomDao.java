package com.example.roomfinderapp.roomdatabase_folder;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

@Dao
public interface RoomDao {

    @Query("SELECT * FROM BecomeRoomMateModel")
    List<BecomeRoomMateModel> getAllRoomMateList();

    @Query("SELECT * FROM RentOutRoomModel")
    List<RentOutRoomModel> getAllRoomDetailList();


    @Insert
    void insertAll(BecomeRoomMateModel becomeRoomMates);

    @Insert
    long insertAllRoomDetail(RentOutRoomModel rentOutRoomModels);

    @Insert
    long insertSignUpDetail(SignUpModel signUpModel);


    @Query("SELECT * FROM SignUpModel")
    List<SignUpModel> getAllSignUpDetail();

    @Query("SELECT * FROM  SignUpModel WHERE emailId= (:email) AND password=(:password)")
    SignUpModel verifyUserDetail(String email, String password);

    @Query("SELECT purpose FROM  SignUpModel WHERE emailId= :emailid")
    String getPurpose(String emailid);

    @Query("SELECT COUNT() FROM SignUpModel WHERE emailId = :MAIL_ID")
    int checkEntry(String MAIL_ID);

    @Insert
    long insertLatLong(LatLongModel latLongModel);

    @Query("SELECT * FROM LatLongModel")
    List<LatLongModel> getAllLatLongData();

    @Query("SELECT * FROM LatLongModel WHERE deviceName=:devieName LIMIT 1")
    List<LatLongModel> getSingleDeviceRecord(String devieName);

    //to get last record
/*    @Query("SELECT * FROM SignUpModel WHERE id=(SELECT MAX(ID)  FROM SignUpModel)")
    List<SignUpModel> getAllSignUpDetailf();*/


}
