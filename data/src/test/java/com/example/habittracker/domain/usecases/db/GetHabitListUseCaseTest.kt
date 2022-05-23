package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.models.HabitListOrderBy.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.NullSource

internal class GetHabitListUseCaseTest {

    private lateinit var getHabitListUseCase: GetHabitListUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var successHabit: Habit

    @BeforeEach
    fun setUp() = runBlocking {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        getHabitListUseCase = GetHabitListUseCase(dbHabitRepositoryFake)
        successHabit = dbHabitRepositoryFake.habitToInsert
        dbHabitRepositoryFake.upsertHabit(successHabit)
        dbHabitRepositoryFake.initFilling()
    }

    @ParameterizedTest
    @EnumSource(HabitType::class)
    @NullSource
    fun `return lists of bad, good and both types habits`(habitTypeFilter: HabitType?) =
        runBlocking {
            val emptyFilter =
                HabitListFilter(orderBy = NAME_ASC, search = EMPTY_SEARCH)
            val list = getHabitListUseCase.invoke(habitTypeFilter, emptyFilter).last()
            val actualHabitGood = list.find { it.type == HabitType.GOOD }
            val actualHabitBad = list.find { it.type == HabitType.BAD }
            when (habitTypeFilter) {
                HabitType.GOOD -> assertThat(actualHabitBad == null && actualHabitGood != null).isTrue()
                HabitType.BAD -> assertThat(actualHabitBad != null && actualHabitGood == null).isTrue()
                null -> assertThat(actualHabitBad != null && actualHabitGood != null).isTrue()
            }
        }

    @ParameterizedTest
    @EnumSource(HabitListOrderBy::class)
    fun `return habit lists sorted by order from HabitListOrderBy`(habitListOrderBy: HabitListOrderBy) =
        runBlocking {
            val habitTypeFilter = null
            val emptyFilter =
                HabitListFilter(orderBy = habitListOrderBy, search = EMPTY_SEARCH)
            val list = getHabitListUseCase.invoke(habitTypeFilter, emptyFilter).last()
            val assertRequirement: (Int) -> Unit = when (habitListOrderBy) {
                NAME_ASC -> { i -> assertThat(list[i].name <= list[i + 1].name).isTrue() }
                NAME_DESC -> { i -> assertThat(list[i].name >= list[i + 1].name).isTrue() }
                TIME_CREATION_ASC -> { i -> assertThat(list[i].date <= list[i + 1].date).isTrue() }
                TIME_CREATION_DESC -> { i -> assertThat(list[i].date >= list[i + 1].date).isTrue() }
                PRIORITY_ASC -> { i -> assertThat(list[i].priority.id <= list[i + 1].priority.id).isTrue() }
                PRIORITY_DESC -> { i -> assertThat(list[i].priority.id >= list[i + 1].priority.id).isTrue() }
            }
            for (i in 0 until list.size - 1) assertRequirement(i)
        }

    @Test
    fun `return habit search result`() = runBlocking {
        val habitTypeFilter = null
        val searchString = successHabit.name
        val searchFilter =
            HabitListFilter(orderBy = NAME_ASC, search = searchString)
        val listFromUseCase = getHabitListUseCase.invoke(habitTypeFilter, searchFilter).last()
        val unfilteredList = dbHabitRepositoryFake.getUnfilteredList()
        assertThat(unfilteredList is Success).isTrue()
        if (unfilteredList is Success) {
            val filteredList = unfilteredList.result.filter { it.name.contains(searchString) }
            assertThat(listFromUseCase.sortedBy { it.name }).isEqualTo(filteredList.sortedBy { it.name })
        }
    }

    @Test
    fun `return empty search result`() = runBlocking {
        val habitTypeFilter = null
        val searchFilter =
            HabitListFilter(orderBy = NAME_ASC, search = WRONG_SEARCH)
        val list = getHabitListUseCase.invoke(habitTypeFilter, searchFilter).last()
        assertThat(list.isEmpty()).isTrue()
    }

    companion object {
        private const val EMPTY_SEARCH = ""
        private const val WRONG_SEARCH = "wrong search"
    }
}