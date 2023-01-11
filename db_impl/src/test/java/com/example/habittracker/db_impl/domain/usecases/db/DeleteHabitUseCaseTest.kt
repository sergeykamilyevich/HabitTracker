package com.example.habittracker.db_impl.domain.usecases.db

import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.usecases.db.DeleteHabitUseCase
import com.example.habittracker.db_impl.data.repositories.DbHabitRepositoryFake
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class DeleteHabitUseCaseTest {

    private lateinit var deleteHabitUseCase: DeleteHabitUseCase
    private lateinit var dbHabitRepositoryFake: DbHabitRepositoryFake
    private lateinit var habitToInsert: Habit

    @BeforeEach
    fun setUp() = runTest {
        dbHabitRepositoryFake =
            DbHabitRepositoryFake()
        deleteHabitUseCase = DeleteHabitUseCase(dbHabitRepositoryFake)
        habitToInsert = dbHabitRepositoryFake.habitToInsert
    }

    @Test
    fun `habit deleted from repository`() = runTest {
        dbHabitRepositoryFake.upsertHabit(habitToInsert)
        val preFind = dbHabitRepositoryFake.findHabit(habitToInsert)
        assertThat(preFind).isNotNull()
        deleteHabitUseCase.invoke(habitToInsert)
        val postFind = dbHabitRepositoryFake.findHabit(habitToInsert)
        assertThat(postFind).isNull()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        if (isSuccess) dbHabitRepositoryFake.upsertHabit(habitToInsert)
        val result = deleteHabitUseCase.invoke(habitToInsert)
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

}