package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.db_impl.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.db_api.domain.usecases.UpsertHabitUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class UpsertHabitUseCaseTest {

    private lateinit var upsertHabitUseCase: UpsertHabitUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var habitToInsert: Habit

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        upsertHabitUseCase = UpsertHabitUseCase(dbHabitRepositoryFake::upsertHabit)
        habitToInsert = dbHabitRepositoryFake.habitToInsert
    }

    @Test
    fun `update habit and return id = 0`() = runTest {
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
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        if (!isSuccess) dbHabitRepositoryFake.setErrorReturn()
        val result = upsertHabitUseCase.invoke(habitToInsert)
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

    @Test
    fun `transfer habit to the repository`() = runTest {
        val preFind = dbHabitRepositoryFake.findHabit(habitToInsert)
        assertThat(preFind).isNull()
        upsertHabitUseCase.invoke(habitToInsert)
        val postFind = dbHabitRepositoryFake.findHabit(habitToInsert)
        assertThat(postFind).isNotNull()
    }
}