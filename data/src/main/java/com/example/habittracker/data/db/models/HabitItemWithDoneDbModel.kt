package com.example.habittracker.data.db.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.models.HabitPriority

data class HabitItemWithDoneDbModel(
    @Embedded
    val habitItemDbModel: HabitItemDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val habitDoneDbModel: List<HabitDoneDbModel>
) {
    fun toHabitItem() = HabitItem(
        name = habitItemDbModel.name,
        description = habitItemDbModel.description,
        priority = HabitPriority.getPriorityByPosition(habitItemDbModel.priority),
        type = habitItemDbModel.type,
        color = habitItemDbModel.color,
        recurrenceNumber = habitItemDbModel.recurrenceNumber,
        recurrencePeriod = habitItemDbModel.recurrencePeriod,
        id = habitItemDbModel.id,
        date = habitItemDbModel.date,
        doneDates = habitDoneDbModel.map {
            it.date
        }
    )

    companion object {
        fun mapDbModelListToHabitList(list: List<HabitItemWithDoneDbModel>) = list.map {
            it.toHabitItem()
        }
    }
}