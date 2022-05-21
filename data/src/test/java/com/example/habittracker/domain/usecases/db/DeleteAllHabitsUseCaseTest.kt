package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class DeleteAllHabitsUseCaseTest {

    private lateinit var deleteAllHabitsUseCase: DeleteAllHabitsUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var habitToInsert: Habit

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        deleteAllHabitsUseCase = DeleteAllHabitsUseCase(dbHabitRepositoryFake)
        habitToInsert = dbHabitRepositoryFake.habitToInsert
    }

    @Test
    fun `empty list in repository after delete habits`() = runBlocking {
        dbHabitRepositoryFake.upsertHabit(habitToInsert)
        val habitList = dbHabitRepositoryFake.getUnfilteredList()
        assertThat(habitList is Success && habitList.result.isNotEmpty()).isTrue()
        deleteAllHabitsUseCase.invoke()
        val habitListAfterDelete = dbHabitRepositoryFake.getUnfilteredList()
        assertThat(habitListAfterDelete is Success && habitListAfterDelete.result.isEmpty()).isTrue()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runBlocking {
        if (!isSuccess) dbHabitRepositoryFake.setErrorReturn()
        val result = deleteAllHabitsUseCase.invoke()
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }
}