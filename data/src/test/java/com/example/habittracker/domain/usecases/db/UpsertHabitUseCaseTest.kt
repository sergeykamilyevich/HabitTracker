package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.FakeDbHabitRepository
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UpsertHabitUseCaseTest {

    private lateinit var upsertHabitUseCase: UpsertHabitUseCase
    private lateinit var fakeDbHabitRepository: FakeDbHabitRepository
    private lateinit var successHabit: Habit
    private lateinit var errorHabit: Habit

    @BeforeEach
    fun setUp() {
        fakeDbHabitRepository = FakeDbHabitRepository()
        upsertHabitUseCase = UpsertHabitUseCase(fakeDbHabitRepository)
        successHabit = fakeDbHabitRepository.successWhileUpsertHabit
        errorHabit = fakeDbHabitRepository.errorWhileUpsertHabit
    }

    @Test
    fun `add habit and return habit id`() = runBlocking {
        val habitId = upsertHabitUseCase.invoke(successHabit)
        assertThat(habitId is Success).isTrue()
    }

    @Test
    fun `update habit and return id = 0`() = runBlocking {
        val habitId = upsertHabitUseCase.invoke(successHabit)
        assertThat(habitId is Success).isTrue()
        if (habitId is Success) {
            val habitToUpdate = successHabit.copy(id = habitId.result)
            val resultOfUpserting = upsertHabitUseCase.invoke(habitToUpdate)
            assertThat(resultOfUpserting is Success && resultOfUpserting.result == 0).isTrue()
        }
    }

    @Test
    fun `return error`() = runBlocking {
        val result = upsertHabitUseCase.invoke(errorHabit)
        assertThat(result is Failure).isTrue()
    }

    @Test
    fun `transfer habit to the repository`() = runBlocking {
        val preFind = fakeDbHabitRepository.findHabit(successHabit)
        assertThat(preFind).isNull()
        upsertHabitUseCase.invoke(successHabit)
        val postFind = fakeDbHabitRepository.findHabit(successHabit)
        assertThat(postFind).isNotNull()
    }
}