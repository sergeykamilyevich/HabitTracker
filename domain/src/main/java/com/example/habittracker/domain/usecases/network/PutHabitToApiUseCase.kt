package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.models.UpsertException
import com.example.habittracker.domain.repositories.DbHabitRepository
import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutHabitToApiUseCase @Inject constructor(
    private val networkHabitRepository: NetworkHabitRepository
    ) {

    suspend operator fun invoke(habitItem: HabitItem): String? {
        return networkHabitRepository.putHabit(habitItem)
    }
}