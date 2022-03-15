package com.example.habittracker.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitListRepository
import java.lang.RuntimeException

class HabitListRepositoryImpl(application: Application) : HabitListRepository {

    private val habitListDao = AppDataBase.getInstance(application).habitListDao()
    private val mapper = HabitListMapper()

    override fun getList(): LiveData<List<HabitItem>> {
        val listHabitItemDbModel = habitListDao.getList()
            ?: throw RuntimeException("List of habits is empty")
        return Transformations.map(listHabitItemDbModel) {
            mapper.mapListDbModelToEntity(it)
        }
    }

    override suspend fun getById(habitItemId: Int): HabitItem {
        val habitItemDbModel = habitListDao.getById(habitItemId)
            ?: throw RuntimeException("Habit with id $habitItemId not found")
        return mapper.mapDbModelToEntity(habitItemDbModel)
    }

    override suspend fun add(habitItem: HabitItem) {
        val habitItemDbModel = mapper.mapEntityToDbModel(habitItem)
        habitListDao.add(habitItemDbModel)
    }

    override suspend fun delete(habitItem: HabitItem) {
        habitListDao.delete(habitItem.id)
    }

    override suspend fun edit(habitItem: HabitItem) {
        val habitItemDbModel = mapper.mapEntityToDbModel(habitItem)
        habitListDao.edit(habitItemDbModel)
    }
}