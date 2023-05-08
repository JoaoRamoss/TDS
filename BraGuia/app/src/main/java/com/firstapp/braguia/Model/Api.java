package com.firstapp.braguia.Model;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface Api {
    @GET("trails")
    Call<List<Trail>> getTrails();

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@Field("username") String user, @Field("password") String password);


    @GET("user")
    Call<User> getUser(@Header("Cookie") String tokens);
}
