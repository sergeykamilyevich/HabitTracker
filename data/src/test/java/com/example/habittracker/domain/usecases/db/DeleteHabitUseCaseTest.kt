package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.FakeDbHabitRepository
import com.example.habittracker.domain.models.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DeleteHabitUseCaseTest {

    private lateinit var deleteHabitUseCase: DeleteHabitUseCase
    private lateinit var fakeDbHabitRepository: FakeDbHabitRepository
    private lateinit var successHabit: Habit

    @BeforeEach
    fun setUp() {
        fakeDbHabitRepository = FakeDbHabitRepository()
        deleteHabitUseCase = DeleteHabitUseCase(fakeDbHabitRepository)
        successHabit = fakeDbHabitRepository.successWhileUpsertHabit
    }

    @Test
    fun `return null when searching habit after deleting this habit`() = runBlocking {
        fakeDbHabitRepository.upsertHabit(successHabit)
        val preFind = fakeDbHabitRepository.findHabit(successHabit)
        assertThat(preFind).isNotNull()
        deleteHabitUseCase.invoke(successHabit)
        val postFind = fakeDbHabitRepository.findHabit(successHabit)
        assertThat(postFind).isNull()
    }

}