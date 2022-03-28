package com.example.habittracker.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.habittracker.data.room.AppDataBase
import com.example.habittracker.data.room.HabitListMapper
import com.example.habittracker.domain.*
import java.lang.RuntimeException

class HabitListRepositoryImpl(application: Application) : HabitListRepository {

    private val habitListDao = AppDataBase.getInstance(application).habitListDao()
    private val mapper = HabitListMapper()

    override fun getList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): LiveData<List<HabitItem>> {
        val habitTypeFilter =
            if (habitType == null) allHabitTypesToStringList()
            else listOf(habitType.toString())
        val listHabitItemDbModel = habitListDao.getList(
            habitTypeFilter,
            habitListFilter.orderBy.name,
            habitListFilter.search
        )
            ?: throw RuntimeException("List of habits is empty")
        return Transformations.map(listHabitItemDbModel) {
            mapper.mapListDbModelToEntity(it)
        }
    }

    private fun allHabitTypesToStringList() = HabitType.values().map { it.name }

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