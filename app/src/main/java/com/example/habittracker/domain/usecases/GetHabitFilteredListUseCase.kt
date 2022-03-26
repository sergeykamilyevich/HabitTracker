package com.example.habittracker.domain.usecases

import androidx.lifecycle.LiveData
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitListRepository
import com.example.habittracker.domain.HabitType

class GetHabitFilteredListUseCase(private val habitListRepository: HabitListRepository) {

    operator fun invoke(habitType: HabitType?): LiveData<List<HabitItem>> {
        return habitListRepository.getFilteredList(habitType)
    }
}