package com.example.habittracker.network_api.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitDone
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CloudUseCase @Inject constructor(
    val deleteAllHabitsFromCloudUseCase: DeleteAllHabitsFromCloudUseCase,
    val deleteHabitFromCloudUseCase: DeleteHabitFromCloudUseCase,
    val getHabitListFromCloudUseCase: GetHabitListFromCloudUseCase,
    val postHabitDoneToCloudUseCase: PostHabitDoneToCloudUseCase,
    val getCloudErrorUseCase: GetCloudErrorUseCase
)

fun interface DeleteAllHabitsFromCloudUseCase : suspend () -> Either<IoError, Unit>

fun interface DeleteHabitFromCloudUseCase : suspend (Habit) -> Either<IoError, Unit>

fun interface GetHabitListFromCloudUseCase : suspend () -> Either<IoError, List<Habit>>

fun interface PostHabitDoneToCloudUseCase : suspend (HabitDone) -> Either<IoError, Unit>

fun interface GetCloudErrorUseCase : () -> Flow<Either<IoError, Unit>>