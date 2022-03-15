package com.example.habittracker.data

import com.example.habittracker.domain.HabitItem

class HabitListMapper {

    fun mapEntityToDbModel(habitItem: HabitItem) = HabitItemDbModel(
        id = habitItem.id,
        name = habitItem.name,
        description = habitItem.description,
        priority = habitItem.priority,
        type = habitItem.type,
        color = habitItem.color,
        recurrenceNumber = habitItem.recurrenceNumber,
        recurrencePeriod = habitItem.recurrencePeriod
    )

    fun mapDbModelToEntity(habitItemDbModel: HabitItemDbModel) = HabitItem(
        name = habitItemDbModel.name,
        description = habitItemDbModel.description,
        priority = habitItemDbModel.priority,
        type = habitItemDbModel.type,
        color = habitItemDbModel.color,
        recurrenceNumber = habitItemDbModel.recurrenceNumber,
        recurrencePeriod = habitItemDbModel.recurrencePeriod,
        id = habitItemDbModel.id
    )

    fun mapListDbModelToEntity(list: List<HabitItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}