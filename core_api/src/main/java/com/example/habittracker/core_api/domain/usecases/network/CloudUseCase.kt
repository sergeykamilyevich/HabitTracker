package com.example.habittracker.core_api.domain.usecases.network

import com.example.habittracker.core_api.domain.repositories.CloudHabitRepository
import javax.inject.Inject

class CloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository,
    val deleteAllHabitsFromCloudUseCase: DeleteAllHabitsFromCloudUseCase
    = DeleteAllHabitsFromCloudUseCase(cloudHabitRepository),
    val deleteHabitFromCloudUseCase: DeleteHabitFromCloudUseCase
    = DeleteHabitFromCloudUseCase(cloudHabitRepository),
    val getHabitListFromCloudUseCase: GetHabitListFromCloudUseCase
    = GetHabitListFromCloudUseCase(cloudHabitRepository),
    val postHabitDoneToCloudUseCase: PostHabitDoneToCloudUseCase
    = PostHabitDoneToCloudUseCase(cloudHabitRepository),
    val getCloudErrorUseCase: GetCloudErrorUseCase
)