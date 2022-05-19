package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.models.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DeleteHabitUseCaseTest {

    private lateinit var deleteHabitUseCase: DeleteHabitUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var successHabit: Habit

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        deleteHabitUseCase = DeleteHabitUseCase(dbHabitRepositoryFake)
        successHabit = dbHabitRepositoryFake.habitToInsert
    }

    @Test
    fun `return null when searching habit after deleting this habit`() = runBlocking {
        dbHabitRepositoryFake.upsertHabit(successHabit)
        val preFind = dbHabitRepositoryFake.findHabit(successHabit)
        assertThat(preFind).isNotNull()
        deleteHabitUseCase.invoke(successHabit)
        val postFind = dbHabitRepositoryFake.findHabit(successHabit)
        assertThat(postFind).isNull()
    }

}