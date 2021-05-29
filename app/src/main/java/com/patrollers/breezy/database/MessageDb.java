package com.patrollers.breezy.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.patrollers.breezy.models.Message;

@Database(entities = {Message.class}, version = 1, exportSchema = false)
public abstract class MessageDb extends RoomDatabase {
    private static final String DATABASE_NAME = "messageDb";
    private static MessageDb sInstance;

    public static MessageDb getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    MessageDb.class, MessageDb.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    public abstract MessageDao messageDao();
}