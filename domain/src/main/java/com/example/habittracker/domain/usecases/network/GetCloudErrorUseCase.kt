package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.IoErrorFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCloudErrorUseCase @Inject constructor(
    private val ioErrorFlow: IoErrorFlow
) {

    operator fun invoke(
    ): Flow<Either<IoError, Unit>> = ioErrorFlow.getError()
}
