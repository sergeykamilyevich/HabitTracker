package com.example.habittracker.data.room.models

import androidx.room.*
import com.example.habittracker.domain.models.HabitDone

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
) {
    companion object {
        fun fromHabitDone(habitDone: HabitDone) = HabitDoneDbModel(
            id = habitDone.id,
            habitId = habitDone.habitId,
            date = habitDone.date
        )
    }
}
