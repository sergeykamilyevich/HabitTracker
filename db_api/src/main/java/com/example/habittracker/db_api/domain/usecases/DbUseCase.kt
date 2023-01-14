package com.example.habittracker.db_api.domain.usecases

interface DbUseCase {

    val addHabitDoneUseCase: AddHabitDoneUseCase
    val deleteHabitDoneUseCase: DeleteHabitDoneUseCase
    val deleteHabitUseCase: DeleteHabitUseCase
    val getHabitUseCase: GetHabitUseCase
    val getHabitListUseCase: GetHabitListUseCase
    val getUnfilteredHabitListUseCase: GetUnfilteredHabitListUseCase
    val upsertHabitUseCase: UpsertHabitUseCase
    val deleteAllHabitsUseCase: DeleteAllHabitsUseCase
}
