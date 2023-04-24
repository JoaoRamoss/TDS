package com.firstapp.braguia.ViewModel;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firstapp.braguia.Model.LoginBody;
import com.firstapp.braguia.Model.LoginResponse;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Repository.Repository;

import java.io.IOException;
import java.util.List;

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

    public void login(String email, String password){
        mRepository.loginRemote(new LoginBody(email,password),new Repository.IloginResponse(){
            @Override
            public void OnResponse(LoginResponse loginResponse) {
                mLoginResultMutableData.postValue("Login Success");
                System.out.println("sucesso");
            }
            @Override
            public void onFailure(Throwable t) {
                mLoginResultMutableData.postValue("Login Failure: "+t.getLocalizedMessage());
                System.out.println("insucesso");
            }
        });
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
