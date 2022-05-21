package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetUnfilteredHabitListUseCaseTest {

    private lateinit var getUnfilteredHabitListUseCase: GetUnfilteredHabitListUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake

    @BeforeEach
    fun setUp() = runBlocking {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        getUnfilteredHabitListUseCase = GetUnfilteredHabitListUseCase(dbHabitRepositoryFake)
        dbHabitRepositoryFake.initFilling()
    }

    @Test
    fun `return whole list of habits`() = runBlocking {
        val listFromUseCase = getUnfilteredHabitListUseCase.invoke()
        val listFromRepository = dbHabitRepositoryFake.getUnfilteredList()
        assertThat(
            listFromUseCase is Success && listFromRepository is Success
                    && listFromUseCase.result == listFromRepository.result
        ).isTrue()
    }

    @Test
    fun `return error`() = runBlocking {
        dbHabitRepositoryFake.setErrorReturn()
        val list = getUnfilteredHabitListUseCase.invoke()
        assertThat(list is Failure).isTrue()
    }
}