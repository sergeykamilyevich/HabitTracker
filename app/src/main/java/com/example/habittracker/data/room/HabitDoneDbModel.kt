package com.example.habittracker.data.room

import androidx.room.*

@Entity(
    tableName = "habit_done",
    indices = [Index("date")],
    foreignKeys = [
        ForeignKey(
            entity = HabitItemDbModel::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class HabitDoneDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "habit_id")
    val habitId: Int,
    val date: Int
)
