package com.firstapp.braguia.Repository;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firstapp.braguia.Model.Api;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Model.TrailDao;
import com.firstapp.braguia.Model.TrailRoomDatabase;
import com.firstapp.braguia.Model.User;
import com.firstapp.braguia.Model.UserDao;
import com.firstapp.braguia.Model.UserRoomDatabase;
import com.firstapp.braguia.Utils.CookieOven;
import com.firstapp.braguia.Utils.CookieValidation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private final TrailDao localTrailDao;

    private final UserDao localUserDao;
    private Api api;

    SharedPreferences sharedPreferences;

    // http://192.168.85.186 -> Alternative IP
    // https://c5a2-193-137-92-29.eu.ngrok.io -> Main IP
    private static final String BASE_URL = "https://c5a2-193-137-92-29.eu.ngrok.io";


    //All trails stored locally
    private final LiveData<List<Trail>> allLocalTrails;

    // Locally stored user Details
    private final LiveData<User> localUser;


    public Repository(Application application) throws IOException {

        // Creates Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);

        // Handle Rooms and Dao
        TrailRoomDatabase db = TrailRoomDatabase.getDatabase(application);
        UserRoomDatabase dbu = UserRoomDatabase.getDatabase(application);
        localUserDao = dbu.userDao();
        localTrailDao = db.trailDao();

        // Fills instance variables.
        allLocalTrails = localTrailDao.getTrails();
        localUser = localUserDao.getUser();

        // Create "myPrefs" sharedPreference in case it doesn't already exist. Retrieves it in case it does.
        this.sharedPreferences =  application.getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
    }


    // Get all locally stored trails
    public LiveData<List<Trail>> getAllLocalTrails(){return allLocalTrails;}

    // Gets trail list from API
    public LiveData<List<Trail>> getTrails() throws IOException {
        Map<String, ?> cookies = getCookies();
        MutableLiveData<List<Trail>> trailLiveData = new MutableLiveData<>();

        if (!cookies.isEmpty()) {
            String csrf = CookieOven.extractCsrfTokenValue(Objects.requireNonNull(cookies.get("csrftoken")).toString());
            String session = CookieOven.extractSessionIdValue(Objects.requireNonNull(cookies.get("sessionid")).toString());
            Call<List<Trail>> call = api.getTrails(CookieOven.getFormatedCookies(csrf, session));

            call.enqueue(new Callback<List<Trail>>() {
                @Override
                public void onResponse(Call<List<Trail>> call, Response<List<Trail>> response) {
                    if (response.isSuccessful()) {
                        trailLiveData.setValue(response.body());
                    } else {
                        System.out.println("Oops!");
                    }
                }

                @Override
                public void onFailure(Call<List<Trail>> call, Throwable t) {
                    System.out.println(t);
                }
            });
        }

        return trailLiveData;
    }


    // Inserts trail into database
    public void insert(Trail trail) {
        TrailRoomDatabase.databaseWriteExecutor.execute(() -> {
            localTrailDao.insert(trail);
        });
    }

    // Deletes locally stored trails (AKA trail history)
    public void deleteHistory(){
        TrailRoomDatabase.databaseWriteExecutor.execute(() -> {
            localTrailDao.deleteAll();
        });
    }

    // Makes login request to API

    public LiveData<Boolean> loginRemote(String user, String pass){

        MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

        // Make POST to API
        Call<ResponseBody> initiateLogin = api.login(user, pass);

        // Handle API response
        initiateLogin.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    // We get 2 "Set-Cookie". Split it and store each in an array.
                    List<String> cookies = CookieOven.formatCookiesForStorage(response);

                    // Store each cookie in an individual string
                    String csrfToken = cookies.get(0);
                    String sessionId = cookies.get(1);

                    // Locally store each cookie
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("csrftoken", csrfToken);
                    editor.putString("sessionid", sessionId);
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

    // Gets locally stored cookies
    public Map<String, ?> getCookies(){return sharedPreferences.getAll();}


    // Clears locally stored cookies when user logs out
    public void clearCookies(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }



    // Gets user information from API
    public LiveData<User> getUser() throws IOException {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        Map<String, ?> cookies = getCookies();

        if (!cookies.isEmpty()) {
            String csrf = CookieOven.extractCsrfTokenValue(Objects.requireNonNull(cookies.get("csrftoken")).toString());
            String session = CookieOven.extractSessionIdValue(Objects.requireNonNull(cookies.get("sessionid")).toString());
            Call<User> call = api.getUser(CookieOven.getFormatedCookies(csrf, session));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        userLiveData.setValue(response.body());
                    } else {
                        System.out.println(response);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    System.out.println("Error!!");
                    System.out.println(t);
                }
            });
        }
        return userLiveData;
    }

    // Inserts user in Database
    public void insert(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            localUserDao.insert(user);
        });
    }

    // Deletes locally stored user
    public void deleteUser(){
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            localUserDao.deleteAll();
        });
    }

    // Gets user stored locally (in database)
    public LiveData<User> getLocalUser(){
        return this.localUser;
    }


    public void logout() {
        Map<String, ?> cookies = getCookies();

        if (!cookies.isEmpty()) {
            String csrf = CookieOven.extractCsrfTokenValue(Objects.requireNonNull(cookies.get("csrftoken")).toString());
            Call<ResponseBody> call = api.logout(csrf);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        System.out.println("Successfully logged out");
                    } else {
                        System.out.println(response);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("Error!!");
                    System.out.println(t);
                }
            });
        }
    }


}
