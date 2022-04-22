package com.example.habittracker.domain.models

data class HabitWithDone(
    val habit: Habit,
    val habitDone: List<HabitDone>
) {

//    fun toHabitWithDoneWithoutApiUid(): HabitWithDone =
//        HabitWithDone(
//            habit = habit.copy(apiUid = EMPTY_UID),
//            habitDone = habitDone
//        )

//    companion object {
//        const val EMPTY_UID = ""
//    }
}
