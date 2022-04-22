package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostHabitDoneToCloudUseCase @Inject constructor(
    private val networkHabitRepository: NetworkHabitRepository
    ) {

    suspend operator fun invoke(habitDone: HabitDone): String? {
        return networkHabitRepository.postHabitDone(habitDone)
    }
}