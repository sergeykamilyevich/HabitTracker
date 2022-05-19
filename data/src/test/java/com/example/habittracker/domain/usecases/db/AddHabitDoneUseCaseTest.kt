package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.HabitDone
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AddHabitDoneUseCaseTest {

    private lateinit var addHabitDoneUseCase: AddHabitDoneUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var habitDoneToInsert: HabitDone

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        addHabitDoneUseCase = AddHabitDoneUseCase(dbHabitRepositoryFake)
        habitDoneToInsert = dbHabitRepositoryFake.habitDoneToInsert
    }

    @Test
    fun `return habitDone id`() = runBlocking {
        val habitDoneId = addHabitDoneUseCase.invoke(habitDoneToInsert)
        assertThat(habitDoneId is Success).isTrue()
    }

    @Test
    fun `return error`() = runBlocking {
        dbHabitRepositoryFake.setErrorReturn()
        val result = addHabitDoneUseCase.invoke(habitDoneToInsert)
        assertThat(result is Failure).isTrue()
    }

    @Test
    fun `transfer habitDone to the repository`() = runBlocking {
        val preFind = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(preFind).isNull()
        addHabitDoneUseCase.invoke(habitDoneToInsert)
        val postFind = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(postFind).isNotNull()
    }
}