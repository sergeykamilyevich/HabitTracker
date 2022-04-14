package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.repositories.HabitRepository
import javax.inject.Inject

class DeleteHabitDoneUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitDoneId: Int) {
        habitRepository.deleteHabitDone(habitDoneId)
    }
}