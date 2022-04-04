package com.example.habittracker.data.room

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.habittracker.data.HabitAlreadyExistsException
import com.example.habittracker.domain.HabitRepository
import com.example.habittracker.domain.entities.HabitDone
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitListFilter
import com.example.habittracker.domain.entities.HabitType

class RoomHabitRepository(application: Application) : HabitRepository {

    private val habitItemDao = AppDataBase.getInstance(application).habitItemDao()
    private val habitDoneDao = AppDataBase.getInstance(application).habitDoneDao()

    override fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): LiveData<List<HabitItem>> {
        val habitTypeFilter =
            if (habitType == null) allHabitTypesToStringList()
            else listOf(habitType.toString())
        val listHabitItemWithDoneDbModel = habitItemDao.getList(
            habitTypeFilter,
            habitListFilter.orderBy.name,
            habitListFilter.search
        )
            ?: throw RuntimeException("List of habits is empty")
        return Transformations.map(listHabitItemWithDoneDbModel) {
            HabitItemWithDoneDbModel.mapDbModelListToHabitList(it)
        }
    }

    private fun allHabitTypesToStringList() = HabitType.values().map { it.name }

    override suspend fun getHabitById(habitItemId: Int): HabitItem {
        val habitItemWithDoneDbModel = habitItemDao.getById(habitItemId)
            ?: throw RuntimeException("Habit with id $habitItemId not found")
        return habitItemWithDoneDbModel.toHabitItem()
    }

    override suspend fun addHabitItem(habitItem: HabitItem): HabitAlreadyExistsException? {
        val habitItemDbModel = HabitItemDbModel.fromHabitItem(habitItem)
        runCatching {
            habitItemDao.add(habitItemDbModel)
        }
            .onFailure {
                return HabitAlreadyExistsException(habitItem.name)
            }
        return null
    }

    override suspend fun deleteHabitItem(habitItem: HabitItem) {
        habitItemDao.delete(habitItem.id)
    }

    override suspend fun editHabitItem(habitItem: HabitItem): HabitAlreadyExistsException? {
        val habitItemDbModel = HabitItemDbModel.fromHabitItem(habitItem)
        runCatching {
            habitItemDao.edit(habitItemDbModel)
        }
            .onFailure {
                return HabitAlreadyExistsException(habitItem.name)
            }
        return null
    }

    override suspend fun addHabitDone(habitDone: HabitDone): Int {
        val habitDoneDbModel = HabitDoneDbModel.fromHabitDone(habitDone)
        return habitDoneDao.add(habitDoneDbModel).toInt()
    }

    override suspend fun deleteHabitDone(habitDoneId: Int) {
        habitDoneDao.delete(habitDoneId)
    }
}

