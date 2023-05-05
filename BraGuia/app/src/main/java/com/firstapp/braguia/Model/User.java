package com.firstapp.braguia.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "user_table")
public class User implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "username")
    @SerializedName("username")
    @NonNull
    private String username;
    @ColumnInfo(name = "user_type")
    @SerializedName("user_type")
    private String userType;

    @ColumnInfo(name = "last_login")
    @SerializedName("last_login")
    private String lastLogin;

    @ColumnInfo(name = "is_superuser")
    @SerializedName("is_superuser")
    private boolean isSuperuser;

    @ColumnInfo(name = "first_name")
    @SerializedName("first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    @SerializedName("last_name")
    private String lastName;

    @ColumnInfo(name = "email")
    @SerializedName("email")
    private String email;

    @ColumnInfo(name = "is_staff")
    @SerializedName("is_staff")
    private boolean isStaff;

    @ColumnInfo(name = "is_active")
    @SerializedName("is_active")
    private boolean isActive;

    @ColumnInfo(name = "date_joined")
    @SerializedName("date_joined")
    private String dateJoined;

    // Constructor
    public User(String userType, String lastLogin, boolean isSuperuser, String username, String firstName, String lastName, String email, boolean isStaff, boolean isActive, String dateJoined) {
        this.userType = userType;
        this.lastLogin = lastLogin;
        this.isSuperuser = isSuperuser;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isStaff = isStaff;
        this.isActive = isActive;
        this.dateJoined = dateJoined;
    }

    // Getter and setter methods
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isSuperuser() {
        return isSuperuser;
    }

    public void setSuperuser(boolean isSuperuser) {
        this.isSuperuser = isSuperuser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }
}
