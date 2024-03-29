package com.firstapp.braguia.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Model.User;
import com.firstapp.braguia.Repository.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ViewModel extends AndroidViewModel{
    private final Repository mRepository;
    private final LiveData<List<Trail>> mAllTrails;
    private final LiveData<List<Trail>> apiTrails;

    private final LiveData<User> apiUser;


    public ViewModel (Application application) throws IOException {
        super(application);
        mRepository = new Repository(application);
        mAllTrails = mRepository.getAllLocalTrails();
        apiTrails = mRepository.getTrails();
        apiUser = mRepository.getUser();
    }

    public LiveData<Boolean> login(String email, String password){
        return mRepository.loginRemote(email,password);
    }

    public LiveData<List<Trail>> getmAllTrails() {
        return mAllTrails;
    }

    public LiveData<List<Trail>> getTrails(){
        return this.apiTrails;
    }

    public void insert (Trail trail) {mRepository.insert(trail);}

    public Map<String, ?> getCookies(){return mRepository.getCookies();}

    public void clearCookies(){mRepository.clearCookies();}

    public void clearHistory(){mRepository.deleteHistory();}


    public LiveData<User> getUser(){
        return this.apiUser;
    }

    public LiveData<User> getLocalUser(){
        return mRepository.getLocalUser();
    }

    public void insert (User user) {mRepository.insert(user);}

    public void delete () {mRepository.deleteUser();}

    public void logout() {mRepository.logout();}


}
