package com.firstapp.braguia.Repository;
import android.app.Application;

import androidx.lifecycle.LiveData;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Model.TrailDao;
import com.firstapp.braguia.Model.TrailRoomDatabase;

import java.util.List;

public class Repository {
    private final TrailDao localTrailDao;

    //All trails stored locally
    private final LiveData<List<Trail>> allLocalTrails;

    public Repository (Application application) {
        TrailRoomDatabase db = TrailRoomDatabase.getDatabase(application);
        localTrailDao = db.trailDao();
        allLocalTrails = localTrailDao.getAlphabetizedTrails();
    }

    LiveData<List<Trail>> getAllLocalTrails(){return allLocalTrails;}

    void insert (Trail trail) {
        TrailRoomDatabase.databaseWriteExecutor.execute(() -> {
            localTrailDao.insert(trail);
        });
    }
}
