package com.firstapp.braguia.Model;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("trails")
    Call<List<Trail>> getTrails();

}
