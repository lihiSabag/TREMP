package com.example.tremp.model;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tremp.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;


@Entity
public class User {
    final static String EMAIL = "email";
    public final static String LAST_UPDATED = "LAST_UPDATED";

    @PrimaryKey
    @NonNull
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userAddress;
    private String userPhone;
    Long lastUpdated = new Long(0);

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public Long getLastUpdated() {
        return lastUpdated;
    }

    public User()
    {
        this.userEmail = " ";
        this.userFirstName = " ";
        this.userLastName = " ";
        this.userAddress = " ";
        this.userPhone = " ";
    }

    public User(String uEmail,String uFirstName,String uLastName,String uAddress,String uPhone)
    {   this.userEmail = uEmail;
        this.userFirstName = uFirstName;
        this.userLastName = uLastName;
        this.userAddress = uAddress;
        this.userPhone = uPhone;
    }
    //setters
    public void setUserEmail(String uEmail){this.userEmail = uEmail;}
    public void setUserFirstName(String uName){this.userFirstName = uName;}
    public void setUserLastName(String uLastName){this.userLastName = uLastName;}
    public void setUserAddress(String uAddress){this.userAddress = uAddress;}
    public void setUserPhone(String uPhone){this.userPhone = uPhone;}

    //getters
    public String getUserEmail(){return this.userEmail;}
    public String getUserFirstName(){return this.userFirstName; }
    public String getUserLastName(){return this.userLastName ;}
    public String getUserAddress(){return this.userAddress ;}
    public String getUserPhone(){return this.userPhone;}

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(EMAIL, getUserEmail());
        json.put("firstname", getUserFirstName());
        json.put("lastname", getUserFirstName());
        json.put("address", getUserFirstName());
        json.put("phone", getUserFirstName());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());

        return json;
    }
    //convert from json Map to User object
    static User fromJson(Map<String,Object> json){
        String email = (String)json.get(EMAIL);
        if (email == null){
            return null;
        }
        String firstName = (String)json.get("firstname");
        String lastName = (String)json.get("lastname");
        String address = (String)json.get("address");
        String phone = (String)json.get("phone");
        User user = new User(email,firstName,lastName,address,phone);
        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        user.setLastUpdated(new Long(ts.getSeconds()));
        return user;
    }

    static Long getLocalLastUpdated(){
        Long localLastUpdate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("USER_LAST_UPDATE",0);
        return localLastUpdate;
    }

    static void setLocalLastUpdated(Long date){
        SharedPreferences.Editor editor = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong("USER_LAST_UPDATE",date);
        editor.commit();
        Log.d("TAG", "new lud " + date);
    }


}