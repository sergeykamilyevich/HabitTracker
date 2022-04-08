package com.example.habittracker.data.room

import android.app.Application
import com.example.habittracker.data.room.models.HabitDoneDbModel
import com.example.habittracker.data.room.models.HabitItemDbModel
import com.example.habittracker.data.room.models.HabitItemWithDoneDbModel
import com.example.habittracker.domain.models.HabitAlreadyExistsException
import com.example.habittracker.domain.repositories.HabitRepository
import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.models.HabitListFilter
import com.example.habittracker.domain.models.HabitType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomHabitRepository(application: Application) : HabitRepository {

    private val habitItemDao = AppDataBase.getInstance(application).habitItemDao()
    private val habitDoneDao = AppDataBase.getInstance(application).habitDoneDao()

    override fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<HabitItem>> {
        val habitTypeFilter =
            if (habitType == null) allHabitTypesToStringList()
            else listOf(habitType.toString())
        val listHabitItemWithDoneDbModel = habitItemDao.getList(
            habitTypeFilter,
            habitListFilter.orderBy.name,
            habitListFilter.search
        )
            ?: throw RuntimeException("List of habits is empty")
        return listHabitItemWithDoneDbModel.map {
            HabitItemWithDoneDbModel.mapDbModelListToHabitList(it)
        }
//        Transformations.map(listHabitItemWithDoneDbModel) {
//            HabitItemWithDoneDbModel.mapDbModelListToHabitList(it)
//        }
    }

    private fun allHabitTypesToStringList() = HabitType.values().map { it.name }

    override suspend fun getHabitById(habitItemId: Int): HabitItem {
        val habitItemWithDoneDbModel = habitItemDao.getById(habitItemId)
            ?: throw RuntimeException("Habit with id $habitItemId not found")
        return habitItemWithDoneDbModel.toHabitItem()
    }

    override suspend fun addHabitItem(habitItem: HabitItem): HabitAlreadyExistsException? { //TODO upsert
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

