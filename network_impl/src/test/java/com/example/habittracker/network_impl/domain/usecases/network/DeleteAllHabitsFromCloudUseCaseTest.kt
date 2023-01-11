package com.example.habittracker.network_impl.domain.usecases.network

import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.usecases.network.DeleteAllHabitsFromCloudUseCase
import com.example.habittracker.network_impl.repositories.CloudHabitRepositoryFake
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class DeleteAllHabitsFromCloudUseCaseTest {

    private lateinit var deleteAllHabitsFromCloudUseCase: DeleteAllHabitsFromCloudUseCase
    private lateinit var cloudHabitRepositoryFake: CloudHabitRepositoryFake

    @BeforeEach
    fun setUp() {
        cloudHabitRepositoryFake =
            CloudHabitRepositoryFake()
        deleteAllHabitsFromCloudUseCase = DeleteAllHabitsFromCloudUseCase(cloudHabitRepositoryFake)
    }

    @Test
    fun `empty list in repository after delete habits`() = runTest {
        cloudHabitRepositoryFake.preInsertHabit()
        val habitList = cloudHabitRepositoryFake.getHabitList()
        assertThat(habitList is Success && habitList.result.isNotEmpty()).isTrue()
        deleteAllHabitsFromCloudUseCase.invoke()
        val habitListAfterDelete = cloudHabitRepositoryFake.getHabitList()
        assertThat(habitListAfterDelete is Success && habitListAfterDelete.result.isEmpty())
            .isTrue()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        if (!isSuccess) cloudHabitRepositoryFake.setErrorReturn()
        val result = deleteAllHabitsFromCloudUseCase.invoke()
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }
}