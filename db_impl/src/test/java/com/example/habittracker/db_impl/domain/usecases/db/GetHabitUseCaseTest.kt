package com.example.habittracker.db_impl.domain.usecases.db

import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.usecases.db.GetHabitUseCase
import com.example.habittracker.db_impl.data.repositories.DbHabitRepositoryFake
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetHabitUseCaseTest {

    private lateinit var getHabitUseCase: GetHabitUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var habitToInsert: Habit

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake =
            DbHabitRepositoryFake()
        getHabitUseCase = GetHabitUseCase(dbHabitRepositoryFake)
        habitToInsert = dbHabitRepositoryFake.habitToInsert
    }

    @Test
    fun `get habit from repository`() = runTest {
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
    fun `return error`() = runTest {
        dbHabitRepositoryFake.upsertHabit(habitToInsert)
        val habit = getHabitUseCase(DbHabitRepositoryFake.ERROR_HABIT_ID)
        assertThat(habit is Failure).isTrue()
    }

}