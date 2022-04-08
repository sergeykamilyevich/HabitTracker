package com.example.habittracker.data.room

import androidx.room.Embedded
import androidx.room.Relation
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitPriority

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