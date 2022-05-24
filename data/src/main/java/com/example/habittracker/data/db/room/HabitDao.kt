package com.example.habittracker.data.db.room

import androidx.room.*
import com.example.habittracker.data.db.models.HabitDbModel
import com.example.habittracker.data.db.models.HabitDoneDbModel
import com.example.habittracker.data.db.models.HabitWithDoneDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Transaction
    @Query(
        """
            SELECT * FROM habit_items WHERE type IN (:habitTypeFilter)
            AND name LIKE '%' || :search || '%' ORDER BY 
            CASE WHEN :orderBy = 'NAME_ASC' THEN name END ASC,
            CASE WHEN :orderBy = 'NAME_DESC' THEN name END DESC,
            CASE WHEN :orderBy = 'TIME_CREATION_ASC' THEN id END ASC,
            CASE WHEN :orderBy = 'TIME_CREATION_DESC' THEN id END DESC,
            CASE WHEN :orderBy = 'PRIORITY_ASC' THEN priority END ASC,
            CASE WHEN :orderBy = 'PRIORITY_DESC' THEN priority END DESC
            """
    )
    fun getList(
        habitTypeFilter: List<String>,
        orderBy: String,
        search: String
    ): Flow<List<HabitWithDoneDbModel>>?

    @Transaction
    @Query("SELECT * FROM habit_items")
    suspend fun getUnfilteredList(): List<HabitWithDoneDbModel>?

    @Transaction
    @Query("SELECT * FROM habit_items WHERE id = :habitItemId LIMIT 1")
    suspend fun getHabitById(habitItemId: Int): HabitWithDoneDbModel?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertHabit(habitDbModel: HabitDbModel): Long

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateHabit(habitDbModel: HabitDbModel)

    @Query("DELETE FROM habit_items WHERE id = :habitItemId")
    suspend fun deleteHabit(habitItemId: Int)

    @Insert()
    suspend fun insertHabitDone(habitDoneDbModel: HabitDoneDbModel): Long

    @Query("DELETE FROM habit_done WHERE id = :habitDoneId")
    suspend fun deleteHabitDone(habitDoneId: Int)


}