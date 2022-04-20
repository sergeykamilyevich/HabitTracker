package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.models.UpsertException
import com.example.habittracker.domain.repositories.DbHabitRepository
import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

class DeleteHabitFromApiUseCase @Inject constructor( //TODO rename Api to Cloud
    private val networkHabitRepository: NetworkHabitRepository
    ) {

    suspend operator fun invoke(habitItem: HabitItem) {
        return networkHabitRepository.deleteHabit(habitItem)
    }
}