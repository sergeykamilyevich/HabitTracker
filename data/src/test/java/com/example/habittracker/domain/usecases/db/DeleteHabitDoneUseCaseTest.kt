package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.FakeDbHabitRepository
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.models.HabitDone
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DeleteHabitDoneUseCaseTest {

    private lateinit var deleteHabitDoneUseCase: DeleteHabitDoneUseCase
    private lateinit var fakeDbHabitRepository: FakeDbHabitRepository
    private lateinit var successHabitDone: HabitDone

    @BeforeEach
    fun setUp() {
        fakeDbHabitRepository = FakeDbHabitRepository()
        deleteHabitDoneUseCase = DeleteHabitDoneUseCase(fakeDbHabitRepository)
        successHabitDone = fakeDbHabitRepository.successWhileAddHabitDone
    }

    @Test
    fun `transfer habitDone to the repository`() = runBlocking {
        val preFind = fakeDbHabitRepository.findHabitDone(successHabitDone)
        assertThat(preFind).isNull()
        val habitDoneId = fakeDbHabitRepository.addHabitDone(successHabitDone)
        val findAfterAdd = fakeDbHabitRepository.findHabitDone(successHabitDone)
        assertThat(findAfterAdd).isNotNull()
        if (habitDoneId is Either.Success) {
            deleteHabitDoneUseCase.invoke(habitDoneId.result)
        }
        val findAfterDelete = fakeDbHabitRepository.findHabitDone(successHabitDone)
        assertThat(findAfterDelete).isNull()
    }
}