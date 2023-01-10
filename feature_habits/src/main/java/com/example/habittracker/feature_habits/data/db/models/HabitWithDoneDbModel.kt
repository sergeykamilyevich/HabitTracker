package com.example.habittracker.feature_habits.data.db.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.habittracker.core.domain.models.Habit
import com.example.habittracker.core.domain.models.HabitPriority

data class HabitWithDoneDbModel(
    @Embedded
    val habitDbModel: HabitDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val habitDoneDbModel: List<HabitDoneDbModel>
) {
    fun toHabit(): Habit {
        return Habit(
            name = habitDbModel.name,
            description = habitDbModel.description,
            priority = HabitPriority.findPriorityById(habitDbModel.priority),
            type = habitDbModel.type,
            color = habitDbModel.color,
            recurrenceNumber = habitDbModel.recurrenceNumber,
            recurrencePeriod = habitDbModel.recurrencePeriod,
            done = HabitDoneDbModel.toDoneList(habitDoneDbModel),
            uid = habitDbModel.apiUid,
            date = habitDbModel.date,
            id = habitDbModel.id
        )
    }

    companion object {

        fun toHabitList(list: List<HabitWithDoneDbModel>) = list.map {
            it.toHabit()
        }
    }
}