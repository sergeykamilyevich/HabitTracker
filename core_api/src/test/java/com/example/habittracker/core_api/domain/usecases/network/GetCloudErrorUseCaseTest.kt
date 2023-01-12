package com.example.habittracker.core_api.domain.usecases.network

import com.example.habittracker.core_api.domain.errors.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
internal class GetCloudErrorUseCaseTest {

    private lateinit var getCloudErrorUseCase: GetCloudErrorUseCase
    private lateinit var ioErrorFlow: IoErrorFlow
    private lateinit var ioError: IoError

    @BeforeEach
    fun setUp() {
        ioErrorFlow = IoErrorFlowFake()
        getCloudErrorUseCase = GetCloudErrorUseCase(ioErrorFlow)
        ioError = IoError.CloudError()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false]) //TODO ParameterizedTest with sealed class Either
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runTest {
        if (!isSuccess) ioErrorFlow.setError(ioError.failure())
        else ioErrorFlow.setError(Unit.success())
        val cloudError = getCloudErrorUseCase.invoke()
        if (isSuccess) assertThat(cloudError.first() is Either.Success).isTrue()
        else assertThat(cloudError.first() is Either.Failure).isTrue()
    }

    @Test
    fun `get ioError`() = runTest {
        ioErrorFlow.setError(ioError.failure())
        val cloudError = getCloudErrorUseCase.invoke()
        assertThat(cloudError.first() == ioError.failure()).isTrue()
    }
}