package com.example.habittracker.domain.usecases.common

import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.CloudUseCase
import javax.inject.Inject

class SyncAllToCloudUseCase @Inject constructor(
    private val dbUseCase: DbUseCase,
    private val cloudUseCase: CloudUseCase,
    private val uploadAllToCloudUseCase: UploadAllToCloudUseCase
) {

    suspend operator fun invoke() {
        cloudUseCase.deleteAllHabitsFromCloudUseCase()
        val habitList = dbUseCase.getUnfilteredHabitListFromDbUseCase.invoke()
        println("habitLis $habitList")
        habitList?.let {
            println("habit $it")
            uploadAllToCloudUseCase(habitList)
        }
    }
}