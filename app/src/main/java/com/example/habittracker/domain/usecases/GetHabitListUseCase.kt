package com.example.habittracker.domain.usecases

import androidx.lifecycle.LiveData
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitListFilter
import com.example.habittracker.domain.HabitListRepository
import com.example.habittracker.domain.HabitType

class GetHabitListUseCase(private val habitListRepository: HabitListRepository) {

    operator fun invoke(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter
    ): LiveData<List<HabitItem>> {
        return habitListRepository.getList(habitTypeFilter, habitListFilter)
    }
}