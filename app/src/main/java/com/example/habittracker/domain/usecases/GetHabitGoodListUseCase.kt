package com.example.habittracker.domain.usecases

import androidx.lifecycle.LiveData
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitListRepository

class GetHabitGoodListUseCase(private val habitListRepository: HabitListRepository) {

    operator fun invoke(): LiveData<List<HabitItem>> {
        return habitListRepository.getGoodList()
    }
}