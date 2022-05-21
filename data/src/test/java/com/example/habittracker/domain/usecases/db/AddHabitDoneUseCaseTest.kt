package com.example.habittracker.domain.usecases.db

import com.example.habittracker.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.models.HabitDone
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class AddHabitDoneUseCaseTest {

    private lateinit var addHabitDoneUseCase: AddHabitDoneUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var habitDoneToInsert: HabitDone

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        addHabitDoneUseCase = AddHabitDoneUseCase(dbHabitRepositoryFake)
        habitDoneToInsert = dbHabitRepositoryFake.habitDoneToInsert
    }

    @Test
    fun `transfer habitDone to the repository`() = runBlocking {
        val preFind = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(preFind).isNull()
        addHabitDoneUseCase.invoke(habitDoneToInsert)
        val postFind = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(postFind).isNotNull()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runBlocking {
        if (!isSuccess) dbHabitRepositoryFake.setErrorReturn()
        val result = addHabitDoneUseCase.invoke(habitDoneToInsert)
        when (isSuccess) {
            true -> assertThat(result is Success).isTrue()
            false -> assertThat(result is Failure).isTrue()
        }
    }

}