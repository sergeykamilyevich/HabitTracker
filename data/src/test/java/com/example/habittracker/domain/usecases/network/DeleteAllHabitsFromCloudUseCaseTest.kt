package com.example.habittracker.domain.usecases.network

import com.example.habittracker.data.repositories.CloudHabitRepositoryFake
import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.Either.*
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.usecases.db.DeleteAllHabitsUseCase
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class DeleteAllHabitsFromCloudUseCaseTest {

    private lateinit var deleteAllHabitsFromCloudUseCase: DeleteAllHabitsFromCloudUseCase
    private lateinit var cloudHabitRepositoryFake: CloudHabitRepositoryFake

    @BeforeEach
    fun setUp() {
        cloudHabitRepositoryFake = CloudHabitRepositoryFake()
        deleteAllHabitsFromCloudUseCase = DeleteAllHabitsFromCloudUseCase(cloudHabitRepositoryFake)
    }

    @Test
    fun `empty list in repository after delete habits`() = runBlocking {
        cloudHabitRepositoryFake.preInsertHabit()
        val habitList = cloudHabitRepositoryFake.getHabitList()
        assertThat(habitList is Success && habitList.result.isNotEmpty()).isTrue()
        deleteAllHabitsFromCloudUseCase.invoke()
        val habitListAfterDelete = cloudHabitRepositoryFake.getHabitList()
        println(habitListAfterDelete)
        assertThat(habitListAfterDelete is Success && habitListAfterDelete.result.isEmpty())
            .isTrue()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runBlocking {
        if (!isSuccess) cloudHabitRepositoryFake.setErrorReturn()
        val result = deleteAllHabitsFromCloudUseCase.invoke()
        when (isSuccess) {
            true -> assertThat(result is Success).isTrue()
            false -> assertThat(result is Failure).isTrue()
        }
    }
}