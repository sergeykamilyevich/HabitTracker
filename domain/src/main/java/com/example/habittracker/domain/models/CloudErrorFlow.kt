package com.example.habittracker.domain.models

import kotlinx.coroutines.flow.Flow

interface CloudErrorFlow {

    fun getError(): Flow<Either<CloudError, Unit>>

    fun setError(error: Either<CloudError, Unit>)
}