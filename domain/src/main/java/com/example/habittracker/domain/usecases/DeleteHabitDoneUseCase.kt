package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.repositories.HabitRepository
import javax.inject.Inject
import javax.inject.Singleton

class DeleteHabitDoneUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitDoneId: Int) {
        habitRepository.deleteHabitDone(habitDoneId)
    }
}