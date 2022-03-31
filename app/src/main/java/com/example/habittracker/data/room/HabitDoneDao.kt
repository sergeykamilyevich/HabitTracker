package com.example.habittracker.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitDoneDao {

    @Insert()
    suspend fun add(habitDoneDbModel: HabitDoneDbModel)

    @Query("DELETE FROM habit_done WHERE id = :habitDoneId")
    suspend fun delete(habitDoneId: Int)


}