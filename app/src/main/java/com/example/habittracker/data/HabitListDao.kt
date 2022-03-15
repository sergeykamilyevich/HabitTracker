package com.example.habittracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.habittracker.domain.HabitItem

@Dao
interface HabitListDao {

    @Query("SELECT * FROM habit_items")
    fun getList(): LiveData<List<HabitItemDbModel>>?

    @Query("SELECT * FROM habit_items WHERE id = :habitItemId LIMIT 1")
    suspend fun getById(habitItemId: Int): HabitItemDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(habitItemDbModel: HabitItemDbModel)

    @Query("DELETE FROM habit_items WHERE id = :habitItemId")
    suspend fun delete(habitItemId: Int)

    @Update()
    suspend fun edit(habitItemDbModel: HabitItemDbModel)

}