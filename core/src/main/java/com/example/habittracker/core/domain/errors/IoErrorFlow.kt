package com.example.habittracker.core.domain.errors

import kotlinx.coroutines.flow.Flow

interface IoErrorFlow {

    fun getError(): Flow<Either<IoError, Unit>>

    fun setError(error: Either<IoError, Unit>)
}