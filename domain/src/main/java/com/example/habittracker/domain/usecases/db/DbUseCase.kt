package com.example.habittracker.domain.usecases.db

import javax.inject.Inject

class DbUseCase @Inject constructor(
    val addHabitDoneToDbUseCase: AddHabitDoneToDbUseCase,
    val deleteHabitDoneFromDbUseCase: DeleteHabitDoneFromDbUseCase,
    val deleteHabitFromDbUseCase: DeleteHabitFromDbUseCase,
    val getHabitFromDbUseCase: GetHabitFromDbUseCase,
    val getHabitListFromDbUseCase: GetHabitListFromDbUseCase,
    val upsertHabitToDbUseCase: UpsertHabitToDbUseCase
)