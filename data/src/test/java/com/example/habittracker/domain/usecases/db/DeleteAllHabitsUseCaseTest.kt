package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DeleteAllHabitsUseCaseTest {

    private lateinit var deleteAllHabitsUseCase: DeleteAllHabitsUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var successHabit: Habit

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        deleteAllHabitsUseCase = DeleteAllHabitsUseCase(dbHabitRepositoryFake)
        successHabit = dbHabitRepositoryFake.habitToInsert
    }

    @Test
    fun `return empty list after delete habits from the repository`() = runBlocking {
        dbHabitRepositoryFake.upsertHabit(successHabit)
        val habitList = dbHabitRepositoryFake.getUnfilteredList()
        assertThat(habitList is Success && habitList.result.isNotEmpty()).isTrue()
        deleteAllHabitsUseCase.invoke()
        val habitListAfterDelete = dbHabitRepositoryFake.getUnfilteredList()
        assertThat(habitListAfterDelete is Success && habitListAfterDelete.result.isEmpty()).isTrue()
    }
}