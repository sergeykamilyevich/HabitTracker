package com.example.habittracker.domain.usecases.common

import com.example.habittracker.data.repositories.CloudHabitRepositoryFake
import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.data.repositories.SyncHabitRepositoryFake
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.Either.*
import com.google.common.truth.Truth
import com.google.common.truth.Truth.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class SyncAllFromCloudUseCaseTest {

    private lateinit var syncAllFromCloudUseCase: SyncAllFromCloudUseCase
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
        syncAllFromCloudUseCase = SyncAllFromCloudUseCase(syncHabitRepositoryFake)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runBlocking {
        if (!isSuccess) syncHabitRepositoryFake.setErrorReturn()
        val result = syncAllFromCloudUseCase.invoke()
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

    @Test
    fun `habit lists in db and cloud repositories are equal`() = runBlocking {
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