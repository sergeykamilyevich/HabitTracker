package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHabitListFromApiUseCase @Inject constructor(
    private val networkHabitRepository: NetworkHabitRepository
) {

    suspend operator fun invoke(): List<HabitItem>? {
        return networkHabitRepository.getHabitList()
    }
}