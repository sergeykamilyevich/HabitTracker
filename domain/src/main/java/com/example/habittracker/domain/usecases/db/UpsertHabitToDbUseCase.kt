package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.models.UpsertException
import com.example.habittracker.domain.repositories.HabitRepository
import com.example.habittracker.domain.models.HabitItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertHabitToDbUseCase @Inject constructor(val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItem: HabitItem): UpsertException? {
        return habitRepository.upsertHabitItem(habitItem)
    }
}