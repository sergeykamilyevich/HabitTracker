package com.example.habittracker.domain.usecases.db

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbUseCase @Inject constructor(
    val addHabitDoneUseCase: AddHabitDoneUseCase,
    val deleteHabitDoneUseCase: DeleteHabitDoneUseCase,
    val deleteHabitUseCase: DeleteHabitUseCase,
    val getHabitUseCase: GetHabitUseCase,
    val getHabitListUseCase: GetHabitListUseCase,
    val getUnfilteredHabitListUseCase: GetUnfilteredHabitListUseCase,
    val upsertHabitUseCase: UpsertHabitUseCase,
    val deleteAllHabitsUseCase: DeleteAllHabitsUseCase
)