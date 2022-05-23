package com.example.habittracker.domain.usecases.common

import com.example.habittracker.data.network.retrofit.IoErrorFlowFake
import com.example.habittracker.data.repositories.CloudHabitRepositoryFake
import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.errors.Either.*
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.CloudUseCase
import com.example.habittracker.domain.usecases.network.GetCloudErrorUseCase
import com.google.common.truth.Truth.*
import kotlinx.coroutines.runBlocking

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

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
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runBlocking {
        if (!isSuccess) cloudHabitRepositoryFake.setErrorReturn()
        val result = areCloudAndDbEqualUseCase.invoke()
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return list comparison result`(areEqual: Boolean) = runBlocking {
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