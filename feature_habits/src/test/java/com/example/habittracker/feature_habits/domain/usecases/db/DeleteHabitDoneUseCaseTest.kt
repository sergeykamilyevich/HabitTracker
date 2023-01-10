package com.example.habittracker.feature_habits.domain.usecases.db

import com.example.habittracker.core.domain.models.HabitDone
import com.example.habittracker.feature_habits.data.repositories.DbHabitRepositoryFake
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class DeleteHabitDoneUseCaseTest {

    private lateinit var deleteHabitDoneUseCase: DeleteHabitDoneUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var habitDoneToInsert: HabitDone

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        deleteHabitDoneUseCase = DeleteHabitDoneUseCase(dbHabitRepositoryFake)
        habitDoneToInsert = dbHabitRepositoryFake.habitDoneToInsert
    }

    @Test
    fun `deleted habitDone from repository`() = runTest {
        val preFind = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(preFind).isNull()
        val habitDoneId = dbHabitRepositoryFake.addHabitDone(habitDoneToInsert)
        val findAfterAdd = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(findAfterAdd).isNotNull()
        if (habitDoneId is com.example.habittracker.core.domain.errors.Either.Success) {
            deleteHabitDoneUseCase.invoke(habitDoneId.result)
        }
        val findAfterDelete = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(findAfterDelete).isNull()
    }
}