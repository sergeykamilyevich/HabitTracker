package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.FakeDbHabitRepository
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DeleteAllHabitsUseCaseTest {

    private lateinit var deleteAllHabitsUseCase: DeleteAllHabitsUseCase
    private lateinit var fakeDbHabitRepository: FakeDbHabitRepository
    private lateinit var successHabit: Habit

    @BeforeEach
    fun setUp() {
        fakeDbHabitRepository = FakeDbHabitRepository()
        deleteAllHabitsUseCase = DeleteAllHabitsUseCase(fakeDbHabitRepository)
        successHabit = fakeDbHabitRepository.successWhileUpsertHabit
    }

    @Test
    fun `return empty list after delete habits from the repository`() = runBlocking {
        fakeDbHabitRepository.upsertHabit(successHabit)
        val habitList = fakeDbHabitRepository.getUnfilteredList()
        assertThat(habitList is Success && habitList.result.isNotEmpty()).isTrue()
        deleteAllHabitsUseCase.invoke()
        val habitListAfterDelete = fakeDbHabitRepository.getUnfilteredList()
        assertThat(habitListAfterDelete is Success && habitListAfterDelete.result.isEmpty()).isTrue()
    }
}