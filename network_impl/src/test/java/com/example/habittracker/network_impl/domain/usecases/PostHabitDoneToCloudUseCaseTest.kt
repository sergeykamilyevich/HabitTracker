package com.example.habittracker.network_impl.domain.usecases

import com.example.habittracker.network_impl.repositories.CloudHabitRepositoryFake
import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.models.HabitDone
import com.example.habittracker.network_api.domain.usecases.PostHabitDoneToCloudUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class PostHabitDoneToCloudUseCaseTest {

    private lateinit var postHabitDoneToCloudUseCase: PostHabitDoneToCloudUseCase
    private lateinit var cloudHabitRepositoryFake: CloudHabitRepositoryFake
    private lateinit var habitDoneToInsert: HabitDone

    @BeforeEach
    fun setUp() {
        cloudHabitRepositoryFake = CloudHabitRepositoryFake()
        postHabitDoneToCloudUseCase = PostHabitDoneToCloudUseCase(cloudHabitRepositoryFake::postHabitDone)
        habitDoneToInsert = cloudHabitRepositoryFake.habitDoneToInsert
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        if (!isSuccess) cloudHabitRepositoryFake.setErrorReturn()
        val result = postHabitDoneToCloudUseCase.invoke(habitDoneToInsert)
        if (isSuccess) assertThat(result is Success).isTrue()
        else assertThat(result is Failure).isTrue()
    }

    @Test
    fun `transfer habitDone to the repository`() = runTest {
        val preFind = cloudHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(preFind).isNull()
        postHabitDoneToCloudUseCase.invoke(habitDoneToInsert)
        val postFind = cloudHabitRepositoryFake.findHabitDone(habitDoneToInsert)
        assertThat(postFind).isNotNull()
    }
}