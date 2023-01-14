package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.domain.usecases.DeleteHabitDoneUseCase
import javax.inject.Inject

class DeleteHabitDoneUseCaseImpl @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) : DeleteHabitDoneUseCase {

    override suspend operator fun invoke(habitDoneId: Int) {
        dbHabitRepository.deleteHabitDone(habitDoneId)
    }
}