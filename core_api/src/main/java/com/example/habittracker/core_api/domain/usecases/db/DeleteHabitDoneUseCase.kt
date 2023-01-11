package com.example.habittracker.core_api.domain.usecases.db

import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import javax.inject.Inject

class DeleteHabitDoneUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(habitDoneId: Int) {
        dbHabitRepository.deleteHabitDone(habitDoneId)
    }
}