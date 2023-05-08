package com.firstapp.braguia.Repository;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firstapp.braguia.Model.Api;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Model.TrailDao;
import com.firstapp.braguia.Model.TrailRoomDatabase;
import com.firstapp.braguia.Model.User;
import com.firstapp.braguia.Model.UserDao;
import com.firstapp.braguia.Model.UserRoomDatabase;
import com.firstapp.braguia.Utils.CookieValidation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
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

    private final LiveData<User> localUser;

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
        UserRoomDatabase dbu = UserRoomDatabase.getDatabase(application);
        localUserDao = dbu.userDao();
        localTrailDao = db.trailDao();
        allLocalTrails = localTrailDao.getTrails();
        allTrails = getTrails();
        localUser = localUserDao.getUser();
        this.sharedPreferences =  application.getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
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

    public void deleteHistory(){
        TrailRoomDatabase.databaseWriteExecutor.execute(() -> {
            localTrailDao.deleteAll();
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

    public Map<String, ?> getCookies(){return sharedPreferences.getAll();}


    public void clearCookies(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }



    public LiveData<User> getUser() throws IOException {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        Map<String, ?> cookies = getCookies();

        if (!cookies.isEmpty()) {
            String csrf = CookieValidation.extractCookieValue("csrftoken", cookies.get("csrfToken").toString());
            String session = CookieValidation.extractCookieValue("sessionid", cookies.get("sessionId").toString());
            Call<User> call = api.getUser(CookieValidation.getFormatedCookies(csrf, session));
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

    public void insert(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            localUserDao.insert(user);
        });
    }

    public void deleteUser(){
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            localUserDao.deleteAll();
        });
    }

    public LiveData<User> getLocalUser(){
        return this.localUser;
    }


}
