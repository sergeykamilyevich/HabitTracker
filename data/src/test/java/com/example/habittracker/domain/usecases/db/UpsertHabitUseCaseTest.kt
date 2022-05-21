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

internal class UpsertHabitUseCaseTest {

    private lateinit var upsertHabitUseCase: UpsertHabitUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var habitToInsert: Habit

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        upsertHabitUseCase = UpsertHabitUseCase(dbHabitRepositoryFake)
        habitToInsert = dbHabitRepositoryFake.habitToInsert
    }

    @Test
    fun `update habit and return id = 0`() = runBlocking {
        val habitId = upsertHabitUseCase.invoke(habitToInsert)
        assertThat(habitId is Success).isTrue()
        if (habitId is Success) {
            val habitToUpdate = habitToInsert.copy(id = habitId.result)
            val resultOfUpserting = upsertHabitUseCase.invoke(habitToUpdate)
            assertThat(resultOfUpserting is Success && resultOfUpserting.result == 0).isTrue()
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runBlocking {
        if (!isSuccess) dbHabitRepositoryFake.setErrorReturn()
        val result = upsertHabitUseCase.invoke(habitToInsert)
        when (isSuccess) {
            true -> assertThat(result is Success).isTrue()
            false -> assertThat(result is Failure).isTrue()
        }
    }

    @Test
    fun `transfer habit to the repository`() = runBlocking {
        val preFind = dbHabitRepositoryFake.findHabit(habitToInsert)
        assertThat(preFind).isNull()
        upsertHabitUseCase.invoke(habitToInsert)
        val postFind = dbHabitRepositoryFake.findHabit(habitToInsert)
        assertThat(postFind).isNotNull()
    }
}