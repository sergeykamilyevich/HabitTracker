package com.example.habittracker.data.repositories

import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitWithDone
import com.example.habittracker.domain.models.DbException
import com.example.habittracker.domain.repositories.DbHabitRepository
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import com.example.habittracker.domain.repositories.SyncHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncHabitRepositoryImpl @Inject constructor(
    private val dbHabitRepository: DbHabitRepository,
    private val networkHabitRepository: NetworkHabitRepository
) : SyncHabitRepository {

    override suspend fun uploadAllToCloud(habitList: List<HabitWithDone>) {
        habitList.forEach { habitWithDone ->
            val habitForUpload =
                habitWithDone.habit.clearUid()
            val newUid = putAndSyncWithDb(
                habit = habitForUpload,
                newHabitId = habitForUpload.id
            )
            newUid?.let {
                habitWithDone.habitDone.forEach { habitDone ->
                    println("habitItemWithDoneWithoutApiUid.habitDone $habitDone")
                    val habitDoneWithNewUid = habitDone.copy(habitUid = it)
                    networkHabitRepository.postHabitDone(habitDoneWithNewUid)
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

    override suspend fun putAndSyncWithDb(habit: Habit, newHabitId: Int): String? {
        val apiUid = networkHabitRepository.putHabit(habit)
        apiUid?.let {
            val newItemWithNewUid = habit.copy(
                id = newHabitId,
                uid = apiUid
            )
            dbHabitRepository.upsertHabit(newItemWithNewUid)
        }
        return apiUid
    }

    override suspend fun syncAllFromCloud() {
        TODO("Not yet implemented")
    }

    override suspend fun syncAllToCloud() {
        networkHabitRepository.deleteAllHabits()
        val habitListWithDone = dbHabitRepository.getUnfilteredList()
        habitListWithDone?.let {
            uploadAllToCloud(habitListWithDone)
        }
    }
}