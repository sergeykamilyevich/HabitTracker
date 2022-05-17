package com.example.habittracker.data.repositories

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.failure
import com.example.habittracker.domain.errors.success
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.repositories.DbHabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDbHabitRepository(
    val successWhileAddHabitDone: HabitDone = HabitDone(
        date = 0,
        habitUid = "testUid"
    ),
    val errorWhileAddHabitDone: HabitDone = HabitDone(
        date = 0,
        habitUid = "errorUid"
    ),
    val successWhileUpsertHabit: Habit = Habit(
        name = "success name",
        description = "returns Success while upserting habit",
        priority = HabitPriority.NORMAL,
        type = HabitType.GOOD,
        color = 1,
        recurrenceNumber = 1,
        recurrencePeriod = 1
    ),
    val errorWhileUpsertHabit: Habit = Habit(
        name = "error name",
        description = "returns Failure while upserting habit",
        priority = HabitPriority.NORMAL,
        type = HabitType.GOOD,
        color = 1,
        recurrenceNumber = 1,
        recurrencePeriod = 1
    ),
    val errorGetListHabit: Habit = Habit(
        name = "error get list",
        description = "returns Failure while getting list",
        priority = HabitPriority.NORMAL,
        type = HabitType.GOOD,
        color = 1,
        recurrenceNumber = 1,
        recurrencePeriod = 1
    )
) : DbHabitRepository {

    private val habits = mutableListOf<Habit>()
    private val habitDones = mutableListOf<HabitDone>()
    private var indexHabits = 0
    private var indexHabitDones = 0

    override fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>> {
        val filteredByTypeList =
            if (habitType != null) habits.filter { it.type == habitType } else habits
        val filteredBySearchList = filteredByTypeList.filter {
            it.name.contains(habitListFilter.search)
        }
        val filteredList = when (habitListFilter.orderBy) {
            HabitListOrderBy.NAME_ASC -> filteredBySearchList.sortedBy { it.name }
            HabitListOrderBy.NAME_DESC -> filteredBySearchList.sortedByDescending { it.name }
            HabitListOrderBy.PRIORITY_ASC -> filteredBySearchList.sortedBy { it.priority.id }
            HabitListOrderBy.PRIORITY_DESC -> filteredBySearchList.sortedByDescending { it.priority.id }
            HabitListOrderBy.TIME_CREATION_ASC -> filteredBySearchList.sortedBy { it.date }
            HabitListOrderBy.TIME_CREATION_DESC -> filteredBySearchList.sortedByDescending { it.date }
        }
        return flow {
            emit(filteredList)
        }
    }

    override suspend fun getUnfilteredList(): Either<IoError, List<Habit>> {
        return if (habits.contains(errorGetListHabit)) IoError.SqlError().failure() else habits.success()
    }

    override suspend fun getHabitById(habitId: Int): Either<IoError, Habit> {
        return (habits.find { it.id == habitId })?.success()
            ?: IoError.SqlError("Habit with id $habitId not found").failure()
    }

    override suspend fun upsertHabit(habit: Habit): Either<IoError, Int> {
        if (habit == errorWhileUpsertHabit) {
            habits.add(habit)
            return IoError.SqlError().failure()
        }
        if (habit.id == 0) {
            indexHabits++
            habits.add(habit.copy(id = indexHabits))
            return indexHabits.success()
        }
        return habits.find { it.id == habit.id }?.let {
            habits.remove(it)
            habits.add(habit)
            ITEM_NOT_ADDED.success()
        } ?: IoError.SqlError("Habit with id ${habit.id} not found").failure()
    }

    override suspend fun deleteHabit(habit: Habit): Either<IoError, Unit> {
        return if (habits.remove(habit)) Unit.success()
        else IoError.SqlError("Error while deleting habit").failure()
    }

    override suspend fun deleteAllHabits(): Either<IoError, Unit> {
        habits.clear()
        return Unit.success()
    }

    override suspend fun addHabitDone(habitDone: HabitDone): Either<IoError, Int> {
        if (habitDone == errorWhileAddHabitDone) return IoError.SqlError().failure()
        if (habitDone.id == 0) {
            indexHabitDones++
            habitDones.add(habitDone.copy(id = indexHabitDones))
            return indexHabitDones.success()
        }
        return habitDones.find { it.id == habitDone.id }?.let {
            habitDones.remove(it)
            habitDones.add(habitDone)
            ITEM_NOT_ADDED.success()
        } ?: IoError.SqlError("HabitDone with id ${habitDone.id} not found").failure()
    }

    override suspend fun deleteHabitDone(habitDoneId: Int) {
        habitDones.find { it.id == habitDoneId }?.let {
            habitDones.remove(it)
        }
    }

    fun findHabit(habit: Habit) =
        habits.find { it == habit }


    fun findHabitDone(habitDoneToInsert: HabitDone) =
        habitDones.find {
            it.date == habitDoneToInsert.date && it.habitUid == habitDoneToInsert.habitUid
        }

    companion object {
        private const val ITEM_NOT_ADDED = 0
    }
}