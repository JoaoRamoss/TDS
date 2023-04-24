package com.firstapp.braguia.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface Api {
    @GET("trails")
    Call<List<Trail>> getTrails();

    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginBody loginBody);

}
