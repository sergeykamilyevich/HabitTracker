package com.example.habittracker.data.network.retrofit

import com.example.habittracker.domain.errors.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IoErrorFlowFake() : IoErrorFlow {

    private var cloudError: Flow<Either<IoError, Unit>> =
    flow { emit (Unit.success()) }

    override fun getError(): Flow<Either<IoError, Unit>> = cloudError

    override fun setError(error: Either<IoError, Unit>) {
        cloudError = flow { emit (error) }
    }


}