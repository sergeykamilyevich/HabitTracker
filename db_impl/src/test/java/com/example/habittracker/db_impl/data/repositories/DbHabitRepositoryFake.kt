package com.example.habittracker.db_impl.data.repositories

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.errors.failure
import com.example.habittracker.core_api.domain.errors.success
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitDone
import com.example.habittracker.core_api.domain.models.HabitListOrderBy.*
import com.example.habittracker.core_api.domain.models.HabitPriority
import com.example.habittracker.core_api.domain.models.HabitType
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DbHabitRepositoryFake @Inject constructor()  : DbHabitRepository {

    private val habitList = mutableListOf<Habit>()
    private val habitDoneList = mutableListOf<HabitDone>()
    private var indexHabits = 0
    private var indexHabitDones = 0

    val habitDoneToInsert: HabitDone =
        HabitDone(date = 0, habitUid = "testUid")

    val habitToInsert: Habit =
        Habit(
            name = "habit name",
            description = "habit to insert",
            priority = HabitPriority.NORMAL,
            type = HabitType.GOOD,
            color = 1,
            recurrenceNumber = 1,
            recurrencePeriod = 1
        )

    private var errorReturn: Boolean = false

    fun setErrorReturn() {
        errorReturn = true
    }


    fun importHabits(habitList: List<Habit>) {
        this.habitList.clear()
        habitList.forEach {
            this.habitList.add(it)
        }
    }

    fun initFilling() = runBlocking {
        val habitsToInsert = mutableListOf<Habit>()
        ('a'..'z').forEachIndexed { index, c ->
            habitsToInsert.add(
                Habit(
                    name = c.toString(),
                    description = c.toString(),
                    priority = HabitPriority.findPriorityById(
                        (index % 3)
                    ),
                    type = HabitType.findTypeById(index % 2),
                    color = index,
                    recurrenceNumber = index,
                    recurrencePeriod = index * 2,
                    date = index,
                    uid = "uid $index"
                )
            )
        }
        habitsToInsert.shuffle()
        habitsToInsert.forEach { upsertHabit(it) }
    }

    override fun getHabitList(
        habitType: HabitType?,
        habitListFilter: com.example.habittracker.core_api.domain.models.HabitListFilter
    ): Flow<List<Habit>> {
        val filteredByTypeList =
            if (habitType != null) habitList.filter { it.type == habitType } else habitList
        val filteredBySearchList = filteredByTypeList.filter {
            it.name.contains(habitListFilter.search)
        }
        val filteredList = when (habitListFilter.orderBy) {
            NAME_ASC -> filteredBySearchList.sortedBy { it.name }
            NAME_DESC -> filteredBySearchList.sortedByDescending { it.name }
            PRIORITY_ASC -> filteredBySearchList.sortedBy { it.priority.id }
            PRIORITY_DESC -> filteredBySearchList.sortedByDescending { it.priority.id }
            TIME_CREATION_ASC -> filteredBySearchList.sortedBy { it.date }
            TIME_CREATION_DESC -> filteredBySearchList.sortedByDescending { it.date }
        }
        return flow {
            emit(filteredList)
        }
    }

    override suspend fun getUnfilteredList(): Either<IoError, List<Habit>> =
        if (errorReturn) IoError.SqlError().failure() else habitList.success()

    override suspend fun getHabitById(habitId: Int): Either<IoError, Habit> =
        (habitList.find { it.id == habitId })?.success()
            ?: IoError.SqlError("Habit with id $habitId not found").failure()

    override suspend fun upsertHabit(habit: Habit): Either<IoError, Int> {
        if (errorReturn) return IoError.SqlError().failure()
        if (habit.id == 0) {
            indexHabits++
            habitList.add(habit.copy(id = indexHabits))
            return indexHabits.success()
        }
        return habitList.find { it.id == habit.id }?.let {
            habitList.remove(it)
            habitList.add(habit)
            ITEM_NOT_ADDED.success()
        } ?: IoError.SqlError("Habit with id ${habit.id} not found").failure()
    }

    override suspend fun deleteHabit(habit: Habit): Either<IoError, Unit> {
        val resultOfDeleting = habitList.remove(habit)
        return if (resultOfDeleting) Unit.success() else IoError.SqlError("Habit $habit not found")
            .failure()
    }

    override suspend fun deleteAllHabits(): Either<IoError, Unit> =
        if (errorReturn) IoError.SqlError("Error while deleting habit").failure()
        else {
            habitList.clear()
            Unit.success()
        }

    override suspend fun addHabitDone(habitDone: HabitDone): Either<IoError, Int> {
        if (errorReturn) return IoError.SqlError().failure()
        if (habitDone.id == 0) {
            indexHabitDones++
            habitDoneList.add(habitDone.copy(id = indexHabitDones))
            return indexHabitDones.success()
        }
        return habitDoneList.find { it.id == habitDone.id }?.let {
            habitDoneList.remove(it)
            habitDoneList.add(habitDone)
            ITEM_NOT_ADDED.success()
        } ?: IoError.SqlError("HabitDone with id ${habitDone.id} not found").failure()
    }

    override suspend fun deleteHabitDone(habitDoneId: Int) {
        habitDoneList.find { it.id == habitDoneId }?.let {
            habitDoneList.remove(it)
        }
    }

    fun findHabit(habit: Habit) = habitList.find { it == habit }

    fun findHabitDone(habitDone: HabitDone) = habitDoneList.find {
        it.date == habitDone.date && it.habitUid == habitDone.habitUid
    }

    companion object {
        private const val ITEM_NOT_ADDED = 0
        const val ERROR_HABIT_ID = -1
    }
}