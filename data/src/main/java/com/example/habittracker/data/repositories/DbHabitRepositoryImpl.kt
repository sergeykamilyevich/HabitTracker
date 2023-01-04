package com.example.habittracker.data.repositories

import com.example.habittracker.data.db.models.HabitDbModel
import com.example.habittracker.data.db.models.HabitDoneDbModel
import com.example.habittracker.data.db.models.HabitWithDoneDbModel
import com.example.habittracker.data.db.room.HabitDao
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.IoError.*
import com.example.habittracker.domain.errors.failure
import com.example.habittracker.domain.errors.success
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.models.HabitListFilter
import com.example.habittracker.domain.models.HabitType
import com.example.habittracker.domain.repositories.DbHabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//@Singleton
class DbHabitRepositoryImpl @Inject constructor(private val habitDao: HabitDao) :
    DbHabitRepository {

    override fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>> {
        val habitTypeFilter =
            if (habitType == null) allHabitTypesToStringList()
            else listOf(habitType.toString())
        val listHabitWithDoneDbModel = habitDao.getList(
            habitTypeFilter,
            habitListFilter.orderBy.name,
            habitListFilter.search
        ) ?: throw RuntimeException("List of habits is empty")
        return listHabitWithDoneDbModel.map { HabitWithDoneDbModel.toHabitList(it) }
    }

    override suspend fun getUnfilteredList(): Either<IoError, List<Habit>> {
        val list = habitDao.getUnfilteredList()
        list?.let {
            return it.map { habitWithDoneDbModel ->
                habitWithDoneDbModel.toHabit()
            }.success()
        }
        return SqlError("Habit list wasn't received").failure()
    }

    private fun allHabitTypesToStringList() = HabitType.values().map { it.name }

    override suspend fun getHabitById(habitId: Int): Either<IoError, Habit> {
        val habitItemWithDoneDbModel = habitDao.getHabitById(habitId)
        habitItemWithDoneDbModel?.let {
            return habitItemWithDoneDbModel.toHabit().success()
        }
        return SqlError("Habit with id $habitId not found").failure()


    }

    override suspend fun upsertHabit(habit: Habit): Either<IoError, Int> {
        val habitItemDbModel = HabitDbModel.fromHabitItem(habit)
        var result: Either<IoError, Int> =
            SqlError().failure()
        runCatching {
            habitDao.insertHabit(habitItemDbModel).toInt()
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
    ): Either<IoError, Int> {
        val insertExceptionMessage = insertException.message
        var result: Either<IoError, Int> =
            SqlError(insertExceptionMessage ?: DEFAULT_SQL_ERROR).failure()
        if (insertExceptionMessage != null) {
            when {
                insertExceptionMessage.contains(UNIQUE_CONSTRAINT_MESSAGE) -> {
                    result = HabitAlreadyExistsError(habit.name).failure()
                }
                insertExceptionMessage.contains(PRIMARY_KEY_CONSTRAINT_MESSAGE) -> {
                    runCatching {
                        habitDao.updateHabit(habitDbModel)
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
    ): IoError {
        val updateExceptionMessage = updateException.message
        return if (updateExceptionMessage?.contains(UNIQUE_CONSTRAINT_MESSAGE) == true) {
            HabitAlreadyExistsError(habitItemName)
        } else {
            SqlError(updateException.message ?: DEFAULT_SQL_ERROR)
        }
    }

    override suspend fun deleteHabit(habit: Habit): Either<IoError, Unit> {
        val habitId = habit.id
        habitDao.deleteHabit(habit.id)
        val result = getHabitById(habitId)
        return when (result) {
            is Success -> DeletingHabitError().failure()
            is Failure -> Unit.success()
        }
    }

    override suspend fun deleteAllHabits(): Either<IoError, Unit> {
        val habitListBefore = habitDao.getUnfilteredList()
        habitListBefore?.forEach {
            deleteHabit(it.toHabit())
        }
        val habitListAfter = habitDao.getUnfilteredList()
        habitListAfter?.let {
            if (habitListAfter.isEmpty()) return Unit.success()
        }
        return DeletingAllHabitsError().failure()
    }

    override suspend fun addHabitDone(habitDone: HabitDone): Either<IoError, Int> {
        val habitDoneDbModel = HabitDoneDbModel.fromHabitDone(habitDone)
        var result: Either<IoError, Int> = SqlError().failure()
        runCatching {
            habitDao.insertHabitDone(habitDoneDbModel).toInt()
        }
            .onSuccess { result = it.success() }
            .onFailure { result = SqlError(it.message ?: DEFAULT_SQL_ERROR).failure() }
        return result
    }

    override suspend fun deleteHabitDone(habitDoneId: Int) {
        habitDao.deleteHabitDone(habitDoneId)
    }

    companion object {
        const val UNIQUE_CONSTRAINT_MESSAGE = "SQLITE_CONSTRAINT_UNIQUE"
        const val PRIMARY_KEY_CONSTRAINT_MESSAGE = "SQLITE_CONSTRAINT_PRIMARYKEY"
        const val DEFAULT_SQL_ERROR = ""
        const val ITEM_NOT_ADDED = 0
    }

}

