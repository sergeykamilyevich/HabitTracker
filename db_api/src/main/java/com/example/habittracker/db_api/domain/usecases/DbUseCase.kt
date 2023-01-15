package com.example.habittracker.db_api.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitDone
import com.example.habittracker.core_api.domain.models.HabitListFilter
import com.example.habittracker.core_api.domain.models.HabitType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DbUseCase @Inject constructor(
    val addHabitDoneUseCase: AddHabitDoneUseCase,
    val deleteAllHabitsUseCase: DeleteAllHabitsUseCase,
    val deleteHabitDoneUseCase: DeleteHabitDoneUseCase,
    val deleteHabitUseCase: DeleteHabitUseCase,
    val getHabitListUseCase: GetHabitListUseCase,
    val getHabitUseCase: GetHabitUseCase,
    val getUnfilteredHabitListUseCase: GetUnfilteredHabitListUseCase,
    val upsertHabitUseCase: UpsertHabitUseCase
)

fun interface AddHabitDoneUseCase : suspend (HabitDone) -> Either<IoError, Int>

fun interface DeleteAllHabitsUseCase : suspend () -> Either<IoError, Unit>

fun interface DeleteHabitDoneUseCase : suspend (Int) -> Unit

fun interface DeleteHabitUseCase : suspend (Habit) -> Either<IoError, Unit>

fun interface GetHabitListUseCase : (HabitType?, HabitListFilter) -> Flow<List<Habit>>

fun interface GetHabitUseCase : suspend (Int) -> Either<IoError, Habit>

fun interface GetUnfilteredHabitListUseCase : suspend () -> Either<IoError, List<Habit>>

fun interface UpsertHabitUseCase : suspend (Habit) -> Either<IoError, Int>