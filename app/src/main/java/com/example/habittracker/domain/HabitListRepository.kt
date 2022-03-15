package com.example.habittracker.domain

import androidx.lifecycle.LiveData

interface HabitListRepository {

    fun getList(): LiveData<List<HabitItem>>

    suspend fun getById(habitItemId: Int): HabitItem

    suspend fun add(habitItem: HabitItem)

    suspend fun delete(habitItem: HabitItem)

    suspend fun edit(habitItem: HabitItem)
}