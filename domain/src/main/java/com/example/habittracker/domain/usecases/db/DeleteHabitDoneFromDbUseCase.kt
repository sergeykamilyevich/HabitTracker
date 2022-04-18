package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.repositories.HabitRepository
import javax.inject.Inject
import javax.inject.Singleton

class DeleteHabitDoneFromDbUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitDoneId: Int) {
        habitRepository.deleteHabitDone(habitDoneId)
    }
}