package com.example.habittracker.data.room

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.habittracker.domain.HabitRepository
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitListFilter
import com.example.habittracker.domain.entities.HabitType

class HabitRoomRepositoryImpl(application: Application) : HabitRepository {

    private val habitListDao = AppDataBase.getInstance(application).habitListDao()
    private val mapper = HabitMapper()

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
            mapper.mapDbModelListToHabitList(it)
        }
    }

    private fun allHabitTypesToStringList() = HabitType.values().map { it.name }

    override suspend fun getById(habitItemId: Int): HabitItem {
        val habitItemDbModel = habitListDao.getById(habitItemId)
            ?: throw RuntimeException("Habit with id $habitItemId not found")
        return mapper.mapDbModelToHabitItem(habitItemDbModel)
    }

    override suspend fun add(habitItem: HabitItem): HabitAlreadyExistsException? {
        val habitItemDbModel = mapper.mapHabitItemToDbModel(habitItem)
        runCatching {
            habitListDao.add(habitItemDbModel)
        }
            .onFailure {
                return HabitAlreadyExistsException(habitItem.name)
            }
        return null
    }

    override suspend fun delete(habitItem: HabitItem) {
        habitListDao.delete(habitItem.id)
    }

    override suspend fun edit(habitItem: HabitItem): HabitAlreadyExistsException? {
        val habitItemDbModel = mapper.mapHabitItemToDbModel(habitItem)
        runCatching {
            habitListDao.edit(habitItemDbModel)
        }
            .onFailure {
                return HabitAlreadyExistsException(habitItem.name)
            }
        return null
    }
}

class HabitAlreadyExistsException(var name: String) :
    Exception("A habit with name $name already exists")