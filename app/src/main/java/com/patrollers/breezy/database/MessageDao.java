package com.patrollers.breezy.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.patrollers.breezy.models.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insertItem(Message msg);

    @Query("SELECT * FROM messageDb")
    List<Message> loadAllMessages();

    @Query("DELETE FROM messageDb")
    void deleteAllMessages();
}
