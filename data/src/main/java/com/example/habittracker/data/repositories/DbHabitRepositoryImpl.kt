package com.example.habittracker.data.repositories

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.example.habittracker.data.db.room.AppDataBase
import com.example.habittracker.data.db.models.HabitDbModel
import com.example.habittracker.data.db.models.HabitDoneDbModel
import com.example.habittracker.data.db.models.HabitWithDoneDbModel
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.repositories.DbHabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbHabitRepositoryImpl @Inject constructor(application: Application) : DbHabitRepository {

    private val habitItemDao = AppDataBase.getInstance(application).habitItemDao()
    private val habitDoneDao = AppDataBase.getInstance(application).habitDoneDao()

    override fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>> {
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
            HabitWithDoneDbModel.mapDbModelListToHabitList(it)
        }
    }

    override suspend fun getUnfilteredList(): List<HabitWithDone>? =
        habitItemDao.getUnfilteredList()?.map {
            it.toHabitWithDone()
        }

    private fun allHabitTypesToStringList() = HabitType.values().map { it.name }

    override suspend fun getHabitById(habitItemId: Int): Habit {
        val habitItemWithDoneDbModel = habitItemDao.getById(habitItemId)
            ?: throw RuntimeException("Habit with id $habitItemId not found")
        return habitItemWithDoneDbModel.toHabitItem()
    }

    override suspend fun upsertHabit(habit: Habit): Either<DbException, Int> {
        val habitItemDbModel = HabitDbModel.fromHabitItem(habit)
        var result: Either<DbException, Int> =
            SqlException(DEFAULT_SQL_ERROR).failure()
        runCatching {
            habitItemDao.insert(habitItemDbModel).toInt()
        }
            .onSuccess {
                return it.success()
            }
            .onFailure {
                result = handleInsertException(it, habit, habitItemDbModel)
            }
        return result
    }

    private suspend fun handleInsertException(
        insertException: Throwable,
        habit: Habit,
        habitDbModel: HabitDbModel
    ): Either<DbException, Int> {
        val insertExceptionMessage = insertException.message
        var result: Either<DbException, Int> =
            SqlException(insertExceptionMessage ?: "").failure()
        if (insertException is SQLiteConstraintException && insertExceptionMessage != null) {
            when {
                insertExceptionMessage.contains(UNIQUE_CONSTRAINT_MESSAGE) -> {
                    result = HabitAlreadyExistsException(habit.name).failure()
                }
                insertExceptionMessage.contains(PRIMARY_KEY_CONSTRAINT_MESSAGE) -> {
                    runCatching {
                        habitItemDao.update(habitDbModel)
                    }
                        .onSuccess {
                            result = ITEM_NOT_ADDED.success()
                    }
                        .onFailure { updateException ->
                            result = handleUpdateException(updateException, habit.name).failure()
                        }
                }
            }
        }
        return result
    }

    private fun handleUpdateException(
        updateException: Throwable,
        habitItemName: String
    ): DbException {
        val updateExceptionMessage = updateException.message
        return if (updateExceptionMessage?.contains(UNIQUE_CONSTRAINT_MESSAGE) == true) {
            HabitAlreadyExistsException(habitItemName)
        } else {
            SqlException(updateException.toString())
        }
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitItemDao.delete(habit.id)
    }

    override suspend fun deleteAllHabits() {
        val habitList = habitItemDao.getUnfilteredList()
        habitList?.forEach {
            deleteHabit(it.toHabitItem())
        }
    }

    override suspend fun addHabitDone(habitDone: HabitDone): Int {
        val habitDoneDbModel = HabitDoneDbModel.fromHabitDone(habitDone)
        return habitDoneDao.add(habitDoneDbModel).toInt()
    }

    override suspend fun deleteHabitDone(habitDoneId: Int) {
        habitDoneDao.delete(habitDoneId)
    }

    companion object {
        const val UNIQUE_CONSTRAINT_MESSAGE = "SQLITE_CONSTRAINT_UNIQUE"
        const val PRIMARY_KEY_CONSTRAINT_MESSAGE = "SQLITE_CONSTRAINT_PRIMARYKEY"
        const val DEFAULT_SQL_ERROR = "Unknown SQL error"
        const val ITEM_NOT_ADDED = 0
    }

}

