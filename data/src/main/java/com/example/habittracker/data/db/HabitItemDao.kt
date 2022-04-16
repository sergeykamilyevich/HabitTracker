package com.example.habittracker.data.db

import androidx.room.*
import com.example.habittracker.data.db.models.HabitItemDbModel
import com.example.habittracker.data.db.models.HabitItemWithDoneDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitItemDao {

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
    ): Flow<List<HabitItemWithDoneDbModel>>?

    @Transaction
    @Query("SELECT * FROM habit_items WHERE id = :habitItemId LIMIT 1")
    suspend fun getById(habitItemId: Int): HabitItemWithDoneDbModel?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(habitItemDbModel: HabitItemDbModel)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(habitItemDbModel: HabitItemDbModel)

    @Query("DELETE FROM habit_items WHERE id = :habitItemId")
    suspend fun delete(habitItemId: Int)


}