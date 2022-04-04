package com.example.habittracker.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitPriority
import com.example.habittracker.domain.entities.HabitType

@Entity(
    tableName = "habit_items",
    indices = [Index("name", unique = true)]
)
data class HabitItemDbModel(
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
    val date: Int
) {
    companion object {
        fun fromHabitItem(habitItem: HabitItem) = HabitItemDbModel(
            id = habitItem.id,
            name = habitItem.name,
            description = habitItem.description,
            priority = habitItem.priority.id,
            type = habitItem.type,
            color = habitItem.color,
            recurrenceNumber = habitItem.recurrenceNumber,
            recurrencePeriod = habitItem.recurrencePeriod,
            date = habitItem.date
        )
    }
}