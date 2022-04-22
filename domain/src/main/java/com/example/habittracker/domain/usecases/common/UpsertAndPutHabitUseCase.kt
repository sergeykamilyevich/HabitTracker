package com.example.habittracker.domain.usecases.common

import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.UpsertException
import com.example.habittracker.domain.repositories.SyncHabitRepository
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.CloudUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertAndPutHabitUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository,
    private val dbUseCase: DbUseCase,
    private val cloudUseCase: CloudUseCase
) {

    suspend operator fun invoke(habit: Habit): Either<UpsertException, Int> {
        val resultOfUpserting = syncHabitRepository.upsertAndSyncWithCloud(habit)
//        val resultOfUpserting = dbUseCase.upsertHabitToDbUseCase(habit)
//        if (resultOfUpserting is Either.Success) {
//            val newHabitId = resultOfUpserting.result
//            putAndSyncWithDb(habit, newHabitId)
//        }
        return resultOfUpserting
    }

//    suspend fun putAndSyncWithDb(habit: Habit, newHabitId: Int = habit.id) {
//        val apiUid = cloudUseCase.putHabitToCloudUseCase(habit)
//        apiUid?.let {
//            val newItemWithNewUid = habit.copy(
//                id = newHabitId,
//                apiUid = apiUid
//            )
//            dbUseCase.upsertHabitToDbUseCase(newItemWithNewUid)
//        }
//    }
}
