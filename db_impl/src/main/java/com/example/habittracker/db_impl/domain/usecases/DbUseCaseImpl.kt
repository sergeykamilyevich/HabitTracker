package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.domain.usecases.*
import javax.inject.Inject

class DbUseCaseImpl @Inject constructor(
    override val addHabitDoneUseCase: AddHabitDoneUseCase,
    override val deleteHabitDoneUseCase: DeleteHabitDoneUseCase,
    override val deleteHabitUseCase: DeleteHabitUseCase,
    override val getHabitUseCase: GetHabitUseCase,
    override val getHabitListUseCase: GetHabitListUseCase,
    override val getUnfilteredHabitListUseCase: GetUnfilteredHabitListUseCase,
    override val upsertHabitUseCase: UpsertHabitUseCase,
    override val deleteAllHabitsUseCase: DeleteAllHabitsUseCase
) : DbUseCase