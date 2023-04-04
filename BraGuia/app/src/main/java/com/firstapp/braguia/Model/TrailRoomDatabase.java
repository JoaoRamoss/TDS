package com.firstapp.braguia.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Trail.class}, version = 1, exportSchema = false)
public abstract class TrailRoomDatabase extends RoomDatabase {

    public abstract TrailDao trailDao();

    private static volatile TrailRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static TrailRoomDatabase getDatabase (final Context context) {
        if (INSTANCE == null) {
            synchronized (TrailRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TrailRoomDatabase.class, "trail_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
