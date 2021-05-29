package com.patrollers.breezy.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.patrollers.breezy.models.DailyPoints;

@Database(entities = {DailyPoints.class}, version = 3, exportSchema = false)
public abstract class DailyPointDb extends RoomDatabase {
    private static final String DATABASE_NAME = "dailyPointDb";
    private static DailyPointDb sInstance;

    public static DailyPointDb getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    DailyPointDb.class, DailyPointDb.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    public abstract DailyDao dailyDao();
}
