package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
    fun `add habit and return habit id`() = runBlocking {
        val habitId = upsertHabitUseCase.invoke(habitToInsert)
        assertThat(habitId is Success).isTrue()
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

    @Test
    fun `return error`() = runBlocking {
        dbHabitRepositoryFake.setErrorReturn()
        val result = upsertHabitUseCase.invoke(habitToInsert)
        assertThat(result is Failure).isTrue()
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