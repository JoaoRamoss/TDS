package com.firstapp.braguia.ViewModel;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Repository.Repository;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private final Repository mRepository;
    private final LiveData<List<Trail>> mAllTrails;

    public ViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllTrails = mRepository.getAllLocalTrails();
    }

    public LiveData<List<Trail>> getmAllTrails() {
        return mAllTrails;
    }

    public void insert (Trail trail) {mRepository.insert(trail);}
}
