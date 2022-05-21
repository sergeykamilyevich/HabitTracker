package com.example.habittracker.data.repositories

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.failure
import com.example.habittracker.domain.errors.success
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.HabitType
import com.example.habittracker.domain.repositories.CloudHabitRepository

class CloudHabitRepositoryFake : CloudHabitRepository {

    private val habits = mutableListOf<Habit>()
    private val habitDones = mutableListOf<HabitDone>()
    private var indexHabits = 0

    val habitDoneToInsert: HabitDone = HabitDone(date = 0, habitUid = "testUid")

    val habitToInsert: Habit = Habit(
        name = "habit name",
        description = "habit to insert",
        priority = HabitPriority.NORMAL,
        type = HabitType.GOOD,
        color = 1,
        recurrenceNumber = 1,
        recurrencePeriod = 1
    )

    val preInsertedHabit: Habit = Habit(
        name = "preinserted habit name",
        description = "preinserted habit",
        priority = HabitPriority.NORMAL,
        type = HabitType.GOOD,
        color = 1,
        recurrenceNumber = 1,
        recurrencePeriod = 1,
        uid = "habit uid"
    )

    private var errorReturn: Boolean = false

    fun preInsertHabit() {
        habits.add(preInsertedHabit)
    }

    fun setErrorReturn() {
        errorReturn = true
    }

    override suspend fun getHabitList(): Either<IoError, List<Habit>> =
        if (errorReturn) IoError.CloudError().failure() else habits.success()

    override suspend fun putHabit(habit: Habit): Either<IoError, String> {
        if (errorReturn) return IoError.SqlError().failure()
        if (habit.uid == Habit.EMPTY_UID) {
            indexHabits++
            val uid = indexHabits.toString()
            habits.add(habit.copy(uid = uid))
            return uid.success()
        }
        return habits.find { it.uid == habit.uid }?.let {
            habits.remove(it)
            habits.add(habit)
            habit.uid.success()
        } ?: IoError.CloudError(
            code = BAD_REQUEST,
            message = "Habit with uid ${habit.uid} not found"
        ).failure()
    }

    override suspend fun deleteHabit(habit: Habit): Either<IoError, Unit> {
        return habits.find { it.uid == habit.uid }?.let {
            habits.remove(it)
            Unit.success()
        } ?: IoError.CloudError(
            code = BAD_REQUEST,
            message = "Habit with uid ${habit.uid} not found"
        ).failure()
    }

    override suspend fun postHabitDone(habitDone: HabitDone): Either<IoError, Unit> =
        if (errorReturn) IoError.CloudError().failure()
        else {
            habitDones.add(habitDone)
            Unit.success()
        }

    override suspend fun deleteAllHabits(): Either<IoError, Unit> =
        if (errorReturn) IoError.CloudError().failure()
        else {
            habits.clear()
            Unit.success()
        }

    fun findHabit(habit: Habit) = habits.find { it == habit }

    fun findHabitDone(habitDoneToInsert: HabitDone) = habitDones.find {
        it.date == habitDoneToInsert.date && it.habitUid == habitDoneToInsert.habitUid
    }

    companion object {
        private const val BAD_REQUEST = 400
    }
}