package com.example.habittracker.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitListDao {

    @Query("""SELECT * FROM habit_items WHERE type IN (:habitTypeFilter)
            AND name LIKE '%' || :search || '%' ORDER BY 
            CASE WHEN :orderBy = 'NAME_ASC' THEN name END ASC,
            CASE WHEN :orderBy = 'NAME_DESC' THEN name END DESC,
            CASE WHEN :orderBy = 'TIME_CREATION_ASC' THEN id END ASC,
            CASE WHEN :orderBy = 'TIME_CREATION_DESC' THEN id END DESC""")
    fun getList(
        habitTypeFilter: List<String>,
        orderBy: String,
        search: String
    ): LiveData<List<HabitItemDbModel>>?

    @Query("SELECT * FROM habit_items WHERE id = :habitItemId LIMIT 1")
    suspend fun getById(habitItemId: Int): HabitItemDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(habitItemDbModel: HabitItemDbModel)

    @Query("DELETE FROM habit_items WHERE id = :habitItemId")
    suspend fun delete(habitItemId: Int)

    @Update()
    suspend fun edit(habitItemDbModel: HabitItemDbModel)

}