package com.example.habittracker.data.db.room

import com.example.habittracker.data.db.models.HabitDbModel
import com.example.habittracker.data.db.models.HabitDoneDbModel
import com.example.habittracker.data.db.models.HabitWithDoneDbModel
import com.example.habittracker.data.repositories.DbHabitRepositoryImpl.Companion.PRIMARY_KEY_CONSTRAINT_MESSAGE
import com.example.habittracker.data.repositories.DbHabitRepositoryImpl.Companion.UNIQUE_CONSTRAINT_MESSAGE
import com.example.habittracker.domain.models.HabitListOrderBy
import com.example.habittracker.domain.models.HabitListOrderBy.*
import com.example.habittracker.domain.models.HabitType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class HabitDaoFake : HabitDao {

    private val habitList = mutableListOf<HabitDbModel>()
    private val habitDoneList = mutableListOf<HabitDoneDbModel>()
    private var indexHabits = 0
    private var indexHabitDone = 0

    val habitDoneToInsert: HabitDoneDbModel = HabitDoneDbModel(id = 0, habitId = 1, date = 0)

    val habitToInsert: HabitDbModel = HabitDbModel(
        id = 0,
        name = "habit name",
        description = "habit to insert",
        priority = 0,
        type = HabitType.GOOD,
        color = 1,
        recurrenceNumber = 1,
        recurrencePeriod = 1,
        date = 0,
        apiUid = "uid habitToInsert"
    )

    private var errorReturn: Boolean = false
    private var errorDeleteReturn: Boolean = false

    fun setErrorReturn() {
        errorReturn = true
    }

    fun setErrorDeleteReturn() {
        errorDeleteReturn = true
    }

//    fun importHabits(habitList: List<HabitDbModel>) {
//        habits.clear()
//        habitList.forEach {
//            habits.add(it)
//        }
//    }
//
//    fun importHabitDones(habitDoneList: List<HabitDoneDbModel>) {
//        this.habitDoneList.clear()
//        habitDoneList.forEach {
//            this.habitDoneList.add(it)
//        }
//    }

    fun initFilling() = runBlocking {
        val habitsToInsert = mutableListOf<HabitDbModel>()
        ('a'..'z').forEachIndexed { index, c ->
            habitsToInsert.add(
                HabitDbModel(
                    id = 0,
                    name = c.toString(),
                    description = c.toString(),
                    priority = index % 3,
                    type = HabitType.findTypeById(index % 2),
                    color = index,
                    recurrenceNumber = index,
                    recurrencePeriod = index * 2,
                    date = index,
                    apiUid = "uid $index"
                )
            )
        }
        habitsToInsert.shuffle()
        habitsToInsert.forEach { insertHabit(it) }
        val habitDonesToInsert = mutableListOf<HabitDoneDbModel>()
        (0..10).forEach {
            habitDonesToInsert.add(
                HabitDoneDbModel(
                    id = 0,
                    habitId = it,
                    date = it
                )
            )
        }
        habitDonesToInsert.shuffle()
        habitDonesToInsert.forEach { insertHabitDone(it) }
    }

    fun findHabit(habit: HabitDbModel): HabitDbModel? {
        println(habit)
        println(habitList)
        return habitList.find {
            it.name == habit.name
                    && it.date == habit.date
                    && it.type == habit.type
                    && it.apiUid == habit.apiUid
                    && it.color == habit.color
                    && it.description == habit.description
                    && it.priority == habit.priority
                    && it.recurrenceNumber == habit.recurrenceNumber
                    && it.recurrencePeriod == habit.recurrencePeriod
        }
    }

    fun findHabitDone(habitDone: HabitDoneDbModel) = habitDoneList.find {
        it.date == habitDone.date && it.habitId == habitDone.habitId
    }

    override fun getList(
        habitTypeFilter: List<String>,
        orderBy: String,
        search: String
    ): Flow<List<HabitWithDoneDbModel>>? {
        if (errorReturn) return null
        val filteredByTypeList = mutableListOf<HabitWithDoneDbModel>()
        habitList.forEach { habit ->
            val habitDonesList = mutableListOf<HabitDoneDbModel>()
            habitDoneList.forEach { habitDone ->
                if (habit.id == habitDone.habitId) habitDonesList.add(habitDone)
            }
            habitTypeFilter.forEach {
                if (habit.type == HabitType.valueOf(it))
                    filteredByTypeList.add(HabitWithDoneDbModel(habit, habitDonesList))
            }
        }
        val filteredBySearchList = filteredByTypeList.filter {
            it.habitDbModel.name.contains(search)
        }
        val filteredList = when (HabitListOrderBy.valueOf(orderBy)) {
            NAME_ASC -> filteredBySearchList.sortedBy { it.habitDbModel.name }
            NAME_DESC -> filteredBySearchList.sortedByDescending { it.habitDbModel.name }
            PRIORITY_ASC -> filteredBySearchList.sortedBy { it.habitDbModel.priority }
            PRIORITY_DESC -> filteredBySearchList.sortedByDescending { it.habitDbModel.priority }
            TIME_CREATION_ASC -> filteredBySearchList.sortedBy { it.habitDbModel.date }
            TIME_CREATION_DESC -> filteredBySearchList.sortedByDescending { it.habitDbModel.date }
        }

        return flow {
            emit(filteredList)
        }
    }

    override suspend fun getUnfilteredList(): List<HabitWithDoneDbModel>? {
        if (errorReturn) return null
        val result = mutableListOf<HabitWithDoneDbModel>()
        habitList.forEach { habit ->
            val habitDonesList = mutableListOf<HabitDoneDbModel>()
            habitDoneList.forEach { habitDone ->
                if (habit.id == habitDone.habitId) habitDonesList.add(habitDone)
            }
            result.add(HabitWithDoneDbModel(habit, habitDonesList))
        }
        return result
    }

    override suspend fun getHabitById(habitItemId: Int): HabitWithDoneDbModel? {
        if (errorReturn) return null
        val habit = habitList.find { it.id == habitItemId }
        val habitDoneList = habitDoneList.filter { it.habitId == habitItemId }
        return habit?.let { HabitWithDoneDbModel(it, habitDoneList) }
    }

    override suspend fun insertHabit(habitDbModel: HabitDbModel): Long {
        if (errorReturn) throw RuntimeException("Habit insertion error")
        if (habitDbModel.id != 0) throw RuntimeException(PRIMARY_KEY_CONSTRAINT_MESSAGE)
        habitList.find { it.name == habitDbModel.name }?.let {
            throw RuntimeException(UNIQUE_CONSTRAINT_MESSAGE)
        }
        habitList.add(habitDbModel.copy(id = ++indexHabits))
        return indexHabits.toLong()
    }

    override suspend fun updateHabit(habitDbModel: HabitDbModel) {
        if (habitDbModel.name == "error habit") throw RuntimeException("Habit update error")
        habitList.find { it.name == habitDbModel.name && it.id != habitDbModel.id }?.let {
            throw RuntimeException(UNIQUE_CONSTRAINT_MESSAGE)
        }
        habitList.find { it.id == habitDbModel.id }?.let {
            habitList.remove(it)
            habitList.add(habitDbModel)
        } ?: throw RuntimeException("Unknown habit id")
    }

    override suspend fun deleteHabit(habitItemId: Int) {
        if (!errorDeleteReturn) habitList.find { it.id == habitItemId }
            ?.let { habitList.remove(it) }
    }

    override suspend fun insertHabitDone(habitDoneDbModel: HabitDoneDbModel): Long {
        if (errorReturn) throw RuntimeException("Habit done insertion error")
        habitDoneList.add(habitDoneDbModel.copy(id = ++indexHabitDone))
        return indexHabitDone.toLong()
    }

    override suspend fun deleteHabitDone(habitDoneId: Int) {
        habitDoneList.find { it.id == habitDoneId }?.let { habitDoneList.remove(it) }
    }

    companion object {
        const val ERROR_HABIT_ID = -1
    }
}