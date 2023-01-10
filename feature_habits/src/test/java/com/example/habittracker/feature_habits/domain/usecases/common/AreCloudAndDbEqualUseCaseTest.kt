package com.example.habittracker.feature_habits.domain.usecases.common

import com.example.habittracker.core.domain.errors.Either.Failure
import com.example.habittracker.core.domain.errors.Either.Success
import com.example.habittracker.feature_habits.data.network.retrofit.IoErrorFlowFake
import com.example.habittracker.feature_habits.data.repositories.CloudHabitRepositoryFake
import com.example.habittracker.feature_habits.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.feature_habits.domain.usecases.db.DbUseCase
import com.example.habittracker.feature_habits.domain.usecases.network.CloudUseCase
import com.example.habittracker.feature_habits.domain.usecases.network.GetCloudErrorUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class AreCloudAndDbEqualUseCaseTest {

    private lateinit var areCloudAndDbEqualUseCase: AreCloudAndDbEqualUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var cloudHabitRepositoryFake: CloudHabitRepositoryFake

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        cloudHabitRepositoryFake = CloudHabitRepositoryFake()
        areCloudAndDbEqualUseCase = AreCloudAndDbEqualUseCase(
            dbUseCase = DbUseCase(dbHabitRepository = dbHabitRepositoryFake),
            cloudUseCase = CloudUseCase(
                cloudHabitRepository = cloudHabitRepositoryFake,
                getCloudErrorUseCase = GetCloudErrorUseCase(IoErrorFlowFake())
            )
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        if (!isSuccess) cloudHabitRepositoryFake.setErrorReturn()
        val result = areCloudAndDbEqualUseCase.invoke()
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return list comparison result`(areEqual: Boolean) = runTest {
        dbHabitRepositoryFake.initFilling()
        if (areEqual) {
            val listFromDb = dbHabitRepositoryFake.getUnfilteredList()
            if (listFromDb is Success) cloudHabitRepositoryFake.importHabits(listFromDb.result)
        }
        val comparisonResult = areCloudAndDbEqualUseCase.invoke()
        assertThat(comparisonResult is Success).isTrue()
        if (comparisonResult is Success) {
            assertThat(comparisonResult.result).isEqualTo(areEqual)
        }
    }
}