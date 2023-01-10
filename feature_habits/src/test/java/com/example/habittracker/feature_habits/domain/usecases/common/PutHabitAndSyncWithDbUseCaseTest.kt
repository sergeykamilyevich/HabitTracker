package com.example.habittracker.feature_habits.domain.usecases.common

import com.example.habittracker.core.domain.errors.Either.Failure
import com.example.habittracker.core.domain.errors.Either.Success
import com.example.habittracker.feature_habits.data.repositories.CloudHabitRepositoryFake
import com.example.habittracker.feature_habits.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.feature_habits.data.repositories.SyncHabitRepositoryFake
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class PutHabitAndSyncWithDbUseCaseTest {

    private lateinit var putHabitAndSyncWithDbUseCase: PutHabitAndSyncWithDbUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var cloudHabitRepositoryFake: CloudHabitRepositoryFake
    private lateinit var syncHabitRepositoryFake: SyncHabitRepositoryFake

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        cloudHabitRepositoryFake = CloudHabitRepositoryFake()
        syncHabitRepositoryFake = SyncHabitRepositoryFake(
            dbHabitRepository = dbHabitRepositoryFake,
            cloudHabitRepository = cloudHabitRepositoryFake
        )
        putHabitAndSyncWithDbUseCase = PutHabitAndSyncWithDbUseCase(syncHabitRepositoryFake)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        val habitToInsert = cloudHabitRepositoryFake.habitToInsert
        if (!isSuccess) cloudHabitRepositoryFake.setErrorReturn()
        val result = putHabitAndSyncWithDbUseCase.invoke(habitToInsert)
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

    @Test
    fun `habit transferred to repository`() = runTest {
        val habitToInsert = cloudHabitRepositoryFake.habitToInsert
        val uid = putHabitAndSyncWithDbUseCase.invoke(habitToInsert)
        assertThat(uid is Success).isTrue()
        if (uid is Success) {
            val result = cloudHabitRepositoryFake.findHabit(habitToInsert.copy(uid = uid.result))
            assertThat(result).isNotNull()
        }
    }

}