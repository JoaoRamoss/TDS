package com.firstapp.braguia.Model;

import com.google.gson.annotations.SerializedName;

public class LoginBody {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public LoginBody(String email,String password){
        this.email=email;
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
