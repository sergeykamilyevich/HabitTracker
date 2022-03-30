package com.example.habittracker.domain.usecases

import androidx.lifecycle.LiveData
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitListFilter
import com.example.habittracker.domain.HabitListRepository
import com.example.habittracker.domain.entities.HabitType

class GetHabitListUseCase(private val habitListRepository: HabitListRepository) {

    operator fun invoke(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter
    ): LiveData<List<HabitItem>> {
        return habitListRepository.getList(habitTypeFilter, habitListFilter)
    }
}