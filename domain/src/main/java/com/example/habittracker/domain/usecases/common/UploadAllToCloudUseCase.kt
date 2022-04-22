package com.example.habittracker.domain.usecases.common

import com.example.habittracker.domain.models.HabitWithDone
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import com.example.habittracker.domain.repositories.SyncHabitRepository
import com.example.habittracker.domain.usecases.network.PostHabitDoneToCloudUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadAllToCloudUseCase @Inject constructor(
    private val postHabitDoneToCloudUseCase: PostHabitDoneToCloudUseCase,
//    private val putHabitToCloudUseCase: PutHabitToCloudUseCase,
//    private val upsertAndPutHabitUseCase: UpsertAndPutHabitUseCase,
    private val syncHabitRepository: SyncHabitRepository,
    private val networkHabitRepository: NetworkHabitRepository
) {

    suspend operator fun invoke(habitList: List<HabitWithDone>) {
//        habitList.forEach {
//            val habitItemWithoutApiUid =
//                it.toHabitWithDoneWithoutApiUid().habit
//            syncHabitRepository.putAndSyncWithDb(habitItemWithoutApiUid)
////            upsertAndPutHabitUseCase.putAndSyncWithDb(habitItemWithoutApiUid)
//            networkHabitRepository.putHabit(habitItemWithoutApiUid)
////            putHabitToCloudUseCase(habitItemWithoutApiUid)
//            it.habitDone.forEach {
//                println("habitItemWithDoneWithoutApiUid.habitDone $it")
//                postHabitDoneToCloudUseCase(it) //TODO don't work!
//            }
//        }
        syncHabitRepository.uploadAllToCloud(habitList)
    }
}