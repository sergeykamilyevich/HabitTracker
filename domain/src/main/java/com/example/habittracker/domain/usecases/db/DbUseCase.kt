package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

class DbUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository,
    val addHabitDoneUseCase: AddHabitDoneUseCase = AddHabitDoneUseCase(dbHabitRepository),
    val deleteHabitDoneUseCase: DeleteHabitDoneUseCase = DeleteHabitDoneUseCase(dbHabitRepository),
    val deleteHabitUseCase: DeleteHabitUseCase = DeleteHabitUseCase(dbHabitRepository),
    val getHabitUseCase: GetHabitUseCase = GetHabitUseCase(dbHabitRepository),
    val getHabitListUseCase: GetHabitListUseCase = GetHabitListUseCase(dbHabitRepository),
    val getUnfilteredHabitListUseCase: GetUnfilteredHabitListUseCase
    = GetUnfilteredHabitListUseCase(dbHabitRepository),
    val upsertHabitUseCase: UpsertHabitUseCase = UpsertHabitUseCase(dbHabitRepository),
    val deleteAllHabitsUseCase: DeleteAllHabitsUseCase = DeleteAllHabitsUseCase(dbHabitRepository)
)