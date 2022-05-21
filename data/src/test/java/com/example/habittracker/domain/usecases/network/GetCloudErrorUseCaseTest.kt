package com.example.habittracker.domain.usecases.network

import com.example.habittracker.data.network.retrofit.IoErrorFlowFake
import com.example.habittracker.data.network.retrofit.IoErrorFlowImpl
import com.example.habittracker.domain.errors.*
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class GetCloudErrorUseCaseTest {

    private lateinit var getCloudErrorUseCase: GetCloudErrorUseCase
    private lateinit var ioErrorFlow: IoErrorFlow
    private lateinit var ioError: IoError

    @BeforeEach
    fun setUp() {
        ioErrorFlow = IoErrorFlowImpl()
        getCloudErrorUseCase = GetCloudErrorUseCase(ioErrorFlow)
        ioError = IoError.CloudError()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false]) //TODO ParameterizedTest with sealed class Either
    fun `return success(true) or failure(false)`(isSuccess: Boolean) = runBlocking {
        if (!isSuccess) ioErrorFlow.setError(ioError.failure())
        else ioErrorFlow.setError(Unit.success())
        val cloudError = getCloudErrorUseCase.invoke()
        if (isSuccess) assertThat(cloudError.first() is Success).isTrue()
        else assertThat(cloudError.first() is Failure).isTrue()
    }

    @Test
    fun `get ioError`() = runBlocking {
        ioErrorFlow.setError(ioError.failure())
        val cloudError = getCloudErrorUseCase.invoke()
        assertThat(cloudError.first() == ioError.failure()).isTrue()
    }
}