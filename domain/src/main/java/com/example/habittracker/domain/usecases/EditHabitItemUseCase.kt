package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.models.HabitAlreadyExistsException
import com.example.habittracker.domain.repositories.HabitRepository
import com.example.habittracker.domain.models.HabitItem
import javax.inject.Inject

class EditHabitItemUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItem: HabitItem): HabitAlreadyExistsException? {
        return habitRepository.editHabitItem(habitItem)
    }
}