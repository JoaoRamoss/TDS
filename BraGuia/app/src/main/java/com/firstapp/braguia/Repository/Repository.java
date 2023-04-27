package com.firstapp.braguia.Repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firstapp.braguia.Model.Api;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Model.TrailDao;
import com.firstapp.braguia.Model.TrailRoomDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private final TrailDao localTrailDao;
    private Api api;

    SharedPreferences sharedPreferences;

    private static final String BASE_URL = "http://192.168.85.186";


    //All trails stored locally
    private final LiveData<List<Trail>> allLocalTrails;

    //All trails from API
    private LiveData<List<Trail>> allTrails;

    private MutableLiveData<Boolean> isLogin = new MutableLiveData<>();




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
        this.sharedPreferences =  application.getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

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

    public LiveData<Boolean> loginRemote(String user, String pass){

        MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        Call<ResponseBody> initiateLogin = api.login(user, pass);

        initiateLogin.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    // We get 2 "Set-Cookie". Split it and store each in an array.
                    List<String> cookies = new ArrayList<>();
                    for (int i = 0; i < response.headers().size(); i++) {
                        String headerName = response.headers().name(i);
                        String headerValue = response.headers().value(i);
                        if (headerName.equalsIgnoreCase("Set-Cookie")) {
                            cookies.add(headerValue);
                        }
                    }

                    // Store each cookie in an individual string
                    String csrfToken = cookies.get(0);
                    String sessionId = cookies.get(1);

                    // Locally store each cookie
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("csrfToken", csrfToken);
                    editor.putString("sessionId", sessionId);
                    editor.apply();

                    isLoggedIn.setValue(true);
                }
                else {
                    try {
                        String errorMessage = response.errorBody().string();
                        System.out.println(errorMessage);
                        isLoggedIn.setValue(false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t);

            }
        });
        return isLoggedIn;
    }
    

}
