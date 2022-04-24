package com.example.habittracker.data.db.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.HabitWithDone
import com.example.habittracker.domain.models.Time

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
//        val currentDate = Time().currentUtcDateInSeconds()
//        val upToDateHabitDoneDates = habitDoneDbModel.filter {
//            habitDbModel.recurrencePeriod > (currentDate - it.date) / SECONDS_IN_DAY
//        }
        return Habit(
            name = habitDbModel.name,
            description = habitDbModel.description,
            priority = HabitPriority.getPriorityByPosition(habitDbModel.priority),
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

    fun toHabitWithDone(): HabitWithDone {
        val apiUid = habitDbModel.apiUid
        return HabitWithDone(
            habit = habitDbModel.toHabit(),
            habitDone = habitDoneDbModel.map {
                it.toHabitDone(habitUid = apiUid)
            }
        )
    }


    companion object {

        const val SECONDS_IN_DAY = 24 * 60 * 60
        fun toHabitList(list: List<HabitWithDoneDbModel>) = list.map {
            it.toHabit()
        }
    }
}