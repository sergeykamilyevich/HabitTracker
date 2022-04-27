package com.example.habittracker.domain.usecases.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudUseCase @Inject constructor(
    val deleteAllHabitsFromCloudUseCase: DeleteAllHabitsFromCloudUseCase,
    val deleteHabitFromCloudUseCase: DeleteHabitFromCloudUseCase,
    val getHabitListFromCloudUseCase: GetHabitListFromCloudUseCase,
    val postHabitDoneToCloudUseCase: PostHabitDoneToCloudUseCase,
    val getCloudErrorUseCase: GetCloudErrorUseCase
)