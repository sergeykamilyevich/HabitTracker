package com.example.habittracker.domain.usecases.common

import com.example.habittracker.data.network.retrofit.IoErrorFlowFake
import com.example.habittracker.data.repositories.CloudHabitRepositoryFake
import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.data.repositories.SyncHabitRepositoryFake
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.CloudUseCase
import com.example.habittracker.domain.usecases.network.GetCloudErrorUseCase
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class SyncAllToCloudUseCaseTest {

    private lateinit var syncAllToCloudUseCase: SyncAllToCloudUseCase
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
        syncAllToCloudUseCase = SyncAllToCloudUseCase(
            dbUseCase = DbUseCase(dbHabitRepository = dbHabitRepositoryFake),
            cloudUseCase = CloudUseCase(
                cloudHabitRepository = cloudHabitRepositoryFake,
                getCloudErrorUseCase = GetCloudErrorUseCase(IoErrorFlowFake())
            ),
            uploadAllToCloudUseCase = UploadAllToCloudUseCase(syncHabitRepositoryFake)
        )
        dbHabitRepositoryFake.initFilling()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runBlocking {
        if (!isSuccess) syncHabitRepositoryFake.setErrorReturn()
        val result = syncAllToCloudUseCase.invoke()
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

    @Test
    fun `habit lists in db and cloud repositories are equal`() = runBlocking {
        syncAllToCloudUseCase.invoke()
        val listFromCloud = cloudHabitRepositoryFake.getHabitList()
        val listFromDb = dbHabitRepositoryFake.getUnfilteredList()
        assertThat(listFromDb is Success && listFromCloud is Success).isTrue()
        if (listFromDb is Success && listFromCloud is Success) {
                assertThat(listFromDb.result).isEqualTo(listFromCloud.result)
        }
    }
}