package com.example.habittracker.cloud_sync.domain.usecases.interfaces

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError

interface AreCloudAndDbEqualUseCase {

    suspend operator fun invoke(): Either<IoError, Boolean>
}