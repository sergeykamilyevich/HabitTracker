package com.example.habittracker.db_api.domain.usecases

interface DeleteHabitDoneUseCase {

    suspend operator fun invoke(habitDoneId: Int)
}