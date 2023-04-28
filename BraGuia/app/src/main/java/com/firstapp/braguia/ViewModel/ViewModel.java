package com.firstapp.braguia.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Repository.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ViewModel extends AndroidViewModel {
    private final Repository mRepository;
    private final LiveData<List<Trail>> mAllTrails;
    private final LiveData<List<Trail>> apiTrails;

    public ViewModel (Application application) throws IOException {
        super(application);
        mRepository = new Repository(application);
        mAllTrails = mRepository.getAllLocalTrails();
        apiTrails = mRepository.getTrails();
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
}
