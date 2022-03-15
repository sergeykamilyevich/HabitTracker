package com.example.habittracker.domain.usecases

import androidx.lifecycle.LiveData
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitListRepository

class GetHabitListUseCase(private val habitListRepository: HabitListRepository) {

    operator fun invoke(): LiveData<List<HabitItem>> {
        return habitListRepository.getList()
    }
}