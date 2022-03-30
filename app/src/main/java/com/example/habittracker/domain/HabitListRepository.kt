package com.example.habittracker.domain

import androidx.lifecycle.LiveData
import com.example.habittracker.data.room.HabitAlreadyExistsException
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitListFilter
import com.example.habittracker.domain.entities.HabitType
import java.time.Instant

interface HabitListRepository {

    fun getList(habitType: HabitType?, habitListFilter: HabitListFilter): LiveData<List<HabitItem>>

    suspend fun getById(habitItemId: Int): HabitItem

    suspend fun add(habitItem: HabitItem): HabitAlreadyExistsException?

    suspend fun delete(habitItem: HabitItem)

    suspend fun edit(habitItem: HabitItem): HabitAlreadyExistsException?
}