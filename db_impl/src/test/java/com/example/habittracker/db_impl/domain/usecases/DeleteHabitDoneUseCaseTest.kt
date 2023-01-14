package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.db_impl.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.models.HabitDone
import com.example.habittracker.db_api.domain.usecases.DeleteHabitDoneUseCase
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
        deleteHabitDoneUseCase = DeleteHabitDoneUseCaseImpl(dbHabitRepositoryFake)
        habitDoneToInsert = dbHabitRepositoryFake.habitDoneToInsert
    }

    @Test
    fun `deleted habitDone from repository`() = runTest {
        val preFind = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(preFind).isNull()
        val habitDoneId = dbHabitRepositoryFake.addHabitDone(habitDoneToInsert)
        val findAfterAdd = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(findAfterAdd).isNotNull()
        if (habitDoneId is Either.Success) {
            deleteHabitDoneUseCase.invoke(habitDoneId.result)
        }
        val findAfterDelete = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(findAfterDelete).isNull()
    }
}