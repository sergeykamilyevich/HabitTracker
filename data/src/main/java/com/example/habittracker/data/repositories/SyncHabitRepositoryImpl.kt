package com.example.habittracker.data.repositories

import android.util.Log
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.Either.*
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.failure
import com.example.habittracker.domain.errors.success
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.repositories.CloudHabitRepository
import com.example.habittracker.domain.repositories.DbHabitRepository
import com.example.habittracker.domain.repositories.SyncHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncHabitRepositoryImpl @Inject constructor(
    private val dbHabitRepository: DbHabitRepository,
    private val cloudHabitRepository: CloudHabitRepository
) : SyncHabitRepository {

    override suspend fun uploadAllToCloud(habitList: List<Habit>): Either<IoError, Unit> {
        var cloudError: Either<IoError, Unit> = Unit.success()
        habitList.forEach { habit ->
            val habitForUpload = habit.clearUid()
            val newUid = putAndSyncWithDb(habitForUpload)
            when (newUid) {
                is Success -> {
                    habit.done.forEach { date ->
                        val habitDoneWithNewUid = HabitDone(
                            habitUid = newUid.result,
                            date = date
                        )
                        val postResult: Either<IoError, Unit> =
                            cloudHabitRepository.postHabitDone(habitDoneWithNewUid)
                        if (postResult is Failure) cloudError = postResult
                    }
                }
                is Failure -> {
                    cloudError = newUid.error.failure()
                }
            }
        }
        return cloudError
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
        val habitList = cloudHabitRepository.getHabitList()
        var result: Either<IoError, Unit> = Unit.success()
        when (habitList) {
            is Success -> {
                dbHabitRepository.deleteAllHabits()
                habitList.result.forEach { habit ->
                    val habitId = dbHabitRepository.upsertHabit(habit)
                    when (habitId) {
                        is Success -> {
                            habit.done.forEach {
                                dbHabitRepository.addHabitDone(
                                    HabitDone(
                                        habitId = habitId.result,
                                        date = it,
                                        habitUid = habit.uid
                                    )
                                )
                            }
                        }
                        is Failure -> {
                            result = habitId.error.failure()
                        }
                    }
                }

            }
            is Failure -> {
                result = habitList.error.failure()
            }
        }
        return result
    }
}