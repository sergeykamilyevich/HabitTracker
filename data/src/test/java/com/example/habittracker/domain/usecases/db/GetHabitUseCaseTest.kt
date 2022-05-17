package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.FakeDbHabitRepository
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetHabitUseCaseTest {

    private lateinit var getHabitUseCase: GetHabitUseCase
    private lateinit var fakeDbHabitRepository: FakeDbHabitRepository
    private lateinit var successHabit: Habit
    private lateinit var errorHabit: Habit


    @BeforeEach
    fun setUp() {
        fakeDbHabitRepository = FakeDbHabitRepository()
        getHabitUseCase = GetHabitUseCase(fakeDbHabitRepository)
        successHabit = fakeDbHabitRepository.successWhileUpsertHabit
        errorHabit = fakeDbHabitRepository.errorWhileUpsertHabit
    }

    @Test
    fun `get habit from repository`() = runBlocking {
        val habitId = fakeDbHabitRepository.upsertHabit(successHabit)
        assertThat(habitId is Success).isTrue()
        if (habitId is Success) {
            val habit = getHabitUseCase(habitId.result)
            assertThat(habit is Success).isTrue()
            if (habit is Success) {
                assertThat(habit.result).isEqualTo(successHabit)
            }
        }
    }

    @Test
    fun `get error from repository`() = runBlocking {
        fakeDbHabitRepository.upsertHabit(successHabit)
        val habit = getHabitUseCase(errorHabit.id)
        assertThat(habit is Failure).isTrue()
    }
}