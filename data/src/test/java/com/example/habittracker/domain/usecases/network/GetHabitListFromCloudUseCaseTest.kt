package com.example.habittracker.domain.usecases.network

import com.example.habittracker.data.repositories.CloudHabitRepositoryFake
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.HabitType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetHabitListFromCloudUseCaseTest {

    private lateinit var getHabitListFromCloudUseCase: GetHabitListFromCloudUseCase
    private lateinit var cloudHabitRepositoryFake: CloudHabitRepositoryFake

    @BeforeEach
    fun setUp() = runBlocking {
        cloudHabitRepositoryFake = CloudHabitRepositoryFake()
        getHabitListFromCloudUseCase = GetHabitListFromCloudUseCase(cloudHabitRepositoryFake)
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
        habitsToInsert.forEach { cloudHabitRepositoryFake.putHabit(it) }
    }

    @Test
    fun `return list of habits`() = runBlocking {
        val listFromUseCase = getHabitListFromCloudUseCase.invoke()
        val listFromRepository = cloudHabitRepositoryFake.getHabitList()
        assertThat(
            listFromUseCase is Success && listFromRepository is Success
                    && listFromUseCase.result == listFromRepository.result
        ).isTrue()
    }

    @Test
    fun `return error`() = runBlocking {
        cloudHabitRepositoryFake.setErrorReturn()
        val list = getHabitListFromCloudUseCase.invoke()
        assertThat(list is Failure).isTrue()
    }
}