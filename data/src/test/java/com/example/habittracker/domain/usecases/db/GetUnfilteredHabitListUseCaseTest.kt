package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.FakeDbHabitRepository
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.HabitType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetUnfilteredHabitListUseCaseTest {

    private lateinit var getUnfilteredHabitListUseCase: GetUnfilteredHabitListUseCase
    private lateinit var fakeDbHabitRepository: FakeDbHabitRepository
    private lateinit var successHabit: Habit
    private lateinit var errorGetListHabit: Habit
    private val habitsToInsert = mutableListOf<Habit>()

    @BeforeEach
    fun setUp() = runBlocking {
        fakeDbHabitRepository = FakeDbHabitRepository()
        getUnfilteredHabitListUseCase = GetUnfilteredHabitListUseCase(fakeDbHabitRepository)
        successHabit = fakeDbHabitRepository.successWhileUpsertHabit
        errorGetListHabit = fakeDbHabitRepository.errorGetListHabit

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
    fun `return whole list of habits`() = runBlocking {
        val list = getUnfilteredHabitListUseCase.invoke()
        assertThat(list is Success).isTrue()
        if (list is Success) {
            assertThat(list.result.sortedBy { it.name }).isEqualTo(habitsToInsert.sortedBy { it.name })
        }
    }

    @Test
    fun `return error`() = runBlocking {
        fakeDbHabitRepository.upsertHabit(errorGetListHabit)
        val list = getUnfilteredHabitListUseCase.invoke()
        assertThat(list is Failure).isTrue()
    }
}