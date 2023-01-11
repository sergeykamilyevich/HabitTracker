package com.example.habittracker.feature_habits.data.repositories

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.errors.failure
import com.example.habittracker.core_api.domain.errors.success
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.repositories.CloudHabitRepository
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.core_api.domain.repositories.SyncHabitRepository

class SyncHabitRepositoryFake(
    private val dbHabitRepository: DbHabitRepository,
    private val cloudHabitRepository: CloudHabitRepository
) : SyncHabitRepository {

    private var errorReturn: Boolean = false

    fun setErrorReturn() {
        errorReturn = true
    }

    override suspend fun uploadAllToCloud(habitList: List<Habit>): Either<IoError, Unit> {
        if (errorReturn) return IoError.CloudError().failure()
        val list = dbHabitRepository.getUnfilteredList()
        if (list is Success) {
            cloudHabitRepository.deleteAllHabits()
            (cloudHabitRepository as CloudHabitRepositoryFake).importHabits(list.result)
        }
        return Unit.success()
    }

    override suspend fun upsertAndSyncWithCloud(habit: Habit): Either<IoError, Int> {
        val resultOfUpserting = dbHabitRepository.upsertHabit(habit)
        if (resultOfUpserting is Success) {
            val habitToPut = habit.copy(id = resultOfUpserting.result)
            putAndSyncWithDb(habitToPut)
        }
        return resultOfUpserting
    }

    override suspend fun putAndSyncWithDb(habit: Habit): Either<IoError, String> {
        val apiUid = cloudHabitRepository.putHabit(habit)
        if (apiUid is Success) {
            val newItemWithNewUid = habit.copy(uid = apiUid.result)
            dbHabitRepository.upsertHabit(newItemWithNewUid)
        }
        return apiUid
    }

    override suspend fun syncAllFromCloud(): Either<IoError, Unit> {
        if (errorReturn) return IoError.CloudError().failure()
        val list = cloudHabitRepository.getHabitList()
        if (list is Success) {
            dbHabitRepository.deleteAllHabits()
            (dbHabitRepository as DbHabitRepositoryFake).importHabits(list.result)
        }
        return Unit.success()
    }
}