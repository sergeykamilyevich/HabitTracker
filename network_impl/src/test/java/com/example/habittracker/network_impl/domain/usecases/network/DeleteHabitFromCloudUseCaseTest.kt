package com.example.habittracker.network_impl.domain.usecases.network

import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.usecases.network.DeleteHabitFromCloudUseCase
import com.example.habittracker.network_impl.repositories.CloudHabitRepositoryFake
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class DeleteHabitFromCloudUseCaseTest {

    private lateinit var deleteHabitFromCloudUseCase: DeleteHabitFromCloudUseCase
    private lateinit var cloudHabitRepositoryFake: CloudHabitRepositoryFake
    private lateinit var habitToInsert: Habit
    private lateinit var preInsertedHabit: Habit

    @BeforeEach
    fun setUp() {
        cloudHabitRepositoryFake =
            CloudHabitRepositoryFake()
        deleteHabitFromCloudUseCase = DeleteHabitFromCloudUseCase(cloudHabitRepositoryFake)
        habitToInsert = cloudHabitRepositoryFake.habitToInsert
        preInsertedHabit = cloudHabitRepositoryFake.preInsertedHabit
    }

    @Test
    fun `habit deleted from repository`() = runTest {
        cloudHabitRepositoryFake.preInsertHabit()
        val preFind = cloudHabitRepositoryFake.findHabit(preInsertedHabit)
        assertThat(preFind).isNotNull()
        deleteHabitFromCloudUseCase.invoke(preInsertedHabit)
        val postFind = cloudHabitRepositoryFake.findHabit(preInsertedHabit)
        assertThat(postFind).isNull()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        if (isSuccess) cloudHabitRepositoryFake.preInsertHabit()
        val result = deleteHabitFromCloudUseCase.invoke(preInsertedHabit)
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

}