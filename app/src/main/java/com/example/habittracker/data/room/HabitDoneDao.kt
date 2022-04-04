package com.example.habittracker.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface HabitDoneDao {

    @Insert()
    suspend fun add(habitDoneDbModel: HabitDoneDbModel): Long

    @Query("DELETE FROM habit_done WHERE id = :habitDoneId")
    suspend fun delete(habitDoneId: Int)

}