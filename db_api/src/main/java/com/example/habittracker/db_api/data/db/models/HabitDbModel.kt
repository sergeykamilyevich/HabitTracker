package com.example.habittracker.db_api.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitPriority
import com.example.habittracker.core_api.domain.models.HabitType

@Entity(
    tableName = "habit_items",
    indices = [Index("name", unique = true)]
)
data class HabitDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var description: String,
    var priority: Int,
    var type: HabitType,
    var color: Int,
    @ColumnInfo(name = "recurrence_number")
    var recurrenceNumber: Int,
    @ColumnInfo(name = "recurrence_period")
    var recurrencePeriod: Int,
    var date: Int,
    @ColumnInfo(name = "uid")
    var apiUid: String
) {

    fun toHabit(): Habit = Habit(
        name = name,
        description = description,
        priority = HabitPriority.findPriorityById(priority),
        type = type,
        color = color,
        recurrenceNumber = recurrenceNumber,
        recurrencePeriod = recurrencePeriod,
        uid = apiUid,
        date = date,
        id = id
    )

    companion object {
        fun fromHabitItem(habit: Habit) = HabitDbModel(
            id = habit.id,
            name = habit.name,
            description = habit.description,
            priority = habit.priority.id,
            type = habit.type,
            color = habit.color,
            recurrenceNumber = habit.recurrenceNumber,
            recurrencePeriod = habit.recurrencePeriod,
            date = habit.date,
            apiUid = habit.uid
        )
    }
}