package com.firstapp.braguia.Repository;
import static androidx.fragment.app.FragmentManager.TAG;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firstapp.braguia.Model.Api;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Model.TrailDao;
import com.firstapp.braguia.Model.TrailRoomDatabase;
import com.google.android.gms.common.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository{
    private final TrailDao localTrailDao;
    private Api api;

    private static final String BASE_URL = "http://192.168.85.186";


    //All trails stored locally
    private final LiveData<List<Trail>> allLocalTrails;

    //All trails from API
    private LiveData<List<Trail>> allTrails;


    public Repository (Application application) throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);

        TrailRoomDatabase db = TrailRoomDatabase.getDatabase(application);
        localTrailDao = db.trailDao();
        allLocalTrails = localTrailDao.getAlphabetizedTrails();
        allTrails = getTrails();
    }

    public LiveData<List<Trail>> getAllLocalTrails(){return allLocalTrails;}

    public LiveData<List<Trail>> getTrails() throws IOException {
        MutableLiveData<List<Trail>> trailLiveData = new MutableLiveData<>();
        Call<List<Trail>> call = api.getTrails();

        call.enqueue(new Callback<List<Trail>>() {
            @Override
            public void onResponse(Call<List<Trail>> call, Response<List<Trail>> response) {
                if (response.isSuccessful()){
                    trailLiveData.setValue(response.body());
                }
                else {
                    System.out.println("Oops!");
                }
            }

            @Override
            public void onFailure(Call<List<Trail>> call, Throwable t) {
                System.out.println(t);
            }
        });

        return trailLiveData;
    }


    public void insert(Trail trail) {
        TrailRoomDatabase.databaseWriteExecutor.execute(() -> {
            localTrailDao.insert(trail);
        });
    }

}
