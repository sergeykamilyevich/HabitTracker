package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject

class DeleteHabitDoneUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(habitDoneId: Int) {
        dbHabitRepository.deleteHabitDone(habitDoneId)
    }
}