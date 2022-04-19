package com.example.habittracker.domain.usecases.db

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbUseCase @Inject constructor(
    val addHabitDoneToDbUseCase: AddHabitDoneToDbUseCase,
    val deleteHabitDoneFromDbUseCase: DeleteHabitDoneFromDbUseCase,
    val deleteHabitFromDbUseCase: DeleteHabitFromDbUseCase,
    val getHabitFromDbUseCase: GetHabitFromDbUseCase,
    val getHabitListFromDbUseCase: GetHabitListFromDbUseCase,
    val upsertHabitToDbUseCase: UpsertHabitToDbUseCase
)