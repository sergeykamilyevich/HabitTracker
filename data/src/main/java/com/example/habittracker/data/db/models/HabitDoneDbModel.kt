package com.example.habittracker.data.db.models

import androidx.room.*
import com.example.habittracker.domain.models.HabitDone

@Entity(
    tableName = "habit_done",
    indices = [Index("date")],
    foreignKeys = [
        ForeignKey(
            entity = HabitDbModel::class,
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

    fun toHabitDone(habitUid: String = EMPTY_STRING) = HabitDone(
        habitId = habitId,
        date = date,
        habitUid = habitUid,
        id = id,
    )

    companion object {

        private const val EMPTY_STRING = ""

        fun fromHabitDone(habitDone: HabitDone) = HabitDoneDbModel(
            id = habitDone.id,
            habitId = habitDone.habitId,
            date = habitDone.date
        )
    }
}
