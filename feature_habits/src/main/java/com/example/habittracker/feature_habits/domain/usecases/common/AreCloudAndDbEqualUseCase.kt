package com.example.habittracker.feature_habits.domain.usecases.common

import com.example.habittracker.core.domain.errors.Either
import com.example.habittracker.core.domain.errors.Either.Failure
import com.example.habittracker.core.domain.errors.Either.Success
import com.example.habittracker.core.domain.errors.IoError
import com.example.habittracker.core.domain.errors.failure
import com.example.habittracker.core.domain.errors.success
import com.example.habittracker.core.domain.models.Habit
import com.example.habittracker.feature_habits.domain.usecases.db.DbUseCase
import com.example.habittracker.feature_habits.domain.usecases.network.CloudUseCase
import javax.inject.Inject

class AreCloudAndDbEqualUseCase @Inject constructor(
    private val dbUseCase: DbUseCase,
    private val cloudUseCase: CloudUseCase,
) {

    suspend operator fun invoke(): Either<IoError, Boolean> {
        val habitListCloud = cloudUseCase.getHabitListFromCloudUseCase.invoke()
        return when (habitListCloud) {
            is Success -> {
                val habitListDb = dbUseCase.getUnfilteredHabitListUseCase.invoke()
                when (habitListDb) {
                    is Success -> compareLists(
                        habitListCloud.result,
                        habitListDb.result
                    ).success()
                    is Failure -> habitListDb.error.failure()
                }
            }
            is Failure -> habitListCloud.error.failure()
        }
    }

    private fun compareLists(habitListCloud: List<Habit>, habitListDb: List<Habit>): Boolean {
        if (habitListDb.size != habitListCloud.size) return false
        habitListDb.forEach { habitDb ->
            var habitIsFound = false
            run found@{
                habitListCloud.forEach { habitCloud ->
                    if (habitDb.uid == habitCloud.uid) {
                        if (habitDb != habitCloud) return false
                        else {
                            habitIsFound = true
                            return@found
                        }
                    }
                }

            }
            if (!habitIsFound) return false
        }
        return true
    }
}