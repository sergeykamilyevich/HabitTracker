package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.models.CloudError
import com.example.habittracker.domain.models.CloudErrorFlow
import com.example.habittracker.domain.models.Either
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCloudErrorUseCase @Inject constructor(
    private val cloudErrorFlow: CloudErrorFlow
) {

    operator fun invoke(
    ): Flow<Either<CloudError, Unit>> = cloudErrorFlow.getError()
}
