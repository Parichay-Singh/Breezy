package com.patrollers.breezy.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.patrollers.breezy.models.DailyPoints;

import java.util.List;

@Dao
public interface DailyDao {

    @Insert
    void insertDaily(DailyPoints points);

    @Query("SELECT * FROM dailyPointDb")
    List<DailyPoints> loadAllPoints();

    @Query("DELETE FROM dailyPointDb")
    void resetDailyDb();

    @Query("SELECT COUNT(*) FROM dailyPointDb WHERE date = :inputDate")
    int isDatePresent(String inputDate);

    @Query("SELECT sleep FROM dailyPointDb ORDER BY id DESC LIMIT 1")
    int getSleep();

    @Query("SELECT mood FROM dailyPointDb ORDER BY id DESC LIMIT 1")
    int getMoodPoints();

    @Query("SELECT exercise FROM dailyPointDb ORDER BY id DESC LIMIT 1")
    boolean getExercise();

    @Query("SELECT meditation FROM dailyPointDb ORDER BY id DESC LIMIT 1")
    boolean getMeditate();

    @Query("SELECT isHydrated FROM dailyPointDb ORDER BY id DESC LIMIT 1")
    int getHydrated();

    @Query("UPDATE dailyPointDb SET exercise = :exer WHERE id =(SELECT MAX(id) FROM dailyPointDb)")
    void updateExercise(boolean exer);

    @Query("UPDATE dailyPointDb SET meditation = :med WHERE id =(SELECT MAX(id) FROM dailyPointDb)")
    void updateMeditate(boolean med);

    @Query("UPDATE dailyPointDb SET points = :point WHERE id =(SELECT MAX(id) FROM dailyPointDb)")
    void updatePoints(int point);

    @Query("SELECT points FROM dailyPointDb ORDER BY id DESC LIMIT 15")
    List<Integer> getGraphPoints();
}