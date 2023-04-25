package com.firstapp.braguia.ViewModel;

import static java.lang.Boolean.TRUE;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firstapp.braguia.Model.LoginBody;
import com.firstapp.braguia.Model.LoginResponse;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Repository.Repository;
import com.firstapp.braguia.UI.Register;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class ViewModel extends AndroidViewModel {
    private final Repository mRepository;
    private final LiveData<List<Trail>> mAllTrails;
    private final LiveData<List<Trail>> apiTrails;

    MutableLiveData<String> mLoginResultMutableData = new MutableLiveData<>();

    public ViewModel (Application application) throws IOException {
        super(application);
        mRepository = new Repository(application);
        mAllTrails = mRepository.getAllLocalTrails();
        apiTrails = mRepository.getTrails();
        mLoginResultMutableData.postValue("Not logged in");
    }

    public Boolean login(String email, String password){
        return mRepository.loginRemote(email,password);
    }

    public LiveData<List<Trail>> getmAllTrails() {
        return mAllTrails;
    }

    public LiveData<List<Trail>> getTrails(){
        return this.apiTrails;
    }

    public void insert (Trail trail) {mRepository.insert(trail);}

    public LiveData<String> getLoginResult(){
        return mLoginResultMutableData;
    }
}
