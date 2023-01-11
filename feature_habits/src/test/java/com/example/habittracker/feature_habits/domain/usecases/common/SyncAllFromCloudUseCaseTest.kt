package com.example.habittracker.feature_habits.domain.usecases.common

import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.usecases.common.SyncAllFromCloudUseCase
import com.example.habittracker.network_impl.repositories.CloudHabitRepositoryFake
import com.example.habittracker.db_impl.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.feature_habits.data.repositories.SyncHabitRepositoryFake
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class SyncAllFromCloudUseCaseTest {

    private lateinit var syncAllFromCloudUseCase: SyncAllFromCloudUseCase
    private lateinit var dbHabitRepositoryFake: com.example.habittracker.db_impl.data.repositories.DbHabitRepositoryFake
    private lateinit var cloudHabitRepositoryFake: com.example.habittracker.network_impl.repositories.CloudHabitRepositoryFake
    private lateinit var syncHabitRepositoryFake: SyncHabitRepositoryFake

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake =
            com.example.habittracker.db_impl.data.repositories.DbHabitRepositoryFake()
        cloudHabitRepositoryFake =
            com.example.habittracker.network_impl.repositories.CloudHabitRepositoryFake()
        syncHabitRepositoryFake = SyncHabitRepositoryFake(
            dbHabitRepository = dbHabitRepositoryFake,
            cloudHabitRepository = cloudHabitRepositoryFake
        )
        syncAllFromCloudUseCase = SyncAllFromCloudUseCase(syncHabitRepositoryFake)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        if (!isSuccess) syncHabitRepositoryFake.setErrorReturn()
        val result = syncAllFromCloudUseCase.invoke()
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

    @Test
    fun `habit lists in db and cloud repositories are equal`() = runTest {
        val habitList = cloudHabitRepositoryFake.getHabitList()
        assertThat(habitList is Success).isTrue()
        if (habitList is Success) {
            syncAllFromCloudUseCase.invoke()
            val listFromDb = dbHabitRepositoryFake.getUnfilteredList()
            val listFromCloud = cloudHabitRepositoryFake.getHabitList()
            assertThat(listFromDb is Success && listFromCloud is Success).isTrue()
            if (listFromDb is Success && listFromCloud is Success) {
                assertThat(listFromDb.result).isEqualTo(listFromCloud.result)
            }
        }
    }

}