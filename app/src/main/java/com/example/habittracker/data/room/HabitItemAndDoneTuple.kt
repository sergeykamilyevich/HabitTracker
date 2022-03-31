package com.example.habittracker.data.room

import androidx.room.Embedded
import androidx.room.Relation

data class HabitItemAndDoneTuple(
    @Embedded
    val habitItemDbModel: HabitItemDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val habitDoneDbModel: HabitDoneDbModel
) {


}