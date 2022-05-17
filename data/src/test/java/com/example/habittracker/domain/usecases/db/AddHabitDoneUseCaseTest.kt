package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.FakeDbHabitRepository
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.HabitDone
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AddHabitDoneUseCaseTest {

    private lateinit var addHabitDoneUseCase: AddHabitDoneUseCase
    private lateinit var fakeDbHabitRepository: FakeDbHabitRepository
    private lateinit var successHabitDone: HabitDone
    private lateinit var errorHabitDone: HabitDone

    @BeforeEach
    fun setUp() {
        fakeDbHabitRepository = FakeDbHabitRepository()
        addHabitDoneUseCase = AddHabitDoneUseCase(fakeDbHabitRepository)
        successHabitDone = fakeDbHabitRepository.successWhileAddHabitDone
        errorHabitDone = fakeDbHabitRepository.errorWhileAddHabitDone
    }

    @Test
    fun `return habitDone id`() = runBlocking {
        val habitDoneId = addHabitDoneUseCase.invoke(successHabitDone)
        assertThat(habitDoneId is Success).isTrue()
    }

    @Test
    fun `return error`() = runBlocking {
        val result = addHabitDoneUseCase.invoke(errorHabitDone)
        assertThat(result is Failure).isTrue()
    }

    @Test
    fun `transfer habitDone to the repository`() = runBlocking {
        val preFind = fakeDbHabitRepository.findHabitDone(successHabitDone)
        assertThat(preFind).isNull()
        addHabitDoneUseCase.invoke(successHabitDone)
        val postFind = fakeDbHabitRepository.findHabitDone(successHabitDone)
        assertThat(postFind).isNotNull()
    }
}