package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetHabitUseCaseTest {

    private lateinit var getHabitUseCase: GetHabitUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var habitToInsert: Habit

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        getHabitUseCase = GetHabitUseCase(dbHabitRepositoryFake)
        habitToInsert = dbHabitRepositoryFake.habitToInsert
    }

    @Test
    fun `get habit from repository`() = runBlocking {
        val habitId = dbHabitRepositoryFake.upsertHabit(habitToInsert)
        assertThat(habitId is Success).isTrue()
        if (habitId is Success) {
            val habit = getHabitUseCase(habitId.result)
            assertThat(habit is Success).isTrue()
            if (habit is Success) {
                assertThat(habit.result).isEqualTo(habitToInsert)
            }
        }
    }

    @Test
    fun `return error`() = runBlocking {
        dbHabitRepositoryFake.upsertHabit(habitToInsert)
        val habit = getHabitUseCase(DbHabitRepositoryFake.ERROR_HABIT_ID)
        assertThat(habit is Failure).isTrue()
    }

}