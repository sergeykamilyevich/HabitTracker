package com.example.habittracker.data.repositories

import com.example.habittracker.domain.models.*
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

    override suspend fun uploadAllToCloud(habitList: List<HabitWithDone>) {
        habitList.forEach { habitWithDone ->
            val habitForUpload =
                habitWithDone.habit.clearUid()
            val newUid = putAndSyncWithDb(
                habit = habitForUpload,
                newHabitId = habitForUpload.id
            )
            if (newUid is Either.Success) {
                habitWithDone.habitDone.forEach { habitDone ->
                    println("habitItemWithDoneWithoutApiUid.habitDone $habitDone")
                    val habitDoneWithNewUid = habitDone.copy(habitUid = newUid.result)
                    cloudHabitRepository.postHabitDone(habitDoneWithNewUid)
                }

            }
        }
    }

    override suspend fun upsertAndSyncWithCloud(habit: Habit): Either<DbException, Int> {
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
    ): Either<CloudResponseError, String> {
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

    override suspend fun syncAllFromCloud() {
        TODO("Not yet implemented")
    }

    override suspend fun syncAllToCloud() {
        cloudHabitRepository.deleteAllHabits()
        val habitListWithDone = dbHabitRepository.getUnfilteredList()
        habitListWithDone?.let {
            uploadAllToCloud(habitListWithDone)
        }
    }
}