package com.example.habittracker.data.db.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.Time

data class HabitItemWithDoneDbModel(
    @Embedded
    val habitItemDbModel: HabitItemDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val habitDoneDbModel: List<HabitDoneDbModel>
) {
    fun toHabitItem(): HabitItem {
        val currentDate = Time().getCurrentUtcDateInInt()
        val upToDateHabitDoneDates = habitDoneDbModel.filter {
            habitItemDbModel.recurrencePeriod > (currentDate - it.date) / SECONDS_IN_DAY
        }
        return HabitItem(
            name = habitItemDbModel.name,
            description = habitItemDbModel.description,
            priority = HabitPriority.getPriorityByPosition(habitItemDbModel.priority),
            type = habitItemDbModel.type,
            color = habitItemDbModel.color,
            recurrenceNumber = habitItemDbModel.recurrenceNumber,
            recurrencePeriod = habitItemDbModel.recurrencePeriod,
            done = upToDateHabitDoneDates.size,
            apiUid = habitItemDbModel.apiUid,
            date = habitItemDbModel.date,
            id = habitItemDbModel.id
        )
    }

    companion object {

        const val SECONDS_IN_DAY = 24 * 60 * 60
        fun mapDbModelListToHabitList(list: List<HabitItemWithDoneDbModel>) = list.map {
            it.toHabitItem()
        }
    }
}