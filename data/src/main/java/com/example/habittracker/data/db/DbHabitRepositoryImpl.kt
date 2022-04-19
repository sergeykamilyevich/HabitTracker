package com.example.habittracker.data.db

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import com.example.habittracker.data.db.models.HabitDoneDbModel
import com.example.habittracker.data.db.models.HabitItemDbModel
import com.example.habittracker.data.db.models.HabitItemWithDoneDbModel
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
    }

    private fun allHabitTypesToStringList() = HabitType.values().map { it.name }

    override suspend fun getHabitById(habitItemId: Int): HabitItem {
        val habitItemWithDoneDbModel = habitItemDao.getById(habitItemId)
            ?: throw RuntimeException("Habit with id $habitItemId not found")
        return habitItemWithDoneDbModel.toHabitItem()
    }

    override suspend fun upsertHabit(habitItem: HabitItem): Either<UpsertException, Int> {
        val habitItemDbModel = HabitItemDbModel.fromHabitItem(habitItem)
        var result: Either<UpsertException, Int> =
            UnknownSqlException(DEFAULT_SQL_ERROR).failure()
        runCatching {
            habitItemDao.insert(habitItemDbModel).toInt()
        }
            .onSuccess {
                return it.success()
            }
            .onFailure {
//                return it.failure()
                result = handleInsertException(it, habitItem, habitItemDbModel)
            }
        return result
    }

    private suspend fun handleInsertException(
        insertException: Throwable,
        habitItem: HabitItem,
        habitItemDbModel: HabitItemDbModel
    ): Either<UpsertException, Int> {
        var result: Either<UpsertException, Int> =
            UnknownSqlException(insertException.toString()).failure()
        val insertExceptionMessage = insertException.message
        if (insertException is SQLiteConstraintException && insertExceptionMessage != null) {
            when {
                insertExceptionMessage.contains(UNIQUE_CONSTRAINT_MESSAGE) -> {
                    result = HabitAlreadyExistsException(habitItem.name).failure()
                }
                insertExceptionMessage.contains(PRIMARY_KEY_CONSTRAINT_MESSAGE) -> {
                    runCatching { habitItemDao.update(habitItemDbModel) }
                        .onSuccess {
                            result = ITEM_NOT_ADDED.success()
                        }
                        .onFailure { updateException ->
                            result = handleUpdateException(
                                updateException,
                                habitItem.name
                            ).failure()
                        }
                }
            }
        }
        return result
    }

    private fun handleUpdateException(
        updateException: Throwable,
        habitItemName: String
    ): UpsertException {
        val updateExceptionMessage = updateException.message
        return if (updateExceptionMessage?.contains(UNIQUE_CONSTRAINT_MESSAGE) == true) {
            HabitAlreadyExistsException(habitItemName)
        } else {
            UnknownSqlException(updateException.toString())
        }
    }

    override suspend fun deleteHabit(habitItem: HabitItem) {
        habitItemDao.delete(habitItem.id)
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

