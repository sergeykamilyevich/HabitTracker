package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.db_api.domain.usecases.GetUnfilteredHabitListUseCase
import com.example.habittracker.db_impl.data.repositories.DbHabitRepositoryFake
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
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        getUnfilteredHabitListUseCase =
            GetUnfilteredHabitListUseCase(dbHabitRepositoryFake::getUnfilteredList)
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