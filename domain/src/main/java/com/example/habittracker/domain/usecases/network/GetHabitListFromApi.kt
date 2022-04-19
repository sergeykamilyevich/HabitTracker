package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHabitListFromApi @Inject constructor(private val dbHabitRepository: DbHabitRepository) {

    suspend operator fun invoke(habitItemId: Int): HabitItem {
        return dbHabitRepository.getHabitById(habitItemId)
    }
}