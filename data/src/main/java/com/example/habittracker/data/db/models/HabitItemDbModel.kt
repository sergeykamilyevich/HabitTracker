package com.example.habittracker.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.HabitType

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
    var date: Int,
    @ColumnInfo(name = "uid")
    var apiUid: String
) {

    fun toHabitItem(): HabitItem  = HabitItem(
            name = name,
            description = description,
            priority = HabitPriority.getPriorityByPosition(priority),
            type = type,
            color = color,
            recurrenceNumber = recurrenceNumber,
            recurrencePeriod = recurrencePeriod,
            apiUid = apiUid,
            date = date,
            id = id
        )

    companion object {
        fun fromHabitItem(habitItem: HabitItem) = HabitItemDbModel(
            id = habitItem.id,
            name = habitItem.name,
            description = habitItem.description,
            priority = habitItem.priority.int,
            type = habitItem.type,
            color = habitItem.color,
            recurrenceNumber = habitItem.recurrenceNumber,
            recurrencePeriod = habitItem.recurrencePeriod,
            date = habitItem.date,
            apiUid = habitItem.apiUid
        )
    }
}