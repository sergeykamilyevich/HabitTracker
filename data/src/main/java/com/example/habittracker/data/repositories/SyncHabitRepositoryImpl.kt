package com.example.habittracker.data.repositories

import android.util.Log
import com.example.habittracker.domain.errors.Either
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
            val newUid = putAndSyncWithDb(
                habit = habitForUpload,
                newHabitId = habitForUpload.id
            )
            when (newUid) {
                is Either.Success -> {
                    habit.done.forEach { date ->
                        val habitDoneWithNewUid = HabitDone(
                            habitUid = newUid.result,
                            date = date
                        )
                        val postResult: Either<IoError, Unit> =
                            cloudHabitRepository.postHabitDone(habitDoneWithNewUid)
                        if (postResult is Either.Failure) cloudError = postResult
                    }
                }
                is Either.Failure -> {
                    cloudError = newUid.error.failure()
                }
            }
        }
        return cloudError
    }

    override suspend fun upsertAndSyncWithCloud(habit: Habit): Either<IoError, Int> {
        val resultOfUpserting = dbHabitRepository.upsertHabit(habit)
        if (resultOfUpserting is Either.Success) {
            val newHabitId = resultOfUpserting.result
            putAndSyncWithDb(habit = habit, newHabitId = newHabitId)
        }
        return resultOfUpserting
    }

    override suspend fun putAndSyncWithDb(
        habit: Habit,
        newHabitId: Int
    ): Either<IoError, String> {
        val apiUid = cloudHabitRepository.putHabit(habit)
        if (apiUid is Either.Success) {
            val newItemWithNewUid = habit.copy(
                id = newHabitId,
                uid = apiUid.result
            )
            dbHabitRepository.upsertHabit(newItemWithNewUid)
        }
        return apiUid
    }

    override suspend fun syncAllFromCloud(): Either<IoError, Unit> {
        val habitList = cloudHabitRepository.getHabitList()
        var result: Either<IoError, Unit> = Unit.success()
        when (habitList) {
            is Either.Success -> {
                dbHabitRepository.deleteAllHabits()
                habitList.result.forEach { habit ->
                    val habitId = dbHabitRepository.upsertHabit(habit)
                    when (habitId) {
                        is Either.Success -> {
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
                        is Either.Failure -> {
                            result = habitId.error.failure()
                        }
                    }
                }

            }
            is Either.Failure -> {
                Log.e("Okhttp", "Loading list of habits failed")
                result = habitList.error.failure()
            }
        }
        return result
    }
}