package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.db_impl.data.repositories.DbHabitRepositoryFake
import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.models.HabitDone
import com.example.habittracker.db_api.domain.usecases.AddHabitDoneUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class AddHabitDoneUseCaseTest {

    private lateinit var addHabitDoneUseCase: AddHabitDoneUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var habitDoneToInsert: HabitDone

    @BeforeEach
    fun setUp() {
        dbHabitRepositoryFake = DbHabitRepositoryFake()
        addHabitDoneUseCase = AddHabitDoneUseCase(dbHabitRepositoryFake::addHabitDone)
        habitDoneToInsert = dbHabitRepositoryFake.habitDoneToInsert
    }

    @Test
    fun `habitDone transferred  to the repository`() = runTest {
        val preFind = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(preFind).isNull()
        addHabitDoneUseCase.invoke(habitDoneToInsert)
        val postFind = dbHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(postFind).isNotNull()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        if (!isSuccess) dbHabitRepositoryFake.setErrorReturn()
        val result = addHabitDoneUseCase.invoke(habitDoneToInsert)
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

}