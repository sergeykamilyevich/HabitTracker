package com.example.habittracker.domain.usecases.common

import com.example.habittracker.data.repositories.CloudHabitRepositoryFake
import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.data.repositories.SyncHabitRepositoryFake
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.Either.Success
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class UploadAllToCloudUseCaseTest {

    private lateinit var uploadAllToCloudUseCase: UploadAllToCloudUseCase
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
        uploadAllToCloudUseCase = UploadAllToCloudUseCase(syncHabitRepositoryFake)
        dbHabitRepositoryFake.initFilling()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        if (!isSuccess) syncHabitRepositoryFake.setErrorReturn()
        val habitList = dbHabitRepositoryFake.getUnfilteredList()
        assertThat(habitList is Success).isTrue()
        if (habitList is Success) {
            val result = uploadAllToCloudUseCase.invoke(habitList.result)
            if (isSuccess) assertThat(result is Success).isTrue()
            else assertThat(result is Either.Failure).isTrue()
        }
    }

    @Test
    fun `habit lists in db and cloud repositories are equal`() = runTest {
        val listFromDb = dbHabitRepositoryFake.getUnfilteredList()
        assertThat(listFromDb is Success).isTrue()
        if (listFromDb is Success) {
            uploadAllToCloudUseCase.invoke(listFromDb.result)
            val listFromCloud = cloudHabitRepositoryFake.getHabitList()
            assertThat(listFromCloud is Success).isTrue()
            if (listFromCloud is Success) {
                assertThat(listFromDb.result).isEqualTo(listFromCloud.result)
            }
        }
    }
}