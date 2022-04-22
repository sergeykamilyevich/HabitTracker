package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.models.HabitWithDone
import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUnfilteredHabitListFromDbUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(): List<HabitWithDone>? = dbHabitRepository.getUnfilteredList()
}