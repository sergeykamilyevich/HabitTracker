package com.example.habittracker.core_api.domain.usecases.db

import com.example.habittracker.core_api.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetUnfilteredHabitListUseCaseTest {

    private lateinit var getUnfilteredHabitListUseCase: GetUnfilteredHabitListUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake

    @BeforeEach
    fun setUp() = runTest {
        dbHabitRepositoryFake =
            DbHabitRepositoryFake()
        getUnfilteredHabitListUseCase = GetUnfilteredHabitListUseCase(dbHabitRepositoryFake)
        dbHabitRepositoryFake.initFilling()
    }

    @Test
    fun `return whole list of habits`() = runTest {
        val listFromUseCase = getUnfilteredHabitListUseCase.invoke()
        val listFromRepository = dbHabitRepositoryFake.getUnfilteredList()
        assertThat(
            listFromUseCase is Success && listFromRepository is Success
                    && listFromUseCase.result == listFromRepository.result
        ).isTrue()
    }

    @Test
    fun `return error`() = runTest {
        dbHabitRepositoryFake.setErrorReturn()
        val list = getUnfilteredHabitListUseCase.invoke()
        assertThat(list is Failure).isTrue()
    }
}