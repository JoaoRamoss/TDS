package com.firstapp.braguia.Repository;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.firstapp.braguia.Model.Api;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Model.TrailDao;
import com.firstapp.braguia.Model.TrailRoomDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository{
    private final TrailDao localTrailDao;

    public static final String BASE_URL = "https://c5a2-193-137-92-29.eu.ngrok.io";

    private List<Trail> trails = null;

    //All trails stored locally
    private final LiveData<List<Trail>> allLocalTrails;

    public Repository (Application application) {
        TrailRoomDatabase db = TrailRoomDatabase.getDatabase(application);
        localTrailDao = db.trailDao();
        allLocalTrails = localTrailDao.getAlphabetizedTrails();
    }

    public LiveData<List<Trail>> getAllLocalTrails(){return allLocalTrails;}



    public void insert(Trail trail) {
        TrailRoomDatabase.databaseWriteExecutor.execute(() -> {
            localTrailDao.insert(trail);
        });
    }
}
