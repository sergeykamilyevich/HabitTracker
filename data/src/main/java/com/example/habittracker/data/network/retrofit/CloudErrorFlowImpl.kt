package com.example.habittracker.data.network.retrofit

import com.example.habittracker.domain.models.CloudError
import com.example.habittracker.domain.models.CloudErrorFlow
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CloudErrorFlowImpl @Inject constructor() : CloudErrorFlow {

    private val cloudError: MutableStateFlow<Either<CloudError, Unit>> =
        MutableStateFlow(Unit.success())

    override fun getError(): Flow<Either<CloudError, Unit>> = cloudError

    override fun setError(error: Either<CloudError, Unit>) {
        cloudError.value = error
    }


}