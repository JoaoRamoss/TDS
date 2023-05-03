package com.firstapp.braguia.Model;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrailDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Trail trail);

    @Query("DELETE FROM trail_table")
    void deleteAll();

    @Query("SELECT DISTINCT * FROM trail_table")
    LiveData<List<Trail>> getTrails();
}
