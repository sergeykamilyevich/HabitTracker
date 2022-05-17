package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.FakeDbHabitRepository
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.models.HabitListOrderBy.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetHabitListUseCaseTest {

    private lateinit var getHabitListUseCase: GetHabitListUseCase
    private lateinit var fakeDbHabitRepository: FakeDbHabitRepository
    private lateinit var successHabit: Habit
    private lateinit var errorHabit: Habit

    @BeforeEach
    fun setUp() = runBlocking {
        fakeDbHabitRepository = FakeDbHabitRepository()
        getHabitListUseCase = GetHabitListUseCase(fakeDbHabitRepository)
        successHabit = fakeDbHabitRepository.successWhileUpsertHabit
        errorHabit = fakeDbHabitRepository.errorWhileUpsertHabit

        fakeDbHabitRepository.upsertHabit(successHabit)
        val habitsToInsert = mutableListOf<Habit>()
        ('a'..'z').forEachIndexed { index, c ->
            habitsToInsert.add(
                Habit(
                    name = c.toString(),
                    description = c.toString(),
                    priority = HabitPriority.findPriorityById((index % 3)),
                    type = HabitType.findTypeById(index % 2),
                    color = index,
                    recurrenceNumber = index,
                    recurrencePeriod = index * 2,
                    date = index
                )
            )
        }
        habitsToInsert.shuffle()
        habitsToInsert.forEach { fakeDbHabitRepository.upsertHabit(it) }
    }

    @Test
    fun `return list of good habits`() {
        val habitTypeFilter = HabitType.GOOD
        checkFilteredListByType(habitTypeFilter = habitTypeFilter)
    }

    @Test
    fun `return list of bad habits`() {
        val habitTypeFilter = HabitType.BAD
        checkFilteredListByType(habitTypeFilter = habitTypeFilter)
    }

    @Test
    fun `return list of good and bad habits`() {
        val habitTypeFilter = null
        checkFilteredListByType(habitTypeFilter = habitTypeFilter)
    }

    private fun checkFilteredListByType(habitTypeFilter: HabitType?) = runBlocking {
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

    @Test
    fun `return list of habits sorted by name ascending`() = runBlocking {
        checkListSort(NAME_ASC)
    }

    @Test
    fun `return list of habits sorted by name descending`() = runBlocking {
        checkListSort(NAME_DESC)
    }

    @Test
    fun `return list of habits sorted by creation ascending`() = runBlocking {
        checkListSort(TIME_CREATION_ASC)
    }

    @Test
    fun `return list of habits sorted by creation descending`() = runBlocking {
        checkListSort(TIME_CREATION_DESC)
    }

    @Test
    fun `return list of habits sorted by priority ascending`() = runBlocking {
        checkListSort(PRIORITY_ASC)
    }

    @Test
    fun `return list of habits sorted by priority descending`() = runBlocking {
        checkListSort(PRIORITY_DESC)
    }

    private fun checkListSort(habitListOrderBy: HabitListOrderBy) = runBlocking {
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
        val unfilteredList = fakeDbHabitRepository.getUnfilteredList()
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
            HabitListFilter(orderBy = NAME_ASC, search = errorHabit.name)
        val list = getHabitListUseCase.invoke(habitTypeFilter, searchFilter).last()
        assertThat(list.isEmpty()).isTrue()
    }

    companion object {
        const val EMPTY_SEARCH = ""
    }
}