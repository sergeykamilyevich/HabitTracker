package com.example.habittracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitType

@Dao
interface HabitListDao {

    @Query("SELECT * FROM habit_items")
    fun getList(): LiveData<List<HabitItemDbModel>>?

    @Query("SELECT * FROM habit_items WHERE type IN (:habitTypeFilter)")
    fun getFilteredList(habitTypeFilter: List<String>): LiveData<List<HabitItemDbModel>>?

    @Query("SELECT * FROM habit_items WHERE id = :habitItemId LIMIT 1")
    suspend fun getById(habitItemId: Int): HabitItemDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(habitItemDbModel: HabitItemDbModel)

    @Query("DELETE FROM habit_items WHERE id = :habitItemId")
    suspend fun delete(habitItemId: Int)

    @Update()
    suspend fun edit(habitItemDbModel: HabitItemDbModel)

}