package com.example.habittracker.network_impl.data.retrofit

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.errors.IoErrorFlow
import com.example.habittracker.core_api.domain.errors.success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class IoErrorFlowImpl @Inject constructor() : IoErrorFlow {

    private val cloudError: MutableStateFlow<Either<IoError, Unit>> =
        MutableStateFlow(Unit.success())

    override fun getError(): Flow<Either<IoError, Unit>> = cloudError

    override fun setError(error: Either<IoError, Unit>) {
        cloudError.value = error
    }


}